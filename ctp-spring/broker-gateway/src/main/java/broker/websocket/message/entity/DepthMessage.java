package broker.websocket.message.entity;

import net.minidev.json.JSONObject;

public class DepthMessage extends Message{
    private JSONObject depth;

    public DepthMessage(JSONObject depth) {
        this.depth = depth;
    }

    public JSONObject getDepth() {
        return depth;
    }

    public void setDepth(JSONObject depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "DepthMessage{" +
                "depth=" + depth.toJSONString() +
                '}';
    }
}
