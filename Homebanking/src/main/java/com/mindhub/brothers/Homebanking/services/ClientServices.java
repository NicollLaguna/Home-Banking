package com.mindhub.brothers.Homebanking.services;

import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.Client;
import org.springframework.security.core.Authentication;


import java.util.List;


public interface ClientServices {

    void saveClient(Client client);

    List<ClientDTO> getClients();

    ClientDTO getClientAuthentication(Authentication authentication);

    Client findByEmail(String email);

}
