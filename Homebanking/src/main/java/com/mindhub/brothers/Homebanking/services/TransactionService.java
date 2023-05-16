package com.mindhub.brothers.Homebanking.services;

import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);

    //Descargar estado de cuenta
    List<Transaction>findBetween(Client client, String string, LocalDateTime date, LocalDateTime date2);

}
