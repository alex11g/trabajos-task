package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    LoanRepository loanRepository;


    @Override
    public Optional<Loan> FindById(long id) {
        return loanRepository.findById(id);
    }

    @Override
    public List<LoanDTO> getLoanDTO() {
        return loanRepository.findAll()
                .stream()
                .map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }
}
