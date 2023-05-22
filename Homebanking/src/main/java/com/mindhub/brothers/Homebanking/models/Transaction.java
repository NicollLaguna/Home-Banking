package com.mindhub.brothers.Homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private TransactionType type;

    private double amount;
    private  String description;
    private LocalDateTime date;

    private boolean active; //eliminar cuenta
    private double balanceTotal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Account_id")
    private Account account;

    public Transaction(){}

    public Transaction(double mount,String describe, LocalDateTime dat, TransactionType type1, boolean active, double balanceTotal){
        this.type = type1;
        this.amount = mount;
        this.description = describe;
        this.date = dat;
        this.active=active;
        this.balanceTotal=balanceTotal;

    }
    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount, TransactionType type) {this.amount = amount;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(double balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    @JsonIgnore
    public Account getAccountId() {
        return account;
    }

    public void setAccountId(Account accountId) {
        this.account = accountId;
    }
}
