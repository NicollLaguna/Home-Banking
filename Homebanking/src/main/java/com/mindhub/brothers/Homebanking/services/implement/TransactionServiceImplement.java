package com.mindhub.brothers.Homebanking.services.implement;

import com.mindhub.brothers.Homebanking.models.Transaction;
import com.mindhub.brothers.Homebanking.repositories.TransactionRepository;
import com.mindhub.brothers.Homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
