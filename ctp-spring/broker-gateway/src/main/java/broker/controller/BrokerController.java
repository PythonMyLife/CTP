package broker.controller;

import broker.service.BrokerService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/broker")
public class BrokerController {
    @Autowired
    BrokerService brokerService;

    @GetMapping("/brokers")
    public JSONObject findAllBrokers() {
        return brokerService.findAll();
    }

    @PostMapping("/register")
    public JSONObject register(@RequestBody Map<String, String> map) {
        return brokerService.register(map.get("username"), map.get("password"));
    }

}
