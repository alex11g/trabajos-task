package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository repositoryCliente, AccountRepository repositoryAccount, TransactionRepository repositoryTransaction, LoanRepository repositoryLoan, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers
			Client client1 = new Client("Melba", "melba@mindhub.com","Morel",passwordEncoder.encode("3221"));
			repositoryCliente.save(client1);
			Client client2 = new Client("Andres", "Andres@mindhub.com","Rey",passwordEncoder.encode("3221"));
			repositoryCliente.save(client2);
			Client client3 = new Client("Alexander", "alexander@gmail.com","Rey",passwordEncoder.encode("3221"));
			repositoryCliente.save(client3);
			Account account1 = new Account("VIN001", 5000.0,LocalDateTime.now(),true,AccountType.CURRENT);
			client1.addAccount(account1);
			repositoryAccount.save(account1);
			Account account2= new Account("VIN002", 7500.0, LocalDateTime.now().plusDays(1),true,AccountType.SAVING);
			client1.addAccount(account2);
			repositoryAccount.save(account2);
			Account account3 = new Account("VIN003", 500000.0,LocalDateTime.now(),true,AccountType.SAVING );
			client2.addAccount(account3);
			repositoryAccount.save(account3);
			Account account4= new Account("VIN004", 750000.0, LocalDateTime.now().plusDays(1),true,AccountType.CURRENT);
			client2.addAccount(account4);
			repositoryAccount.save(account4);


			Transaction transaction1 = new Transaction(TransactionType.DEBIT, 3021.00,"online-pages", LocalDateTime.now(), account1.getBalance(),true);
			account1.addTransaction(transaction1);
			repositoryTransaction.save(transaction1);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 111.00,"online-pages",LocalDateTime.now(),account2.getBalance(),true);
			account2.addTransaction(transaction2);
			repositoryTransaction.save(transaction2);

			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 3000212.00,"free games", LocalDateTime.now(),account3.getBalance(),true);
			account3.addTransaction(transaction3);
			repositoryTransaction.save(transaction3);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 3.00,"free games",LocalDateTime.now(),account4.getBalance(),true);
			account4.addTransaction(transaction4);
			repositoryTransaction.save(transaction4);

			Loan loan1= new Loan("MORTGAGE", 500000,List.of(12,24,36,48,60),1.2);
			repositoryLoan.save(loan1);
			Loan loan2= new Loan("STAFF", 100000,List.of(6,12,24),1.3);
			repositoryLoan.save(loan2);
			Loan loan3= new Loan("AUTOMOTIVE", 300000,List.of(6,12,24,36),1.4);
			repositoryLoan.save(loan3);



			ClientLoan clientLoan1= new ClientLoan(400.000,60);
			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2= new ClientLoan(50.000,12);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan2);

			ClientLoan clientLoan3= new ClientLoan(100.000,24);
			client2.addClientLoan(clientLoan3);
			loan2.addClientLoan(clientLoan3);
			clientLoanRepository.save(clientLoan3);

			ClientLoan clientLoan4= new ClientLoan(200.000,36);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan4);

			Card card1= new Card(client1.getName() + " " + client1.getLastName(),CardType.DEBIT,CardColor.GOLD,"4377 1763 3081 1832",442,LocalDateTime.now(),LocalDateTime.now().plusYears(-3),true);
			client1.addCard(card1);
			cardRepository.save(card1);

			Card card2= new Card(client1.getName() + " " + client1.getLastName(),CardType.CREDIT,CardColor.TITANIUM,"4539 5970 6728 3447",668,LocalDateTime.now(),LocalDateTime.now().plusYears(-3),true);
			client1.addCard(card2);
			cardRepository.save(card2);


			Card card3= new Card(client2.getName() + " " + client2.getLastName(),CardType.CREDIT,CardColor.SILVER,"4024 0071 7734 5287",632,LocalDateTime.now(),LocalDateTime.now().plusYears(5),true);
			client2.addCard(card3);
			cardRepository.save(card3);



		};
	}


}
