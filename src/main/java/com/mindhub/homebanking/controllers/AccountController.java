package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AccountController {
    @Autowired
    private ClientService clientService;
    @Autowired
    AccountService accountService;

    public String randomNumber(){

         String randomNumber;
        do {
            int number = (int) (Math.random() * 899999 + 100000);
            randomNumber = "VIN-" + number;
        } while (accountService.findByNumber(randomNumber) != null);
        return randomNumber;
    }
    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }
    @RequestMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAccount(Authentication authentication) {
        return accountService.getAccountAuthentication(authentication);
    }
    @RequestMapping("/api/clients/current/accounts/{id}")
    public AccountDTO getAccount (@PathVariable Long id){
        return accountService.getAccountID(id);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client.getAccounts().size() >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Customer already has the maximum number of accounts allowed.");
        }

        String accountNumber = randomNumber();
        Account newAccount = new Account(accountNumber, 0.0 , LocalDateTime.now());
        client.addAccount(newAccount);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
