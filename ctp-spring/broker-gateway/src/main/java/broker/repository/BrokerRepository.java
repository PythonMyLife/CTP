package broker.repository;

import broker.entity.Broker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrokerRepository extends MongoRepository<Broker, String> {
    Boolean existsByUsername(String username);
    Broker findByUsername(String username);
}
