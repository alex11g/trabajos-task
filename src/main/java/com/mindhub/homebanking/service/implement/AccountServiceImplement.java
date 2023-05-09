package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(accounts -> new AccountDTO(accounts)).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> getAccountAuthentication(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()))
                .getAccountDTOS().stream().collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountID(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
