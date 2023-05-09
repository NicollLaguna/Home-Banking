package com.mindhub.brothers.Homebanking.services.implement;

import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.models.ClientLoan;
import com.mindhub.brothers.Homebanking.models.Loan;
import com.mindhub.brothers.Homebanking.repositories.ClientLoanRepository;
import com.mindhub.brothers.Homebanking.repositories.LoanRepository;
import com.mindhub.brothers.Homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public ClientLoan findByLoanAndClient(Loan loan, Client client) {
        return clientLoanRepository.findByLoanAndClient(loan, client);
    }
}
