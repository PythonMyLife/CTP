package broker.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @KafkaListener(groupId = "broker-gateway1", topics = {"sun"})
    public void show(ConsumerRecord record) {
        System.out.println(record.value().toString());
    }
}
