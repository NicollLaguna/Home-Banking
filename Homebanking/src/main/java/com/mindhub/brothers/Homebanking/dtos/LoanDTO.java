package com.mindhub.brothers.Homebanking.dtos;

import com.mindhub.brothers.Homebanking.models.Loan;

public class LoanDTO {

    private int payments;

    public LoanDTO(){

    }

    public LoanDTO(int payments) {
        this.payments = payments;
    }

    public LoanDTO(Loan loan) {
    }

    public int getPayments() {
        return payments;
    }
}
