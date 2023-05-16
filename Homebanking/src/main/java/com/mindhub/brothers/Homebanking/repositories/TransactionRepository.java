package com.mindhub.brothers.Homebanking.repositories;

import com.mindhub.brothers.Homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.NamedQuery;
import java.time.LocalDateTime;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
