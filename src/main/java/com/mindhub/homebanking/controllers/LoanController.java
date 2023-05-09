package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoanDTO();
    }

//    @PostMapping("/api/loans/manager")
//    public  ResponseEntity<Object> cretedloans(
//            @
//    )
@PostMapping("/api/loans/manager")
    public ResponseEntity<Object> CreateLoan(@RequestBody Loan loan) {

    if (loan.getName().isBlank()){
        return new ResponseEntity<>("Please enter a name for the new loan", HttpStatus.FORBIDDEN);
    } else if( loan.getMaxAmount() < 1 ){
        return new ResponseEntity<>("Please enter an amount bigger than 1", HttpStatus.FORBIDDEN);
    } else if ( loan.getPayments().size() == 0 ){
        return new ResponseEntity<>("Please enter a valid amount of payments", HttpStatus.FORBIDDEN);
    } else if ( loan.getInterest() < 1 ){
        return new ResponseEntity<>("Please enter an percentage of interest ", HttpStatus.FORBIDDEN);
    }

    Loan newLoan = new Loan(loan.getName(), loan.getMaxAmount() , loan.getPayments(), loan.getInterest());
    loanService.saveLoan(newLoan);

    return new ResponseEntity<>(HttpStatus.CREATED);
}






    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> newLoan(Authentication authentication
            , @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client = clientService.findByEmail(authentication.getName());
        Account accountAuthenticated = accountService.findByNumber(loanApplicationDTO.getAccountDestiny().toUpperCase());
        Optional<Loan> loan = loanService.FindById(loanApplicationDTO.getId());
        Set<ClientLoan> clientLoans;

        if ( loan.isEmpty() ){
            return new ResponseEntity<>("The selected loan is invalid", HttpStatus.FORBIDDEN);
        }else{
            clientLoans =  client.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan().getName().equalsIgnoreCase(loan.get().getName())).collect(Collectors.toSet());
        }
        if (!loan.get().getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Installments are not available for this loan", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount().isNaN()){
            return new ResponseEntity<>("The amount entered is not available", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() < 1){
            return new ResponseEntity<>("Amount cannot be negative or less than 1", HttpStatus.FORBIDDEN);
        }
        if (loan.get().getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("The quantity requested exceeds the maximum quantity available", HttpStatus.FORBIDDEN);
        }
        if(accountAuthenticated == null){
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if (accountAuthenticated == null){
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountAuthenticated)){
            return new ResponseEntity<>("This account does not belong to an authenticated client", HttpStatus.FORBIDDEN);
        }
        if (clientLoans.size() > 0){
            return new ResponseEntity<>("Do you already have a loan of this type", HttpStatus.FORBIDDEN);
        }


        ClientLoan newClientLoan = new ClientLoan(loanApplicationDTO.getAmount() + loanApplicationDTO.getAmount() * loan.get().getInterest(), loanApplicationDTO.getPayments());
        clientLoanService.saveClientLoan(newClientLoan);
        Transaction newtransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.get().getName() + " loan approved", LocalDateTime.now(),accountAuthenticated.getBalance(),true);
        transactionService.saveTransaction(newtransaction);
        accountAuthenticated.setBalance(accountAuthenticated.getBalance() + loanApplicationDTO.getAmount());
        accountAuthenticated.addTransaction(newtransaction);

        loan.get().addClientLoan(newClientLoan);
        client.addClientLoan(newClientLoan);
        clientService.saveClient(client);

        return new ResponseEntity<>("Loan approved ", HttpStatus.CREATED);
    }
    @Transactional
    @PostMapping("/api/current/loans")
    public ResponseEntity<Object> PayLoans (Authentication authentication ,
                                            @RequestParam Long id, @RequestParam String account, @RequestParam double amountEntered  ){

        Client client = clientService.getClientAutenticate(authentication);
        ClientLoan clientLoan = clientLoanService.getClientLoan(id);
        String description = "Pay " + clientLoan.getLoan().getName() + " loan";
        Account accountAutenticate = accountService.findByNumber(account);

        if( clientLoan == null ){
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);
        } else if( client == null){
            return new ResponseEntity<>("You are not registered as a client", HttpStatus.FORBIDDEN);
        }
        if ( account.isBlank() ){
            return new ResponseEntity<>("PLease enter an account", HttpStatus.FORBIDDEN);
        } else if ( client.getAccounts().stream().filter(accounts -> accounts.getNumber().equalsIgnoreCase(account)).collect(Collectors.toList()).size() == 0){
            return new ResponseEntity<>("This account is not yours.", HttpStatus.FORBIDDEN);}

        if ( amountEntered < 1 ){
            return new ResponseEntity<>("PLease enter an amount bigger than 0", HttpStatus.FORBIDDEN);
        }  else if ( accountAutenticate.getBalance() < amountEntered ){
            return new ResponseEntity<>("Insufficient balance in your account " + accountAutenticate.getNumber(), HttpStatus.FORBIDDEN);}

        accountAutenticate.setBalance( accountAutenticate.getBalance() - amountEntered );
        clientLoan.setAmount( clientLoan.getAmount() - amountEntered);
        clientLoan.setPayments(clientLoan.getPayments() - 1 );

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amountEntered , description, LocalDateTime.now(), accountAutenticate.getBalance(),true);
        accountAutenticate.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);



        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
