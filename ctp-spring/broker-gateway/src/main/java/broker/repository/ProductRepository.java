package broker.repository;

import broker.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Boolean existsByNameAndCategoryAndPeriod(String name, String category, String period);
}
