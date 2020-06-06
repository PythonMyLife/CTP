package broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class BrokerGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrokerGatewayApplication.class, args);
    }
}
