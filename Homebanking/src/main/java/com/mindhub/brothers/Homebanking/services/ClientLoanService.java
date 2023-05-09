package com.mindhub.brothers.Homebanking.services;

import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.models.ClientLoan;
import com.mindhub.brothers.Homebanking.models.Loan;

public interface ClientLoanService {
    void saveClientLoan(ClientLoan clientLoan);

    ClientLoan findByLoanAndClient(Loan loan, Client client);
}
