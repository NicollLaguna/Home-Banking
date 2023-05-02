package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.models.Transaction;
import com.mindhub.brothers.Homebanking.models.TransactionType;
import com.mindhub.brothers.Homebanking.repositories.AccountRepository;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import com.mindhub.brothers.Homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/transaction", method = RequestMethod.POST)
    public ResponseEntity<Object> newTransaction(Authentication authentication
            ,@RequestParam double amount,@RequestParam String description
            ,@RequestParam String account1,@RequestParam String account2){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(account1.toUpperCase());
        Account destinyAccount = accountRepository.findByNumber(account2.toUpperCase());

    if (amount<1 || description.isEmpty() || originAccount == null || destinyAccount == null){
        return  new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);//manejar error por individual
    }
    if (originAccount.equals(destinyAccount)){
        return new ResponseEntity<>("Accounts equals", HttpStatus.FORBIDDEN);
    }
    if (client.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(account1))
            .collect(toList()).size()==0){
        return new ResponseEntity<>("This account is not owned by you", HttpStatus.FORBIDDEN);
    }
    if (originAccount.getBalance() < amount){
        return new ResponseEntity<>("Insufficient money", HttpStatus.FORBIDDEN);
    }

    originAccount.setBalance(originAccount.getBalance()-amount);
    destinyAccount.setBalance(destinyAccount.getBalance()+amount);

        Transaction debitTransaction = new Transaction(amount, description, LocalDateTime.now(), TransactionType.DEBIT);
        originAccount.addTransaction(debitTransaction);
        transactionRepository.save(debitTransaction);

        Transaction creditTransaction = new Transaction(amount, description, LocalDateTime.now(),TransactionType.CREDIT);
        destinyAccount.addTransaction(creditTransaction);
        transactionRepository.save(creditTransaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
