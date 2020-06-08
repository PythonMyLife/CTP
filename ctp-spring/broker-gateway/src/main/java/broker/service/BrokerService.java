package broker.service;

import broker.dao.BrokerDao;
import broker.entity.Broker;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrokerService {
    @Autowired
    private BrokerDao brokerDao;

    public JSONObject register(String username, String password) {
        JSONObject object = new JSONObject();
        if(username == null || password == null) {
            object.put("status", 0);
            object.put("message", "用户名和密码不能为空！");
            return object;
        }
        if(brokerDao.existsByUsername(username)) {
            object.put("status", 0);
            object.put("message", "用户名已被占用！");
            return object;
        }
        Broker broker = new Broker(username, password);
        if(brokerDao.save(broker)) {
            object.put("status", 1);
            object.put("message", "注册成功！");
        } else {
            object.put("status", 0);
            object.put("message", "注册失败！");
        }
        return object;
    }

    public JSONObject findAll() {
        JSONObject object = new JSONObject();
        List<String> brokerStr = new ArrayList<>();
        List<Broker> brokers = brokerDao.findAll();
        for(Broker broker : brokers) {
            brokerStr.add(broker.getUsername());
        }
        object.put("message", brokerStr);
        return object;
    }
}
