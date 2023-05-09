package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    Optional<Loan> FindById(long id);
    List<LoanDTO> getLoanDTO();
    void saveLoan(Loan loan);
}
