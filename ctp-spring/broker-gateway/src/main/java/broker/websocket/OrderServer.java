package broker.websocket;

import broker.websocket.message.decoder.OrderDecoder;
import broker.websocket.message.encoder.OrderMessageEncoder;
import broker.websocket.message.entity.Message;
import broker.websocket.message.entity.OrderJoinMessage;
import org.springframework.stereotype.Component;

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

    @OnOpen
    public void openConnection(Session session) {
        logger.log(Level.INFO, "Connection opened.");
    }

    @OnMessage
    public void message(final Session session, Message msg) {
        if(msg instanceof OrderJoinMessage) {
            /* add the new admin */
            OrderJoinMessage omsg = (OrderJoinMessage) msg;
            session.getUserProperties().put("name", omsg.getUsername());
            session.getUserProperties().put("active", true);
            brokers.add(session);
            logger.log(Level.INFO, "Received: {0}", omsg.toString());

            // send message
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
    public synchronized void sendAll(Object msg) {
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
}
