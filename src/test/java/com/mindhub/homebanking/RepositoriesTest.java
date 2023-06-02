package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class RepositoriesTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
     LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void existAccount()
    {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }
    @Test
    public  void  existAccountNumber()
    {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("balance", is(greaterThanOrEqualTo(0.0) ))));
    }
    @Test
    public void existCard()
    {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }
    @Test
    public void existCardCvv()
    {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cvv", isA(Integer.class))));
    }
    @Test
    public void existClientLoan()
    {
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans,is(not(empty())));
    }

    @Test
    public void existClient()
    {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }
    @Test
    public void existClientPassword()
    {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("password",isA(String.class))));
    }
    @Test
    public void existClientLoanAmount()
    {
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans, hasItem(hasProperty("amount", is(greaterThanOrEqualTo(0.0) ))));
    }

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }
    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("STAFF"))));
    }

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void existTransactionType(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("amount", is(3000212.00))));
    }



}