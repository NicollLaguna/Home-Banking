package com.mindhub.brothers.Homebanking.services;

import com.mindhub.brothers.Homebanking.dtos.LoanDTO;
import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<LoanDTO> getLoans();
    Loan findById(long id);
    void saveLoan(Loan loan);
}
