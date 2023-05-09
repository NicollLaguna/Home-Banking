package com.mindhub.brothers.Homebanking.services;

import com.mindhub.brothers.Homebanking.dtos.AccountDTO;
import com.mindhub.brothers.Homebanking.models.Account;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts(Authentication authentication);
    AccountDTO getAccount(Long id);
    void saveAccount(Account account);

    Account findByNumber (String number);
}
