package broker.controller;

import broker.service.ProductService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/broker")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public JSONObject addProduct(@RequestBody Map<String, String> map) {
        return productService.addProduct(map.get("name"), map.get("category"), map.get("period"));
    }
}
