package broker.websocket.message.encoder;

import broker.websocket.message.entity.DepthMessage;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class DepthMessageEncoder implements Encoder.Text<DepthMessage> {
    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }

    @Override
    public String encode(DepthMessage depthMessage) {
        return depthMessage.getDepth().toJSONString();
    }
}
