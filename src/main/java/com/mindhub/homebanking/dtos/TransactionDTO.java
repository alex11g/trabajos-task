package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private double amount;
    private  String description;
    private LocalDateTime date;

    private Set<AccountDTO> accountDTOS;


    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();

    }

    public Long getId() {
        return id;
    }


    public TransactionType getType() {
        return type;
    }


    public double getAmount() {
        return amount;
    }



    public String getDescription() {
        return description;
    }



    public LocalDateTime getDate() {
        return date;
    }




}
