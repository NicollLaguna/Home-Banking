package com.mindhub.brothers.Homebanking.repositories;

import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.models.ClientLoan;
import com.mindhub.brothers.Homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
    ClientLoan findByLoanAndClient (Loan loan, Client client);
}
