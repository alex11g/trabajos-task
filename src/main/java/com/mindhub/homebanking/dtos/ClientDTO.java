package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String name;
    private String email;
    private String lastName;

    private Set<AccountDTO> accountDTOS;
    private Set<ClientLoanDTO> clientLoanDTOS;
    private Set<CardDTO> cardDTOS;




    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.lastName = client.getLastName();
        this.accountDTOS = client.getAccounts()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
        this.clientLoanDTOS = client.getClientLoans()
                .stream()
                .map(clientLoan -> new ClientLoanDTO(clientLoan))
                .collect(Collectors.toSet());

        this.cardDTOS = client.getCards()
                .stream()
                .map(card -> new CardDTO(card))
                .collect(Collectors.toSet());
    }


    public long getId() {
        return id;
    }

    public Set<AccountDTO> getAccountDTOS() {
        return accountDTOS;
    }

    public String getName() {
        return name;
    }



    public String getEmail() {
        return email;
    }


    public String getLastName() {
        return lastName;
    }

    public Set<ClientLoanDTO> getClientLoanDTOS() {
        return clientLoanDTOS;
    }

    public Set<CardDTO> getCardDTOS() {
        return cardDTOS;
    }
}
