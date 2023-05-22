package com.mindhub.brothers.Homebanking.Controllers;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientServices clientServices;

    @RequestMapping("/clients/current")
    public ClientDTO getClientAuthentication(Authentication authentication) {
        return clientServices.getClientAuthentication(authentication);
    }
    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientServices.getClients();
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientServices.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Random randomN = new Random();
        int min = 0;
        int max = 99999999;
        int number = randomN.nextInt((max-min)+1)+min;

        if (accountService.findByNumber("VIN-"+number)==null){
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientServices.saveClient(newClient);
        Account defectAccount = new Account(0.00,"VIN"+number, LocalDateTime.now(), true, AccountType.SAVINGS);
        newClient.addAccount(defectAccount);
        accountService.saveAccount(defectAccount);}
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
