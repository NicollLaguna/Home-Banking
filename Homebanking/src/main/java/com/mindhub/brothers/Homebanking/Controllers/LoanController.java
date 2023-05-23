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
    double totalAmount =0;


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

    //Porcentaje ajustable de prestamos
    if(loanApplicationDTO.getId()==1){
        totalAmount= loanApplicationDTO.getAmount()*1.50;
    }

    if (loanApplicationDTO.getId()==2){
        totalAmount= loanApplicationDTO.getAmount()*1.20;
    }

    if(loanApplicationDTO.getId()==3){
        totalAmount=loanApplicationDTO.getAmount()*1.30;
    }

    ClientLoan clientLoan = new ClientLoan(totalAmount,payments,loan.getName());
    clientLoan.setClient(client);
    clientLoan.setLoan(loan);
    clientLoanService.saveClientLoan(clientLoan);

    //Balance ajustable
    double initialBalance = account.getBalance()+loanApplicationDTO.getAmount();
    double balance = account.getBalance();
    double newBalance = balance + totalAmount;
    Transaction creditTloan = new Transaction(totalAmount,loan.getName()+" loan approved", LocalDateTime.now(),TransactionType.CREDIT, true,newBalance);
    account.addTransaction(creditTloan);
    transactionRepository.save(creditTloan);
    account.setBalance(newBalance);
    accountService.saveAccount(account);

    return new ResponseEntity<>("Loan apply created",HttpStatus.CREATED);
}

 @PostMapping("/loans/admin-loan")
    public ResponseEntity<Object> newLoanAdmin(@RequestBody Loan loan){
        if(loan.getName().isBlank()){
            return new ResponseEntity<>("Loan name is necessary",HttpStatus.FORBIDDEN);
        }
        if(loan.getMaxAmount()<1){
            return new ResponseEntity<>("Max amount  must be greater than 1 ",HttpStatus.FORBIDDEN);
        }

        if(loan.getPayments().size()==0){
            return new ResponseEntity<>("Insert valid payments", HttpStatus.FORBIDDEN);
        }

        for(LoanDTO loans: loanService.getLoans()){
            if (loan.getName().equalsIgnoreCase(loans.getName())){
                return new ResponseEntity<>("This type of loan "+loan.getName()+" is already used",HttpStatus.FORBIDDEN);
            }
        };

        Loan newLoan = new Loan(loan.getName(), loan.getMaxAmount(),loan.getPayments());
        loanService.saveLoan(newLoan);

        return new ResponseEntity<>("A new type of loan was created", HttpStatus.CREATED);

 }
}
