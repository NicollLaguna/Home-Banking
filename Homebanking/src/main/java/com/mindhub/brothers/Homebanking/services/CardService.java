package com.mindhub.brothers.Homebanking.services;

import com.mindhub.brothers.Homebanking.dtos.CardDTO;
import com.mindhub.brothers.Homebanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {

    List<CardDTO> getCards(Authentication authentication);
    Card findByNumber (String number);
    Card findByCvv (int cvv);
    void saveCard(Card card);
}
