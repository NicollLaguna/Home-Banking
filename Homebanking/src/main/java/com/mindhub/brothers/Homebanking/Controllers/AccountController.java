package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.dtos.AccountDTO;
import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.repositories.AccountRepository;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import com.mindhub.brothers.Homebanking.services.AccountService;
import com.mindhub.brothers.Homebanking.services.ClientServices;
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
    private AccountService accountService;

    @Autowired
    private ClientServices clientServices;

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return clientServices.getClientAuthentication(authentication);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        return accountService.getAccounts(authentication);
    }

    @GetMapping("/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccount(id);
    }

   @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount (Authentication authentication){
       Random randomN = new Random();
       int min = 0;
       int max = 99999999;
       int number = randomN.nextInt((max-min)+1)+min;

        if (clientServices.findByEmail(authentication.getName()).getAccounts().size()<=2){
            Account accountCreated = new Account(0.00,"VIN"+number, LocalDateTime.now());
            clientServices.findByEmail(authentication.getName()).addAccount(accountCreated);
            accountService.saveAccount(accountCreated);
        }else{
            return new ResponseEntity<>("There are 3 accounts created", HttpStatus.FORBIDDEN);
        }

       if (accountService.findByNumber("VIN"+number) != null){
           return  new ResponseEntity<>("Number cannot be use", HttpStatus.FORBIDDEN);
       }
        return new ResponseEntity<>(HttpStatus.CREATED);
   }
}
