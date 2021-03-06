package broker.websocket.message.decoder;

import broker.websocket.message.entity.DepthJoinMessage;
import broker.websocket.message.entity.Message;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DepthDecoder implements Decoder.Text<Message> {
    private Map<String, String> messageMap;

    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }

    @Override
    public Message decode(String string) throws DecodeException {
        Message msg = null;
        if(willDecode(string)) {
            switch (messageMap.get("type")) {
                case "join":
                    msg = new DepthJoinMessage(messageMap.get("username"),
                            messageMap.get("productId"));
                    break;
            }
        } else {
            throw new DecodeException(string, "[Message] Can't decode.");
        }
        return msg;
    }

    @Override
    public boolean willDecode(String string) {
        boolean decodes = false;
        messageMap = new HashMap<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(string);
            for(String key : object.keySet()) {
                messageMap.put(key, object.get(key).toString());
            }
            Set keys = messageMap.keySet();
            if(keys.contains("type")) {
                switch (messageMap.get("type")) {
                    case "join":
                        if (keys.contains("username") && keys.contains("productId"))
                            decodes = true;
                        break;
                }
            }
            return decodes;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
