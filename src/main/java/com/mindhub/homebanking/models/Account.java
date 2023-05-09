package com.mindhub.homebanking.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;


    private double balance;
    private LocalDateTime creationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;


    @OneToMany(mappedBy = "account")
    private Set<Transaction> transactions = new HashSet<>();


    public Account() {
    }

    public Account(String number, double balance, LocalDateTime creationDate) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
    }
    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @JsonIgnore
    public Client getClient() {

        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public long getId() {
        return id;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", creationDate=" + creationDate +
                ", client=" + client +
                ", transactions=" + transactions +
                '}';
    }
}
