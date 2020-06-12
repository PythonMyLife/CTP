package broker.websocket;

import broker.dao.DepthDao;
import broker.dao.OrderDao;
import broker.dao.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setDepthDao(DepthDao depthDao) {
        DepthServer.depthDao = depthDao;
    }

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        OrderServer.orderDao = orderDao;
    }

    @Autowired
    public void setTransactionDao(TransactionDao transactionDao) {
        OrderServer.transactionDao = transactionDao;
    }
}
