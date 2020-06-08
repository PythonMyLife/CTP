package broker.dao;

import broker.entity.Broker;
import broker.entity.Order;
import broker.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao {
    @Autowired
    private OrderRepository orderRepository;

    public Order save(Order order) {
        try{
            return orderRepository.save(order);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Order> findAllByStatus(Integer status) {
        return orderRepository.findAllByStatus(status);
    }


}
