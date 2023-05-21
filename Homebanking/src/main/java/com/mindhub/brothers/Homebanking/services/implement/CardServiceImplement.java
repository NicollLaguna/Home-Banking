package com.mindhub.brothers.Homebanking.services.implement;

import com.mindhub.brothers.Homebanking.dtos.CardDTO;
import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.Card;
import com.mindhub.brothers.Homebanking.repositories.CardRepository;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import com.mindhub.brothers.Homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Override
    public List<CardDTO> getCards(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getCards().stream().collect(toList()) ;
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public Card findByCvv(int cvv) {
        return cardRepository.findByCvv(cvv);
    }

    //Eliminar tarjeta
    @Override
    public Card findById(long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
