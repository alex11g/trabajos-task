package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {
    List<CardDTO> getAccount(Authentication authentication);
    Card findByNumber(String number);
    Card findByCvv(int cvv);
    void saveCard(Card card);
}
