package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();
    List<AccountDTO> getAccountAuthentication(Authentication authentication);
    AccountDTO getAccountID (Long id);
    Account getAccountId(Long id);
    Account findByNumber(String number);
    void saveAccount(Account account);

    String aleatoryNumber();
    String aleatoryNumberNotRepeat();
}
