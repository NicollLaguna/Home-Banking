package com.mindhub.brothers.Homebanking.services.implement;

import com.mindhub.brothers.Homebanking.dtos.LoanDTO;
import com.mindhub.brothers.Homebanking.models.Loan;
import com.mindhub.brothers.Homebanking.repositories.LoanRepository;
import com.mindhub.brothers.Homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }

    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

}
