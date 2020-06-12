package broker.websocket.message.encoder;

import broker.websocket.message.entity.OrderMessage;
import net.minidev.json.JSONObject;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class OrderMessageEncoder implements Encoder.Text<OrderMessage>{
    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }

    @Override
    public String encode(OrderMessage orderMessage) {
        JSONObject object = new JSONObject();
        object.put("orders", orderMessage.getOrders());
        object.put("transactions", orderMessage.getTransactions());
        return object.toJSONString();
    }
}
