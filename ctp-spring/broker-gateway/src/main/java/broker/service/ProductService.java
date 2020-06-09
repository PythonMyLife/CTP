package broker.service;

import broker.dao.ProductDao;
import broker.entity.Broker;
import broker.entity.Product;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public JSONObject addProduct(String name, String category, String period) {
        JSONObject object = new JSONObject();
        if(name == null || category == null || period == null) {
            object.put("status", 0);
            object.put("message", "期货名，类别和时期都不能为空！");
            return object;
        }
        if(productDao.existsByNameAndCategoryAndPeriod(name, category, period)) {
            object.put("status", 0);
            object.put("message", "期货已存在！");
            return object;
        }
        Product product = new Product(name, category, period);
        Product productSaved = productDao.save(product);
        if(productSaved != null) {
            object.put("status", 1);
            object.put("message", productSaved);
        } else {
            object.put("status", 0);
            object.put("message", "创建失败！");
        }
        sendProductsMessage();
        return object;
    }

    @KafkaListener(groupId = "broker-gateway", topics = {"getProducts"})
    public void getProducts(ConsumerRecord record) {
        System.out.println(record.value().toString());
        sendProductsMessage();
    }

    public void sendProductsMessage() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        List<Product> products = productDao.findAll();
        for(Product product : products) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", product.getId());
            jsonObject.put("name", product.getName());
            jsonObject.put("category", product.getCategory());
            jsonObject.put("period", product.getPeriod());
            array.add(jsonObject);
        }
        object.put("message", array);
        kafkaTemplate.send("brokerList", object.toJSONString());
    }
}
