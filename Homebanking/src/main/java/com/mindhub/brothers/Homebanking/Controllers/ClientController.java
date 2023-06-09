package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(Collectors.toList());
    }

    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        return optionalClient.map(client -> new ClientDTO(client)).orElse(null);
    };
}
