package broker.repository;

import broker.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByProduct(String product);
    List<Transaction> findAllByTraderbuyOrTradersell(String traderbuy, String tradersell);
}
