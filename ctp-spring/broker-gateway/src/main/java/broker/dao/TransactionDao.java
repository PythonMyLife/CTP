package broker.dao;

import broker.entity.Transaction;
import broker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionDao {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findAllByProductId(String productId) {
        return transactionRepository.findAllByProduct(productId);
    }

    public List<Transaction> findAllByTraderSellOrTraderBuy(String username) {
        return transactionRepository.findAllByTraderbuyOrTradersell(username, username);
    }
}
