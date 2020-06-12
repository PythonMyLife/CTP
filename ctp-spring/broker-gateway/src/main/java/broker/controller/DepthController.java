package broker.controller;

import broker.dao.DepthDao;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/broker")
public class DepthController {
    @Autowired
    private DepthDao depthDao;

    @GetMapping("/depth")
    public String findAllBrokers(String productId) {
        try {
            return depthDao.findAll(productId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
