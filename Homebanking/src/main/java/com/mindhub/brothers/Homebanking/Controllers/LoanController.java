package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.dtos.ClientLoanDTO;
import com.mindhub.brothers.Homebanking.dtos.LoanApplicationDTO;
import com.mindhub.brothers.Homebanking.dtos.LoanDTO;
import com.mindhub.brothers.Homebanking.models.*;
import com.mindhub.brothers.Homebanking.repositories.*;
import com.mindhub.brothers.Homebanking.services.AccountService;
import com.mindhub.brothers.Homebanking.services.ClientLoanService;
import com.mindhub.brothers.Homebanking.services.ClientServices;
import com.mindhub.brothers.Homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanService loanService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientLoanService clientLoanService;

    @Autowired
    ClientServices clientServices;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }
@Transactional
@PostMapping("/loans")
public ResponseEntity<Object> newLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
    double amount = loanApplicationDTO.getAmount();
    int payments = loanApplicationDTO.getPayments();
    Loan loan = this.loanService.findById(loanApplicationDTO.getId());
    Account account= accountService.findByNumber(loanApplicationDTO.getAccountNumber());
    Client client = this.clientServices.findByEmail(authentication.getName());

//Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0.
    if (amount == 0 || payments == 0){
        return new ResponseEntity<>("Invalid loan application data", HttpStatus.FORBIDDEN);
    }

    //Verificar que el préstamo exista
    if (loan == null){
        return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
    }

    //Verificar que el monto solicitado no exceda el monto máximo del préstamo
    if (amount > loan.getMaxAmount()){
        return new ResponseEntity<>("Loan amount exceeds maximum amount", HttpStatus.FORBIDDEN);
    }

    //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
    if (!loan.getPayments().contains(payments)){
        return new ResponseEntity<>("Invalid payment",HttpStatus.FORBIDDEN);
    }

    //Verificar que la cuenta de destino exista

    if (account == null){
        return  new ResponseEntity<>("Account not found",HttpStatus.FORBIDDEN);
    }

    //Verificar que la cuenta de destino pertenezca al cliente autenticado
    String authenticatedUsername = authentication.getName();
    if (!account.getClientId().equals(client)){
        return new ResponseEntity<>("Destination account does not belong to authenticated user",HttpStatus.FORBIDDEN);
    }

    ClientLoan existingClientLoan =clientLoanService.findByLoanAndClient(loan,client);
    if (existingClientLoan != null){
        return new ResponseEntity<>("Loan Application already exists",HttpStatus.FORBIDDEN);
    }

    //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
    int totalAmount = (int)(amount*1.20);
    ClientLoan clientLoan = new ClientLoan(totalAmount,payments,loan.getName());
    clientLoan.setClient(client);
    clientLoan.setLoan(loan);
    clientLoanService.saveClientLoan(clientLoan);


    //Se debe crear una transacción “CREDIT” asociada a la cuenta de destino
    // (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
    Transaction creditTloan = new Transaction(amount,loan.getName()+" loan approved", LocalDateTime.now(),TransactionType.CREDIT, true);
    account.addTransaction(creditTloan);
    transactionRepository.save(creditTloan);

    //Se debe actualizar la cuenta de destino sumando el monto solicitado.
    double balance = account.getBalance();
    double newBalance = balance + amount;
    account.setBalance(newBalance);
    accountService.saveAccount(account);

    return new ResponseEntity<>("Loan apply created",HttpStatus.CREATED);
}
}
