package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class CardDTO {
    private long id;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;
    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder= card.getCardholder();
        this.type= card.getType();
        this.color= card.getColor();
        this.number = card.getNumber();
        this.cvv= card.getCvv();
        this.thruDate= card.getThruDate();
        this.fromDate= card.getFromDate();

    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }


}
