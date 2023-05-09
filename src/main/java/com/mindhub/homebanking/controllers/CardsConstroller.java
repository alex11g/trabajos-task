package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController

public class CardsConstroller {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/api/clients/current/cards")
    public List<CardDTO> getAccount(Authentication authentication) {
        return cardService.getAccount(authentication);
    }


    @PutMapping("/api/clients/current/cards")
    public ResponseEntity<Object> deleteCards ( Authentication authentication , @RequestParam Long id){

        Client client = clientService.findByEmail(authentication.getName());
        Card card = cardService.findById(id);


        if (client == null) {
            return new ResponseEntity<>("You can´t delete an account because you´re not a client", HttpStatus.FORBIDDEN);
        }
        if (card == null){
            return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
        }

        card.setActive(false);
        cardService.saveCard(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCards(Authentication authentication
            , @RequestParam String type, @RequestParam String color) {
        if ( !type.equalsIgnoreCase("CREDIT")  && !type.equalsIgnoreCase("DEBIT")) {
            return new ResponseEntity<>(type + "wrong card type", HttpStatus.FORBIDDEN);
        }
        if ( !color.equalsIgnoreCase("TITANIUM") && !color.equalsIgnoreCase("GOLD")
                && !color.equalsIgnoreCase("SILVER")) {
            return new ResponseEntity<>(color + "the color does not match the card", HttpStatus.FORBIDDEN);
        }

        ClientDTO client = clientService.getClient(authentication);
        if (client == null){
            return new ResponseEntity<>("You can´t create a card, because you are not a client", HttpStatus.FORBIDDEN);
        };
        List<CardDTO> cardsActive = client.getCardDTOS()
                .stream()
                .filter(cardDTO  -> cardDTO.isActive()).collect(Collectors.toList());

        for (CardDTO card : cardsActive) {
            if (card.getType().equals(CardType.valueOf(type)) && card.getColor().equals(CardColor.valueOf(color))) {
                return new ResponseEntity<>("you already have this type of card", HttpStatus.FORBIDDEN);
            }
        }

        if(client.getCardDTOS().size() == 9){
            return new ResponseEntity<>("You can't get more cards", HttpStatus.FORBIDDEN);
        }

        int cvvnumber = CardUtils.generarNumberCvv();
        String cardNumber = cardService.notRepeat();
        Card newCard = new Card(client.getName() + " " + client.getLastName()
                , CardType.valueOf(type), CardColor.valueOf(color), cardNumber, cvvnumber, LocalDateTime.now()
                , LocalDateTime.now().plusYears(5),true);


        Client client1 = clientService.findByEmail(authentication.getName());

        client1.addCard(newCard);
        cardService.saveCard(newCard);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
