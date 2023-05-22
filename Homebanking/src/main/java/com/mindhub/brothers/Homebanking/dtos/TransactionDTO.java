package com.mindhub.brothers.Homebanking.dtos;

import com.mindhub.brothers.Homebanking.models.Transaction;
import com.mindhub.brothers.Homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    private boolean active;//eliminar cuenta

    private double balance;//balance ajustable

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.active= transaction.isActive();
        this.balance=transaction.getBalanceTotal();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public boolean isActive() {
        return active;
    }

    public double getBalance() {
        return balance;
    }
}
