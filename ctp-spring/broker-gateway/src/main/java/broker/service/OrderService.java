package broker.service;


import broker.dao.OrderDao;
import broker.dao.TransactionDao;
import broker.entity.Order;
import broker.entity.Transaction;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private KafkaTemplate kafkaTemplate;

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
        String productId = (String) object.get("productId");
        String price = (String) object.get("price");
        Integer num = Integer.valueOf((String) object.get("num"));
        String traderSell = (String) object.get("traderSell");
        String traderBuy = (String) object.get("traderBuy");
        String brokerSell = (String) object.get("brokerSell");
        String brokerBuy = (String) object.get("brokerBuy");
        Transaction transaction = new Transaction(time, productId, price, num,
                traderSell, traderBuy, brokerSell, brokerBuy);
        Transaction transactionSaved = transactionDao.save(transaction);
        // change order status
        // notify trader gateway
        // notify brokers
    }
}
