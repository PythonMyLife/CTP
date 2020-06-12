package broker.service;


import broker.dao.DepthDao;
import broker.dao.OrderDao;
import broker.dao.TransactionDao;
import broker.entity.Order;
import broker.entity.Transaction;
import broker.websocket.DepthServer;
import broker.websocket.OrderServer;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private DepthDao depthDao;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private DepthServer depthServer;

    @Autowired
    private OrderServer orderServer;

    @KafkaListener(groupId = "broker-gateway", topics = {"order"})
    public void orderFromTrader(ConsumerRecord record) throws ParseException {
        System.out.println(record.value().toString());
        JSONParser jp = new JSONParser();
        JSONObject object = (JSONObject) jp.parse(record.value().toString());
        String time = String.valueOf(record.timestamp());
        Order order = new Order((String) object.get("type"), time, object, 0);
        Order orderSaved = orderDao.save(order);
        JSONObject deal = new JSONObject();
        deal.put("id", orderSaved.getId());
        deal.put("type", orderSaved.getType());
        deal.put("order", orderSaved.getObject());
        deal.put("time", orderSaved.getTime());
        kafkaTemplate.send("order-pending", deal.toJSONString());
        kafkaTemplate.send("order-deal", deal.toJSONString());
    }

    @KafkaListener(groupId = "broker-gateway", topics = {"order-finish"})
    public void orderFromDealer(ConsumerRecord record) throws ParseException {
        String time = String.valueOf(record.timestamp());
        JSONParser jp = new JSONParser();
        JSONObject object = (JSONObject) jp.parse(record.value().toString());
        // transaction
        String productId = (String) object.get("productId");
        String price = String.valueOf(object.get("price"));
        Integer num = Integer.valueOf(String.valueOf(object.get("num")));
        String traderSell = (String) object.get("traderSell");
        String traderBuy = (String) object.get("traderBuy");
        String brokerSell = (String) object.get("brokerSell");
        String brokerBuy = (String) object.get("brokerBuy");
        Transaction transaction = new Transaction(time, productId, price, num,
                traderSell, traderBuy, brokerSell, brokerBuy);
        Transaction transactionSaved = transactionDao.save(transaction);
        // change order status
        String orderSell = (String) object.get("orderSell");
        String orderBuy = (String) object.get("orderBuy");
        if((Integer) object.get("orderSellStatus") > 0) {
            Optional<Order> orderS =  orderDao.findById(orderSell);
            if(orderS.isPresent()) {
                Order orderSC = orderS.get();
                orderSC.setStatus(1);
                orderDao.save(orderSC);
            }
        }
        if((Integer) object.get("orderBuyStatus") > 0) {
            Optional<Order> orderB =  orderDao.findById(orderBuy);
            if(orderB.isPresent()) {
                Order orderBC = orderB.get();
                orderBC.setStatus(1);
                orderDao.save(orderBC);
            }
        }
        // notify trader gateway
        kafkaTemplate.send("transaction", object.toJSONString());
        String depth = "";
        try {
            depth = depthDao.findAll(productId);
            kafkaTemplate.send("depth", depth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // notify brokers
        try {
            depthServer.update(productId, depth);
            orderServer.sendAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
