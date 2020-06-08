package broker.service;

import broker.dao.ProductDao;
import broker.entity.Product;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

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
        return object;
    }
}
