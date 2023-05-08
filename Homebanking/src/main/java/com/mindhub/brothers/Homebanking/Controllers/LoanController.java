package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.dtos.LoanApplicationDTO;
import com.mindhub.brothers.Homebanking.dtos.LoanDTO;
import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.Loan;
import com.mindhub.brothers.Homebanking.repositories.AccountRepository;
import com.mindhub.brothers.Homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getLoans(){
        List<LoanDTO> loans = loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
        return ResponseEntity.ok(loans);
    }
@Transactional
@RequestMapping(path = "/loans", method = RequestMethod.POST)
public ResponseEntity<String> newLoan(@RequestBody LoanApplicationDTO LoanApplication, Authentication authentication){
    String accountNumber = LoanApplication.getAccountNumber();
    double amount = LoanApplication.getAmount();
    int payments = LoanApplication.getPayments();
    long loanId= LoanApplication.getId();
//Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0.
    if (amount == 0 || payments == 0){
        return new ResponseEntity<>("Invalid loan application data", HttpStatus.FORBIDDEN);
    }

    //Verificar que el préstamo exista
    Loan loan = loanRepository.getReferenceById(loanId);
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
    Account account= accountRepository.findByNumber(accountNumber);
    if (account == null){
        return  new ResponseEntity<>("Account not found",HttpStatus.FORBIDDEN);
    }

    //Verificar que la cuenta de destino pertenezca al cliente autenticado
    String authenticatedUsername = authentication.getName();
    if (!authenticatedUsername.contains(account.getClientId().getFirstName()+account.getClientId().getLastName())){
        return new ResponseEntity<>("Destination account does not belong to authenticated user",HttpStatus.FORBIDDEN);
    }

    //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
    int totalAmount = (int)(amount*1.20);
    LoanApplicationDTO newLoanApply = new LoanApplicationDTO();
    newLoanApply.setAccountNumber(accountNumber);
    newLoanApply.setPayments(payments);
    newLoanApply.setAmount(totalAmount);
    //loanRepository.save(newLoanApply) solo se pueden guardar loans y esta es un LoanApplicationDTO
    return new ResponseEntity<>(HttpStatus.CREATED);
}
}
