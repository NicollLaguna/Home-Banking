package com.mindhub.brothers.Homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private double amount;
    private int payments;

    private String accountNumber;

    public LoanApplicationDTO(){

    }
    public LoanApplicationDTO( long id,double amount, int payments, String number) {
        this.amount = amount;
        this.payments = payments;
        this.accountNumber = number;
        this.id= id;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
