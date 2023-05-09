package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController

public class CardsConstroller {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    public int randomNumbercvv(){
        int Min= 100;
        int Max = 899;
        return (int) (Math.random() * Max + Min);
    }
    public static int aleatoryNumberCvv(){
        int Max = 899;
        int Min = 100;
        return (int) (Math.random() * Max + Min);
    }

    private String generateCardNumber() {
        String cardNumber;
        do {
            int firstGroup = (int) (Math.random() * 8999 + 1000);
            int secondGroup = (int) (Math.random() * 8999 + 1000);
            int thirdGroup = (int) (Math.random() * 8999 + 1000);
            int fourthGroup = (int) (Math.random() * 8999 + 1000);
            cardNumber = firstGroup + "-" + secondGroup + "-" + thirdGroup + "-" + fourthGroup;
        } while (cardService.findByNumber(cardNumber) != null);
        return cardNumber;
    }



    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO> getAccount(Authentication authentication) {
        return cardService.getAccount(authentication);
    }

    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCards(Authentication authentication
            , @RequestParam String type, @RequestParam String color) {



        if ( !type.equalsIgnoreCase("CREDIT")  && !type.equalsIgnoreCase("DEBIT")) {
            return new ResponseEntity<>(type + "wrong card type", HttpStatus.FORBIDDEN);
        }
        if ( !color.equalsIgnoreCase("TITANIUM") && !color.equalsIgnoreCase("GOLD")
                && !color.equalsIgnoreCase("SILVER")) {
            return new ResponseEntity<>(color + "the color does not match the card", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        if (client == null){
            return new ResponseEntity<>("You can´t create a card, because you are not a client", HttpStatus.FORBIDDEN);
        };

        for (Card card : client.getCards()) {
            if (card.getType().equals(CardType.valueOf(type)) && card.getColor().equals(CardColor.valueOf(color))) {
                return new ResponseEntity<>("you already have this type of card", HttpStatus.FORBIDDEN);
            }
        }

        int cvvnumber = randomNumbercvv();
        String cardNumber = generateCardNumber();
        Card newCard = new Card(client.getName() + " " + client.getLastName()
                , CardType.valueOf(type), CardColor.valueOf(color), cardNumber, cvvnumber, LocalDateTime.now()
                , LocalDateTime.now().plusYears(5));
        client.addCard(newCard);
        cardService.saveCard(newCard);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
