package com.mindhub.brothers.Homebanking.services.implement;

import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import com.mindhub.brothers.Homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientServices {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @Override
    public ClientDTO getClientAuthentication(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}
