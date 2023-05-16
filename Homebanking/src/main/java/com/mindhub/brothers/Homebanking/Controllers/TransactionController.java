package com.mindhub.brothers.Homebanking.Controllers;

import com.itextpdf.text.DocumentException;
import com.mindhub.brothers.Homebanking.models.*;
import com.mindhub.brothers.Homebanking.services.AccountService;
import com.mindhub.brothers.Homebanking.services.ClientServices;
import com.mindhub.brothers.Homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    AccountService accountService;
    @Autowired
    ClientServices clientServices;
    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> newTransaction(Authentication authentication
            ,@RequestParam double amount,@RequestParam String description
            ,@RequestParam String account1,@RequestParam String account2){
        Client client = clientServices.findByEmail(authentication.getName());
        Account originAccount = accountService.findByNumber(account1.toUpperCase());
        Account destinyAccount = accountService.findByNumber(account2.toUpperCase());

    if (amount<1){
        return  new ResponseEntity<>("Amount is necessary", HttpStatus.FORBIDDEN);
    }
    if(description.isBlank()){
        return  new ResponseEntity<>("Description is necessary", HttpStatus.FORBIDDEN);
    }
    if(originAccount == null) {
        return  new ResponseEntity<>("Origin account is necessary", HttpStatus.FORBIDDEN);
    }
    if(destinyAccount == null){
        return  new ResponseEntity<>("Destiny account is necessary", HttpStatus.FORBIDDEN);
    }
    if (originAccount.equals(destinyAccount)){
        return new ResponseEntity<>("Accounts equals", HttpStatus.FORBIDDEN);
    }
    if (client.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(account1))
            .collect(toList()).size()==0){
        return new ResponseEntity<>("This account is not owned by you", HttpStatus.FORBIDDEN);
    }
    if (originAccount.getBalance() < amount){
        return new ResponseEntity<>("Insufficient money", HttpStatus.FORBIDDEN);
    }

    originAccount.setBalance(originAccount.getBalance()-amount);
    destinyAccount.setBalance(destinyAccount.getBalance()+amount);

        Transaction debitTransaction = new Transaction(amount, description, LocalDateTime.now(), TransactionType.DEBIT);
        originAccount.addTransaction(debitTransaction);
        transactionService.saveTransaction(debitTransaction);

        Transaction creditTransaction = new Transaction(amount, description, LocalDateTime.now(),TransactionType.CREDIT);
        destinyAccount.addTransaction(creditTransaction);
        transactionService.saveTransaction(creditTransaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Descargar estado de cuenta--Inicio--

    @PostMapping("client/current/account_status")
    public ResponseEntity<Object> accountStatusPDF(HttpServletResponse response, Authentication authentication,
    @RequestParam String accNumber, @RequestParam String dateStart, @RequestParam String dateEnd) throws DocumentException, IOException{
        Client client = clientServices.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(accNumber);

        if (client == null){
            return new ResponseEntity<>("This user is not a Client", HttpStatus.FORBIDDEN);
        }

        if (account == null){
            return new ResponseEntity<>("Invalid account number", HttpStatus.FORBIDDEN);
        }

        if (client.getAccounts().stream().noneMatch(account1 -> account1.getNumber().equals(account.getNumber()))){
            return new ResponseEntity<>("This account is not owned by you", HttpStatus.FORBIDDEN);
        }

        if (dateStart.isBlank()){
            return new ResponseEntity<>("Start date is necessary", HttpStatus.FORBIDDEN);
        }

        if (dateEnd.isBlank()){
            return new ResponseEntity<>("End date is necessary", HttpStatus.FORBIDDEN);
        }

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename= Transactions" + accNumber + ".pdf";
        response.setHeader(headerKey, headerValue);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTimeStart = LocalDateTime.parse(dateStart, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(dateEnd, formatter);

        List<Transaction> listTransactions = transactionService.findBetween(client, accNumber, dateTimeStart, dateTimeEnd);

        TransactionPDF  transactionPDF = new TransactionPDF(listTransactions, account);
        transactionPDF.usePDFExport(response);

        return new ResponseEntity<>("ACCOUNT STATUS WAS CREATED", HttpStatus.CREATED);
    }
    //Descargar estado de cuenta--Fin--
}
