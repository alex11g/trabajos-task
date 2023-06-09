package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }
    @GetMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAccount(Authentication authentication) {
        return accountService.getAccountAuthentication(authentication);
    }
    @GetMapping("/api/clients/current/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){

        Client client = clientService.getClientAutenticate(authentication);
        Account account = accountService.getAccountId(id);
        AccountDTO accountDTO = accountService.getAccountID(id);

        if ( !client.getAccounts().stream().filter( accountCLient -> accountCLient.getId() == account.getId() ).collect(Collectors.toList()).isEmpty() ){
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("This account is not yours", HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> deleteAccounts ( Authentication authentication , @RequestParam Long id){
        Client client = clientService.findByEmail(authentication.getName());
        Account account4 = accountService.getAccountId(id);
        if (client == null) {
            return new ResponseEntity<>("You can´t delete an account because you´re not a client", HttpStatus.FORBIDDEN);
        }else if( client.getAccounts().
                stream()
                .filter(account1 -> account1.getId() == id)
                .collect(Collectors.toList()).size() == 0)
        {return new ResponseEntity<>("this card is not yours", HttpStatus.FORBIDDEN);}
        if (account4 == null){
            return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
        }else if ( !account4.isActive() ){
            return new ResponseEntity<>("this card is already inactive", HttpStatus.FORBIDDEN);
        } else if( account4.getBalance() > 0 ){
            return new ResponseEntity<>("You can't delete this account, because you have money", HttpStatus.FORBIDDEN);
        }
        account4.setActive(false);
        account4.getTransactions().stream().forEach(valor -> valor.setActive(false));
        accountService.saveAccount(account4);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication , @RequestParam String type) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client.getAccounts().stream().filter(valor -> valor.isActive()).collect(Collectors.toList()).size() >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Customer already has the maximum number of accounts allowed.");
        }

        String accountNumber = accountService.aleatoryNumber();
        Account newAccount = new Account(accountNumber, 0.0 , LocalDateTime.now(),true, AccountType.valueOf(type));
        client.addAccount(newAccount);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
