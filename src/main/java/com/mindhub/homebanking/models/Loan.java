package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private double maxAmount;
    private double interest;

    @ElementCollection
    private List<Integer>payments = new ArrayList<>();

    @OneToMany(mappedBy = "loan")
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments,double interest) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

    @JsonIgnore
    public List<Client> getClients() {
        return clientLoans.stream()
                .map(clientLoan -> clientLoan.getClient())
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
