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
    private Map<String, PriorityBlockingQueue<Depth>> productDepth;
    private Double priceDeal;

    public DepthService(KafkaTemplate kafkaTemplate, DepthDao depthDao) {
        this.kafkaTemplate = kafkaTemplate;
        this.depthDao = depthDao;
        this.productDepth = new HashMap<>();
        this.priceDeal = 0.0;
    }

    @KafkaListener(groupId = "broker-dealer", topics = {"order-deal"})
    public synchronized void dealOrder(ConsumerRecord record) throws ParseException {
        System.out.println(record.value().toString());
        JSONParser jp = new JSONParser();
        JSONObject object = (JSONObject) jp.parse(record.value().toString());
        String orderId = String.valueOf(object.get("id"));
        String time = String.valueOf(object.get("time"));
        String type = String.valueOf(object.get("type"));
        JSONObject order = (JSONObject) jp.parse(String.valueOf(object.get("order")));
        String productId = String.valueOf(order.get("productId"));
        if(!productDepth.containsKey(productId + "Sell")) {
            initFromDatabase(String.valueOf(order.get("productId")));
        }
        String action = String.valueOf(order.get("action"));
        switch (type) {
            case "limit":
                if(action.equals("buy")) {
                    Double priceBuy = Double.valueOf(String.valueOf(order.get("price")));
                    Integer num = Integer.valueOf(String.valueOf(order.get("num")));
                    while(num > 0) {
                        Depth depth = productDepth.get(productId + "Sell").peek();
                        if(depth == null) {
                            order.put("time", time);
                            order.put("num", num);
                            order.put("orderId", orderId);
                            String id = depthDao.insertOne(productId, order);
                            order.put("id", id);
                            Depth depth1 = new Depth(order);
                            productDepth.get(productId + "Buy").add(depth1);
                            num = 0;
                        } else {
                            System.out.println(depth.getPrice());
                            if(priceBuy.compareTo(depth.getPrice()) >= 0) {
                                // transaction and update depth
                                if(depth.getNum().compareTo(num) > 0) {
                                    depth.setNum(depth.getNum() - num);
                                    JSONObject update = new JSONObject();
                                    update.put("_id", depth.getId());
                                    update.put("num", depth.getNum());
                                    depthDao.updateOne(productId, update);
                                    priceDeal = depth.getPrice();
                                    JSONObject transaction = new JSONObject();
                                    transaction.put("productId", productId);
                                    transaction.put("price", depth.getPrice());
                                    transaction.put("num", num);
                                    transaction.put("traderSell", depth.getTrader());
                                    transaction.put("traderBuy", String.valueOf(order.get("trader")));
                                    transaction.put("brokerSell", depth.getBroker());
                                    transaction.put("brokerBuy", String.valueOf(order.get("broker")));
                                    transaction.put("orderSell", depth.getOrderId());
                                    transaction.put("orderBuy", orderId);
                                    transaction.put("orderSellStatus", 0);
                                    transaction.put("orderBuyStatus", 1);
                                    kafkaTemplate.send("order-finish", transaction.toJSONString());
                                    num = 0;
                                } else {
                                    productDepth.get(productId + "Sell").poll();
                                    JSONObject delete = new JSONObject();
                                    delete.put("_id", depth.getId());
                                    depthDao.deleteOne(productId, delete);
                                    num -= depth.getNum();
                                    priceDeal = depth.getPrice();
                                    JSONObject transaction = new JSONObject();
                                    transaction.put("productId", productId);
                                    transaction.put("price", depth.getPrice());
                                    transaction.put("num", depth.getNum());
                                    transaction.put("traderSell", depth.getTrader());
                                    transaction.put("traderBuy", String.valueOf(order.get("trader")));
                                    transaction.put("brokerSell", depth.getBroker());
                                    transaction.put("brokerBuy", String.valueOf(order.get("broker")));
                                    transaction.put("orderSell", depth.getOrderId());
                                    transaction.put("orderBuy", orderId);
                                    transaction.put("orderSellStatus", 1);
                                    int left = num > 0 ? 0 : 1;
                                    transaction.put("orderBuyStatus", left);
                                    kafkaTemplate.send("order-finish", transaction.toJSONString());
                                }
                            } else {
                                order.put("time", time);
                                order.put("num", num);
                                order.put("orderId", orderId);
                                String id = depthDao.insertOne(productId, order);
                                order.put("id", id);
                                Depth depth1 = new Depth(order);
                                productDepth.get(productId + "Buy").add(depth1);
                                num = 0;
                            }
                        }
                    }
                } else {
                    Double priceSell = Double.valueOf(String.valueOf(order.get("price")));
                    Integer num = Integer.valueOf(String.valueOf(order.get("num")));
                    while(num > 0) {
                        Depth depth = (Depth) productDepth.get(productId + "Buy").peek();
                        if(depth == null) {
                            order.put("time", time);
                            order.put("num", num);
                            order.put("orderId", orderId);
                            String id = depthDao.insertOne(productId, order);
                            order.put("id", id);
                            Depth depth1 = new Depth(order);
                            productDepth.get(productId + "Sell").add(depth1);
                            num = 0;
                        } else {
                            if(priceSell.compareTo(depth.getPrice()) <= 0) {
                                // transaction and update depth
                                if(depth.getNum().compareTo(num) > 0) {
                                    depth.setNum(depth.getNum() - num);
                                    JSONObject update = new JSONObject();
                                    update.put("_id", depth.getId());
                                    update.put("num", depth.getNum());
                                    depthDao.updateOne(productId, update);
                                    priceDeal = depth.getPrice();
                                    JSONObject transaction = new JSONObject();
                                    transaction.put("productId", productId);
                                    transaction.put("price", depth.getPrice());
                                    transaction.put("num", num);
                                    transaction.put("traderBuy", depth.getTrader());
                                    transaction.put("traderSell", String.valueOf(order.get("trader")));
                                    transaction.put("brokerBuy", depth.getBroker());
                                    transaction.put("brokerSell", String.valueOf(order.get("broker")));
                                    transaction.put("orderBuy", depth.getOrderId());
                                    transaction.put("orderSell", orderId);
                                    transaction.put("orderBuyStatus", 0);
                                    transaction.put("orderSellStatus", 1);
                                    kafkaTemplate.send("order-finish", transaction.toJSONString());
                                    num = 0;
                                } else {
                                    productDepth.get(productId + "Buy").poll();
                                    JSONObject delete = new JSONObject();
                                    delete.put("_id", depth.getId());
                                    depthDao.deleteOne(productId, delete);
                                    num -= depth.getNum();
                                    priceDeal = depth.getPrice();
                                    JSONObject transaction = new JSONObject();
                                    transaction.put("productId", productId);
                                    transaction.put("price", depth.getPrice());
                                    transaction.put("num", depth.getNum());
                                    transaction.put("traderBuy", depth.getTrader());
                                    transaction.put("traderSell", String.valueOf(order.get("trader")));
                                    transaction.put("brokerBuy", depth.getBroker());
                                    transaction.put("brokerSell", String.valueOf(order.get("broker")));
                                    transaction.put("orderBuy", depth.getOrderId());
                                    transaction.put("orderSell", orderId);
                                    transaction.put("orderBuyStatus", 1);
                                    int left = num > 0 ? 0 : 1;
                                    transaction.put("orderSellStatus", left);
                                    kafkaTemplate.send("order-finish", transaction.toJSONString());
                                }
                            } else {
                                order.put("time", time);
                                order.put("num", num);
                                order.put("orderId", orderId);
                                String id = depthDao.insertOne(productId, order);
                                order.put("id", id);
                                Depth depth1 = new Depth(order);
                                productDepth.get(productId + "Sell").add(depth1);
                                num = 0;
                            }
                        }
                    }
                }
                break;
            case "market":
                if(action.equals("buy")) {
                    Integer num = Integer.valueOf(String.valueOf(order.get("num")));
                    while(num > 0) {
                        Depth depth = (Depth) productDepth.get(productId + "Sell").peek();
                        if(depth == null) {
                            order.put("time", time);
                            order.put("num", num);
                            order.put("price", Double.MAX_VALUE);
                            order.put("orderId", orderId);
                            String id = depthDao.insertOne(productId, order);
                            order.put("id", id);
                            Depth depth1 = new Depth(order);
                            productDepth.get(productId + "Buy").add(depth1);
                            num = 0;
                        } else {
                            if(depth.getNum().compareTo(num) > 0) {
                                depth.setNum(depth.getNum() - num);
                                JSONObject update = new JSONObject();
                                update.put("_id", depth.getId());
                                update.put("num", depth.getNum());
                                depthDao.updateOne(productId, update);
                                priceDeal = depth.getPrice();
                                JSONObject transaction = new JSONObject();
                                transaction.put("productId", productId);
                                transaction.put("price", depth.getPrice());
                                transaction.put("num", num);
                                transaction.put("traderSell", depth.getTrader());
                                transaction.put("traderBuy", String.valueOf(order.get("trader")));
                                transaction.put("brokerSell", depth.getBroker());
                                transaction.put("brokerBuy", String.valueOf(order.get("broker")));
                                transaction.put("orderSell", depth.getOrderId());
                                transaction.put("orderBuy", orderId);
                                transaction.put("orderSellStatus", 0);
                                transaction.put("orderBuyStatus", 1);
                                kafkaTemplate.send("order-finish", transaction.toJSONString());
                                num = 0;
                            } else {
                                productDepth.get(productId + "Sell").poll();
                                JSONObject delete = new JSONObject();
                                delete.put("_id", depth.getId());
                                depthDao.deleteOne(productId, delete);
                                num -= depth.getNum();
                                priceDeal = depth.getPrice();
                                JSONObject transaction = new JSONObject();
                                transaction.put("productId", productId);
                                transaction.put("price", depth.getPrice());
                                transaction.put("num", depth.getNum());
                                transaction.put("traderSell", depth.getTrader());
                                transaction.put("traderBuy", String.valueOf(order.get("trader")));
                                transaction.put("brokerSell", depth.getBroker());
                                transaction.put("brokerBuy", String.valueOf(order.get("broker")));
                                transaction.put("orderSell", depth.getOrderId());
                                transaction.put("orderBuy", orderId);
                                transaction.put("orderSellStatus", 1);
                                int left = num > 0 ? 0 : 1;
                                transaction.put("orderBuyStatus", left);
                                kafkaTemplate.send("order-finish", transaction.toJSONString());
                            }
                        }
                    }
                } else {
                    Integer num = Integer.valueOf(String.valueOf(order.get("num")));
                    while(num > 0) {
                        Depth depth = (Depth) productDepth.get(productId + "Buy").peek();
                        if(depth == null) {
                            order.put("time", time);
                            order.put("num", num);
                            order.put("price", 0);
                            order.put("orderId", orderId);
                            String id = depthDao.insertOne(productId, order);
                            order.put("id", id);
                            Depth depth1 = new Depth(order);
                            productDepth.get(productId + "Sell").add(depth1);
                            num = 0;
                        } else {
                            if(depth.getNum().compareTo(num) > 0) {
                                depth.setNum(depth.getNum() - num);
                                JSONObject update = new JSONObject();
                                update.put("_id", depth.getId());
                                update.put("num", depth.getNum());
                                depthDao.updateOne(productId, update);
                                priceDeal = depth.getPrice();
                                JSONObject transaction = new JSONObject();
                                transaction.put("productId", productId);
                                transaction.put("price", depth.getPrice());
                                transaction.put("num", num);
                                transaction.put("traderBuy", depth.getTrader());
                                transaction.put("traderSell", String.valueOf(order.get("trader")));
                                transaction.put("brokerBuy", depth.getBroker());
                                transaction.put("brokerSell", String.valueOf(order.get("broker")));
                                transaction.put("orderBuy", depth.getOrderId());
                                transaction.put("orderSell", orderId);
                                transaction.put("orderBuyStatus", 0);
                                transaction.put("orderSellStatus", 1);
                                kafkaTemplate.send("order-finish", transaction.toJSONString());
                                num = 0;
                            } else {
                                productDepth.get(productId + "Buy").poll();
                                JSONObject delete = new JSONObject();
                                delete.put("_id", depth.getId());
                                depthDao.deleteOne(productId, delete);
                                num -= depth.getNum();
                                priceDeal = depth.getPrice();
                                JSONObject transaction = new JSONObject();
                                transaction.put("productId", productId);
                                transaction.put("price", depth.getPrice());
                                transaction.put("num", depth.getNum());
                                transaction.put("traderBuy", depth.getTrader());
                                transaction.put("traderSell", String.valueOf(order.get("trader")));
                                transaction.put("brokerBuy", depth.getBroker());
                                transaction.put("brokerSell", String.valueOf(order.get("broker")));
                                transaction.put("orderBuy", depth.getOrderId());
                                transaction.put("orderSell", orderId);
                                transaction.put("orderBuyStatus", 1);
                                int left = num > 0 ? 0 : 1;
                                transaction.put("orderSellStatus", left);
                                kafkaTemplate.send("order-finish", transaction.toJSONString());
                            }
                        }
                    }
                }
                break;
            case "cancel":
                JSONObject bson = new JSONObject();
                bson.put("orderId", order.get("orderId"));
                JSONArray array = depthDao.findByOrder(productId, bson);
                depthDao.deleteByOrder(productId, bson);
                JSONObject jsonObject = new JSONObject();
                if(array.size() > 0) {
                    jsonObject = (JSONObject) array.get(0);
                    jsonObject.put("status", 1);
                } else {
                    jsonObject.put("status", 0);
                }
                JSONObject cancel = new JSONObject();
                cancel.put("cancelOrderId", order.get("id"));
                cancel.put("message", jsonObject);
                kafkaTemplate.send("order-cancel", cancel.toJSONString());
                initFromDatabase(productId);
                break;
            case "stop":
                if(action.equals("buy")) {
                    JSONObject stopBuyOrder = new JSONObject();
                    stopBuyOrder.put("price", order.get("price"));
                    stopBuyOrder.put("action", "stopBuy");
                    stopBuyOrder.put("time", time);
                    stopBuyOrder.put("num", order.get("num"));
                    stopBuyOrder.put("trader", order.get("trader"));
                    stopBuyOrder.put("broker", order.get("broker"));
                    stopBuyOrder.put("orderId", orderId);
                    String id = depthDao.insertOne(productId, stopBuyOrder);
                    stopBuyOrder.put("id", id);
                    Depth stopBuy = new Depth(stopBuyOrder);
                    productDepth.get(productId + "stopBuy").add(stopBuy);
                } else {
                    JSONObject stopSellOrder = new JSONObject();
                    stopSellOrder.put("price", order.get("price"));
                    stopSellOrder.put("action", "stopSell");
                    stopSellOrder.put("time", time);
                    stopSellOrder.put("num", order.get("num"));
                    stopSellOrder.put("trader", order.get("trader"));
                    stopSellOrder.put("broker", order.get("broker"));
                    stopSellOrder.put("orderId", orderId);
                    String id = depthDao.insertOne(productId, stopSellOrder);
                    stopSellOrder.put("id", id);
                    Depth stopSell = new Depth(stopSellOrder);
                    productDepth.get(productId + "stopSell").add(stopSell);
                }
                break;
        }
        while(productDepth.get(productId + "stopBuy").peek() != null && productDepth.get(productId + "stopBuy").peek().getPrice() < priceDeal) {
            Depth stopBuy = productDepth.get(productId + "stopBuy").poll();
            Integer num = stopBuy.getNum();
            JSONObject de = new JSONObject();
            de.put("_id", stopBuy.getId());
            depthDao.deleteOne(productId, de);
            while(num > 0) {
                Depth depth = productDepth.get(productId + "Sell").peek();
                if(depth == null) {
                    JSONObject stopObject = new JSONObject();
                    stopObject.put("price", Double.MAX_VALUE);
                    stopObject.put("action", "buy");
                    stopObject.put("time", stopBuy.getTime());
                    stopObject.put("num", stopBuy.getNum());
                    stopObject.put("trader", stopBuy.getTrader());
                    stopObject.put("broker", stopBuy.getBroker());
                    stopObject.put("orderId", stopBuy.getOrderId());
                    String id = depthDao.insertOne(productId, stopObject);
                    stopObject.put("id", id);
                    Depth depth1 = new Depth(stopObject);
                    productDepth.get(productId + "Buy").add(depth1);
                    num = 0;
                } else {
                    if(depth.getNum().compareTo(num) > 0) {
                        depth.setNum(depth.getNum() - num);
                        JSONObject update = new JSONObject();
                        update.put("_id", depth.getId());
                        update.put("num", depth.getNum());
                        depthDao.updateOne(productId, update);
                        priceDeal = depth.getPrice();
                        JSONObject transaction = new JSONObject();
                        transaction.put("productId", productId);
                        transaction.put("price", depth.getPrice());
                        transaction.put("num", num);
                        transaction.put("traderSell", depth.getTrader());
                        transaction.put("traderBuy", stopBuy.getTrader());
                        transaction.put("brokerSell", depth.getBroker());
                        transaction.put("brokerBuy", stopBuy.getBroker());
                        transaction.put("orderSell", depth.getOrderId());
                        transaction.put("orderBuy", stopBuy.getOrderId());
                        transaction.put("orderSellStatus", 0);
                        transaction.put("orderBuyStatus", 1);
                        kafkaTemplate.send("order-finish", transaction.toJSONString());
                        num = 0;
                    } else {
                        productDepth.get(productId + "Sell").poll();
                        JSONObject delete = new JSONObject();
                        delete.put("_id", depth.getId());
                        depthDao.deleteOne(productId, delete);
                        num -= depth.getNum();
                        priceDeal = depth.getPrice();
                        JSONObject transaction = new JSONObject();
                        transaction.put("productId", productId);
                        transaction.put("price", depth.getPrice());
                        transaction.put("num", depth.getNum());
                        transaction.put("traderSell", depth.getTrader());
                        transaction.put("traderBuy", stopBuy.getTrader());
                        transaction.put("brokerSell", depth.getBroker());
                        transaction.put("brokerBuy", stopBuy.getBroker());
                        transaction.put("orderSell", depth.getOrderId());
                        transaction.put("orderBuy", stopBuy.getOrderId());
                        transaction.put("orderSellStatus", 1);
                        int left = num > 0 ? 0 : 1;
                        transaction.put("orderBuyStatus", left);
                        kafkaTemplate.send("order-finish", transaction.toJSONString());
                    }
                }
            }
        }
        while(productDepth.get(productId + "stopSell").peek() != null && productDepth.get(productId + "stopSell").peek().getPrice() > priceDeal) {
            Depth depthSell = productDepth.get(productId + "stopSell").poll();
            Integer num = depthSell.getNum();
            JSONObject de = new JSONObject();
            de.put("_id", depthSell.getId());
            depthDao.deleteOne(productId, de);
            while(num > 0) {
                Depth depth = productDepth.get(productId + "Buy").peek();
                if(depth == null) {
                    JSONObject stopObject = new JSONObject();
                    stopObject.put("price", 0);
                    stopObject.put("action", "sell");
                    stopObject.put("time", depthSell.getTime());
                    stopObject.put("num", depthSell.getNum());
                    stopObject.put("trader", depthSell.getTrader());
                    stopObject.put("broker", depthSell.getBroker());
                    stopObject.put("orderId", depthSell.getOrderId());
                    String id = depthDao.insertOne(productId, stopObject);
                    stopObject.put("id", id);
                    Depth depth1 = new Depth(stopObject);
                    productDepth.get(productId + "Sell").add(depth1);
                    num = 0;
                } else {
                    if(depth.getNum().compareTo(num) > 0) {
                        depth.setNum(depth.getNum() - num);
                        JSONObject update = new JSONObject();
                        update.put("_id", depth.getId());
                        update.put("num", depth.getNum());
                        depthDao.updateOne(productId, update);
                        priceDeal = depth.getPrice();
                        JSONObject transaction = new JSONObject();
                        transaction.put("productId", productId);
                        transaction.put("price", depth.getPrice());
                        transaction.put("num", num);
                        transaction.put("traderBuy", depth.getTrader());
                        transaction.put("traderSell", depthSell.getTrader());
                        transaction.put("brokerBuy", depth.getBroker());
                        transaction.put("brokerSell", depthSell.getBroker());
                        transaction.put("orderBuy", depth.getOrderId());
                        transaction.put("orderSell", depthSell.getOrderId());
                        transaction.put("orderBuyStatus", 0);
                        transaction.put("orderSellStatus", 1);
                        kafkaTemplate.send("order-finish", transaction.toJSONString());
                        num = 0;
                    } else {
                        productDepth.get(productId + "Buy").poll();
                        JSONObject delete = new JSONObject();
                        delete.put("_id", depth.getId());
                        depthDao.deleteOne(productId, delete);
                        num -= depth.getNum();
                        priceDeal = depth.getPrice();
                        JSONObject transaction = new JSONObject();
                        transaction.put("productId", productId);
                        transaction.put("price", depth.getPrice());
                        transaction.put("num", depth.getNum());
                        transaction.put("traderBuy", depth.getTrader());
                        transaction.put("traderSell", depthSell.getTrader());
                        transaction.put("brokerBuy", depth.getBroker());
                        transaction.put("brokerSell", depthSell.getBroker());
                        transaction.put("orderBuy", depth.getOrderId());
                        transaction.put("orderSell", depthSell.getOrderId());
                        transaction.put("orderBuyStatus", 1);
                        int left = num > 0 ? 0 : 1;
                        transaction.put("orderSellStatus", left);
                        kafkaTemplate.send("order-finish", transaction.toJSONString());
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
            PriorityBlockingQueue<Depth> depthStopBuys = new PriorityBlockingQueue<>();
            PriorityBlockingQueue<Depth> depthStopSells = new PriorityBlockingQueue<>();
            for (Object o : array) {
                JSONObject object = (JSONObject) o;
                Depth depth = new Depth(object);
                if (depth.getAction().equals("buy")) {
                    depthBuys.add(depth);
                } else if (depth.getAction().equals("sell")){
                    depthSells.add(depth);
                } else if (depth.getAction().equals("stopBuy")) {
                    depthStopBuys.add(depth);
                } else if (depth.getAction().equals("stopSell")) {
                    depthStopSells.add(depth);
                }
            }
            productDepth.put(productId + "Sell", depthSells);
            productDepth.put(productId + "Buy", depthBuys);
            productDepth.put(productId + "stopSell", depthStopSells);
            productDepth.put(productId + "stopBuy", depthStopBuys);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
