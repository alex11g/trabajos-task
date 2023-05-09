package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<ClientDTO> getClient() {
         return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientDTO(Long id) {
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @Override
    public void saveClient(Client client) {
    clientRepository.save(client);
    }

    @Override
    public ClientDTO getClient(Authentication authentication) {
        return  new ClientDTO(clientRepository.findByEmail(authentication.getName()));

    }
    @Override
    public Client getClientAutenticate(Authentication authentication) {
        return (clientRepository.findByEmail(authentication.getName()));

    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

}
