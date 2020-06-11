package dealer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class BrokerDealerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrokerDealerApplication.class, args);
    }
}
