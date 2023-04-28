package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.dtos.AccountDTO;
import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.repositories.AccountRepository;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/clients/current", method = RequestMethod.GET)
    public ClientDTO getClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        return accountRepository.findAll()
                .stream().map(account -> new AccountDTO(account))
                .collect(Collectors.toList());
    }

    @RequestMapping("/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.map(account ->  new AccountDTO(account)).orElse(null);
    }

   @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount (Authentication authentication){
       Random randomN = new Random();
       int min = 0;
       int max = 99999999;
       int number = randomN.nextInt((max-min)+1)+min;

        if (clientRepository.findByEmail(authentication.getName()).getAccounts().size()<=2){
            Account accountCreated = new Account(0.00,"VIN"+number, LocalDateTime.now());
            clientRepository.findByEmail(authentication.getName()).addAccount(accountCreated);
            accountRepository.save(accountCreated);
        }else{
            return new ResponseEntity<>("There are 3 accounts created", HttpStatus.FORBIDDEN);
        }

       if (accountRepository.findByNumber("VIN"+number) != null){
           return  new ResponseEntity<>("Number cannot be use", HttpStatus.FORBIDDEN);
       }
        return new ResponseEntity<>(HttpStatus.CREATED);
   }
}
