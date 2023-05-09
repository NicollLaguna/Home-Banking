package com.mindhub.brothers.Homebanking.dtos;

import com.mindhub.brothers.Homebanking.models.Loan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoanDTO {
    private long id;
    private String name;
    private double maxAmount;
    private Set<Integer> payments = new HashSet<>();


    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public Set<Integer> getPayments() {
        return payments;
    }
}
