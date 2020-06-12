package dealer.service;

import dealer.dao.DepthDao;
import dealer.entity.Depth;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class DepthService {
    private final KafkaTemplate kafkaTemplate;
    private final DepthDao depthDao;
    private Map<String, PriorityBlockingQueue> productDepth;

    public DepthService(KafkaTemplate kafkaTemplate, DepthDao depthDao) {
        this.kafkaTemplate = kafkaTemplate;
        this.depthDao = depthDao;
        this.productDepth = new HashMap<>();
    }

    @KafkaListener(groupId = "broker-dealer", topics = {"order-deal"})
    public synchronized void dealOrder(ConsumerRecord record) throws ParseException {
        System.out.println(record.value().toString());
        JSONParser jp = new JSONParser();
        JSONObject object = (JSONObject) jp.parse(record.value().toString());
        String orderId = String.valueOf(object.get("id"));
        String time = String.valueOf(object.get("time"));
        String type = String.valueOf(object.get("type"));
        JSONObject order = (JSONObject) jp.parse(String.valueOf(object.get("object")));
        String productId = String.valueOf(order.get("productId"));
        if(!productDepth.containsKey(productId + "Sell")) {
            initFromDatabase(String.valueOf(order.get("productId")));
        }
        switch (type) {
            case "limit":
                String action = String.valueOf(order.get("action"));
                if(action.equals("buy")) {
                    Depth depth = (Depth) productDepth.get(productId + "Sell").peek();
                    if(depth == null) {
                        order.put("time", time);
                        order.put("orderId", orderId);
                        productDepth.get(productId + "Buy").add(order);
                    } else {
                        Double priceBuy = Double.valueOf(String.valueOf(object.get("price")));
                        if(priceBuy.compareTo(depth.getPrice()) >= 0) {
                            // transaction and delete depth
                        }
                    }
                }
        }
    }

    public void initFromDatabase(String productId) {
        try {
            JSONArray array = depthDao.findAll(productId);
            PriorityBlockingQueue<Depth> depthSells = new PriorityBlockingQueue<>();
            PriorityBlockingQueue<Depth> depthBuys = new PriorityBlockingQueue<>();
            for (Object o : array) {
                JSONObject object = (JSONObject) o;
                Depth depth = new Depth(object);
                if (depth.getAction().equals("buy")) {
                    depthBuys.add(depth);
                } else {
                    depthSells.add(depth);
                }
            }
            productDepth.put(productId + "Sell", depthSells);
            productDepth.put(productId + "Buy", depthBuys);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
