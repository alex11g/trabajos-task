package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {



    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;


    @GetMapping("/clients")
    public List<ClientDTO> getClients (){
       return clientService.getClient();
    };
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id ){
        return clientService.getClientDTO(id);
    };
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        return clientService.getClient(authentication);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isBlank()  || !firstName.matches("^[a-zA-Z]*$")){
            return new ResponseEntity<>("Invalid name, only letters are allowed.", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()  || !lastName.matches("^[a-zA-Z]*$")){
            return new ResponseEntity<>("Invalid last name, only letters are allowed.", HttpStatus.FORBIDDEN);
        }
        if ( email.isBlank() || !email.contains("@") ) {
            return new ResponseEntity<>("Error, please enter a valid email address.", HttpStatus.FORBIDDEN);
        }
//        Password parameter
        if ( password.isBlank()) {
            return new ResponseEntity<>("Password required", HttpStatus.FORBIDDEN);}


        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client newClient = new Client(firstName, email,lastName, passwordEncoder.encode(password));
        clientService.saveClient(newClient);
        String accountNumber = accountService.aleatoryNumberNotRepeat();
        Account newAccount = new Account("VIN-" + accountNumber, 0, LocalDateTime.now(),true, AccountType.SAVING);
        newClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

