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


    @RequestMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoanDTO();
    }

    @Transactional
    @RequestMapping(path = "/api/loans", method = RequestMethod.POST)
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


        ClientLoan newClientLoan = new ClientLoan(loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.2 ), loanApplicationDTO.getPayments());
        clientLoanService.saveClientLoan(newClientLoan);
        Transaction newtransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.get().getName() + " loan approved", LocalDateTime.now());
        transactionService.saveTransaction(newtransaction);
        accountAuthenticated.setBalance(accountAuthenticated.getBalance() + loanApplicationDTO.getAmount());
        accountAuthenticated.addTransaction(newtransaction);

        loan.get().addClientLoan(newClientLoan);
        client.addClientLoan(newClientLoan);
        clientService.saveClient(client);

        return new ResponseEntity<>("Loan approved ", HttpStatus.CREATED);
    }
}
