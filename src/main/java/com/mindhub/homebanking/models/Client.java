package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private String email;
    private String lastName;
    private String password;

    @OneToMany(mappedBy = "client")
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<ClientLoan> clientLoans = new HashSet<>();
    @OneToMany(mappedBy = "client")
    private Set<Card> cards = new HashSet<>();
    public Client() {
    }

    public Client(String name, String email, String lastName, String password) {
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.password = password;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }
    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }
    public void addCard(Card card) {
        card.setClient(this);
        cards.add(card);
    }

    public List<Loan> getLoans() {
        return clientLoans.stream()
                .map(clientLoan -> clientLoan.getLoan())
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }


    public Set<Account> getAccounts() {
        return accounts;
    }


    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}

