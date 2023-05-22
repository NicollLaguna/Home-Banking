package com.mindhub.brothers.Homebanking.dtos;

import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Set<TransactionDTO> transactions;

    private boolean active;//eliminar cuenta

    private AccountType accountType;//Tipo de cuenta

    public AccountDTO(Account account){
        this.id = account.getId();
        this.balance = account.getBalance();
        this.number = account.getNumber();
        this.creationDate= account.getCreationDate();
        this.transactions= account.getTransactions()
                .stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(Collectors.toSet());
        this.active= account.isActive();
        this.accountType= account.getAccountType();
    };

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }


    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public boolean isActive() {
        return active;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
