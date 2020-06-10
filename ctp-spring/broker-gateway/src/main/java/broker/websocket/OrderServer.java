package broker.websocket;

import broker.dao.OrderDao;
import broker.dao.TransactionDao;
import broker.entity.Order;
import broker.entity.Transaction;
import broker.websocket.message.decoder.OrderDecoder;
import broker.websocket.message.encoder.OrderMessageEncoder;
import broker.websocket.message.entity.Message;
import broker.websocket.message.entity.OrderJoinMessage;
import broker.websocket.message.entity.OrderMessage;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@ServerEndpoint(
        value = "/websocket/api/broker/orders",
        decoders = {
                OrderDecoder.class
        },
        encoders = {
                OrderMessageEncoder.class
        }
)
public class OrderServer {
    private static Logger logger = Logger.getLogger("websocket");
    private static final List<Session> brokers = new CopyOnWriteArrayList<>();


    public static OrderDao orderDao;


    public static TransactionDao transactionDao;

    @OnOpen
    public void openConnection(Session session) {
        logger.log(Level.INFO, "Connection opened.");
    }

    @OnMessage
    public void message(final Session session, Message msg) throws IOException, EncodeException {
        if(msg instanceof OrderJoinMessage) {
            /* add the new admin */
            OrderJoinMessage omsg = (OrderJoinMessage) msg;
            session.getUserProperties().put("name", omsg.getUsername());
            session.getUserProperties().put("active", true);
            brokers.add(session);
            logger.log(Level.INFO, "Received: {0}", omsg.toString());

            // send message
            OrderMessage orderMessage = read();
            session.getBasicRemote().sendObject(orderMessage);

        }
    }

    @OnClose
    public void closedConnection(Session session) {
        /* Notify everybody */
        session.getUserProperties().put("active", false);
        brokers.remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {
        logger.log(Level.INFO, "Connection error ({0})", t.toString());
    }



    /* Forward a message to all connected clients
     * The endpoint figures what encoder to use based on the message type */
    public synchronized void sendAll() {
        OrderMessage msg = read();
        try {
            for (Session s : brokers) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendObject(msg);
                }
            }
            logger.log(Level.INFO, "Sent: {0}", msg.toString());
        } catch (IOException | EncodeException e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    private OrderMessage read() {
        JSONArray orders = new JSONArray();
        JSONArray transactions = new JSONArray();
        List<Order> orderList = orderDao.findAllByStatus(0);
        for(Order order : orderList) {
            JSONObject object = new JSONObject();
            object.put("id", order.getId());
            object.put("type", order.getType());
            object.put("time", order.getTime());
            object.put("value", order.getObject());
            orders.add(object);
        }
        List<Transaction> transactionList = transactionDao.findAll();
        for(Transaction transaction : transactionList) {
            JSONObject object = new JSONObject();
            object.put("id", transaction.getId());
            object.put("productId", transaction.getProduct());
            object.put("time", transaction.getTime());
            object.put("price", transaction.getPrice());
            object.put("num", transaction.getNum());
            object.put("traderSell", transaction.getTradersell());
            object.put("traderBuy", transaction.getTraderbuy());
            object.put("brokerSell", transaction.getBrokersell());
            object.put("brokerBuy", transaction.getBrokerbuy());
            transactions.add(object);
        }
        return new OrderMessage(orders, transactions);
    }
}
