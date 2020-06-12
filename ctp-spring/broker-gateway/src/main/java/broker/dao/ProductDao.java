package broker.dao;

import broker.entity.Product;
import broker.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao {
    @Autowired
    private ProductRepository productRepository;

    public Boolean existById(String id) {
        return productRepository.existsById(id);
    }

    public Boolean existsByNameAndCategoryAndPeriod(String name, String category, String period) {
        return productRepository.existsByNameAndCategoryAndPeriod(name, category, period);
    }

    public Product save(Product product) {
        try{
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
