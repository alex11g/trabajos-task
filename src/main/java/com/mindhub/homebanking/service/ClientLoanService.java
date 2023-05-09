package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.ClientLoan;

public interface ClientLoanService {
    void saveClientLoan(ClientLoan clientLoan);
    ClientLoan getClientLoan(Long id);
}
