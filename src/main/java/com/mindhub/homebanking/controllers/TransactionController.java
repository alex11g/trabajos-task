package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
public class TransactionController {
    @Autowired
    private ClientService clientService;

    @Autowired
     private AccountService accountService;

    @Autowired
    private TransactionService transactionService;
    @Transactional
    @PostMapping("/api/transaction")
    public ResponseEntity<Object> newTransaction(Authentication authentication
            , @RequestParam double amount, @RequestParam String description
            , @RequestParam String account1 , @RequestParam String account2){

        Client client1 = clientService.findByEmail(authentication.getName());
        Account Account1 = accountService.findByNumber(account1.toUpperCase());
            Account Account2 = accountService.findByNumber(account2.toUpperCase());

        if(amount < 1){
            return  new ResponseEntity<>("Your amount cannot be less than 0 or negative", HttpStatus.FORBIDDEN);
        }
        if(description.isEmpty()){
            return  new ResponseEntity<>("You must have a description", HttpStatus.FORBIDDEN);
        }
        if(Account1 == null){
            return  new ResponseEntity<>("The account origin does not exist", HttpStatus.FORBIDDEN);
        }
        if(Account2 == null){
            return  new ResponseEntity<>("The account destiny does not exist", HttpStatus.FORBIDDEN);
        }
        if(Account1.equals(Account2)){
            return  new ResponseEntity<>("You can't send money to yourself", HttpStatus.FORBIDDEN);
        }
        if(Account1.getBalance() < amount){
            return  new ResponseEntity<>("Your balance is not enough", HttpStatus.FORBIDDEN);
        }
        if(client1.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(account1))
                .collect(Collectors.toList()).size() == 0){
            return  new ResponseEntity<>("Your balance is not enough", HttpStatus.FORBIDDEN);
        }

        Account1.setBalance(Account1.getBalance() - amount);
        Account2.setBalance(Account2.getBalance() + amount);

        Transaction newtransaction = new Transaction(TransactionType.DEBIT,amount,description, LocalDateTime.now(), Account1.getBalance(),true);
        Account1.addTransaction(newtransaction);
        transactionService.saveTransaction(newtransaction);
        Transaction newtransaction2 = new Transaction(TransactionType.CREDIT,amount,description, LocalDateTime.now(),Account2.getBalance(),true);
        Account2.addTransaction(newtransaction2);
        transactionService.saveTransaction(newtransaction2);


        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
