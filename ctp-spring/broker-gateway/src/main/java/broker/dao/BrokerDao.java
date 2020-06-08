package broker.dao;

import broker.entity.Broker;
import broker.repository.BrokerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrokerDao {
    @Autowired
    private BrokerRepository brokerRepository;

    public Boolean existsByUsername(String username) {
        return brokerRepository.existsByUsername(username);
    }

    public Boolean save(Broker broker) {
        try{
            brokerRepository.save(broker);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Broker findByUsername(String username) {
        return brokerRepository.findByUsername(username);
    }

    public List<Broker> findAll() {
        return brokerRepository.findAll();
    }
}
