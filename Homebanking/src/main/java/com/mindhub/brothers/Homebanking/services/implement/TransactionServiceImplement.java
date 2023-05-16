package com.mindhub.brothers.Homebanking.services.implement;

import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.models.Transaction;
import com.mindhub.brothers.Homebanking.repositories.TransactionRepository;
import com.mindhub.brothers.Homebanking.services.AccountService;
import com.mindhub.brothers.Homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountService accountService;
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    //Descargar estado de cuenta
    @Override
    public List<Transaction> findBetween(Client client, String string, LocalDateTime date, LocalDateTime date2) {
        Account account= accountService.findByNumber(string);
        List<Transaction> list = new ArrayList<>();
        for (Transaction transaction: account.getTransactions()){
           if (transaction.getDate().isAfter(date) && transaction.getDate().isBefore(date2)){
               list.add(transaction);
           }
        }
        return list;
    }


}
