package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.dtos.AccountDTO;
import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.AccountType;
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

import static java.util.stream.Collectors.toList;

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
    public ResponseEntity<Object> newAccount (Authentication authentication, @RequestParam String accountType){
       Random randomN = new Random();
       int min = 0;
       int max = 99999999;
       int number = randomN.nextInt((max-min)+1)+min;

       if (!accountType.equalsIgnoreCase("SAVINGS")&&!accountType.equalsIgnoreCase("CURRENT")){
           return new ResponseEntity<>("The type of account is required ",HttpStatus.FORBIDDEN);
       }
       if (clientServices.findByEmail(authentication.getName()).getAccounts().size()<=2){
            Account accountCreated = new Account(0.00,"VIN"+number, LocalDateTime.now(),true, AccountType.valueOf(accountType.toUpperCase()));
            clientServices.findByEmail(authentication.getName()).addAccount(accountCreated);
            accountService.saveAccount(accountCreated);
       }else{
            return new ResponseEntity<>("There are 3 accounts created", HttpStatus.FORBIDDEN);
        }

       if (accountService.findByNumber("VIN"+number) == null){
           return  new ResponseEntity<>("Number cannot be use", HttpStatus.FORBIDDEN);
       }
        return new ResponseEntity<>(HttpStatus.CREATED);
   }

    @PutMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAccount (Authentication authentication, @PathVariable Long id){
        Client client= clientServices.findByEmail(authentication.getName());
        Account account=accountService.findById(id);

        if(account == null){
            return new ResponseEntity<>("This account not exist", HttpStatus.FORBIDDEN);
        }

        if (!account.isActive()){
            return new ResponseEntity<>("This account is inactive", HttpStatus.FORBIDDEN);
        }

        if(account.getBalance()>0){
            return new ResponseEntity<>("This account has money, it cannot be deleted",HttpStatus.FORBIDDEN);
        }

        if(client==null){
            return new ResponseEntity<>("This user isn't client",HttpStatus.FORBIDDEN);
        }

        if (client.getAccounts().stream().filter(account1 -> account1.getId()==id).collect(toList()).size()==0){
            return new ResponseEntity<>("This account is not owned by you", HttpStatus.FORBIDDEN);
        }

        account.setActive(false);
        account.getTransactions().stream().forEach(transaction -> transaction.setActive(false));
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account deleted",HttpStatus.ACCEPTED);
    }
}
