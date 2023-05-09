package com.mindhub.brothers.Homebanking.services;

import com.mindhub.brothers.Homebanking.models.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
