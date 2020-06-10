package broker.websocket;

import broker.dao.DepthDao;
import broker.websocket.message.decoder.DepthDecoder;
import broker.websocket.message.encoder.DepthMessageEncoder;
import broker.websocket.message.entity.DepthJoinMessage;
import broker.websocket.message.entity.DepthMessage;
import broker.websocket.message.entity.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@ServerEndpoint(
        value = "/websocket/api/broker/depth/{productId}",
        decoders = {
                DepthDecoder.class
        },
        encoders = {
                DepthMessageEncoder.class
        }
)
public class DepthServer {
    private static Logger logger = Logger.getLogger("websocket");
    private static final Map<String, Set<Session>> products = new ConcurrentHashMap<>();

    public static DepthDao depthDao;

//    private DepthDao depthDao = (DepthDao) ApplicationHelper.getBean("depthDao");

    @OnOpen
    public void connect(@PathParam("productId") String productId, Session session) throws Exception {
        // 将session按照房间名来存储，将各个房间的用户隔离
        if (!products.containsKey(productId)) {
            // 创建房间不存在时，创建房间
            Set<Session> product = new HashSet<>();
            // 添加用户
            product.add(session);
            products.put(productId, product);
        } else {
            // 房间已存在，直接添加用户到相应的房间
            products.get(productId).add(session);
        }
        logger.log(Level.INFO, "Connection opened.");
    }

    @OnClose
    public void disConnect(@PathParam("productId") String productId, Session session) {
        products.get(productId).remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }

    @OnMessage
    public void receiveMsg(@PathParam("productId") String productId,
                           Message msg, Session session) throws Exception {
        if(msg instanceof DepthJoinMessage) {
            // send depth message
            String depth = depthDao.findAll(productId);
            session.getBasicRemote().sendText(depth);
//            session.getBasicRemote().sendText("hi");
        }
    }

    @OnError
    public void error(Session session, Throwable t) {
        logger.log(Level.INFO, "Connection error ({0})", t.toString());
    }

    public void update(String productId, String message) throws IOException {
        for(Session session : products.get(productId)) {
            session.getBasicRemote().sendText(message);
        }
    }
 }
