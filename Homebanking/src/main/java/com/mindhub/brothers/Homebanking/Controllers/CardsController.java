package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.dtos.AccountDTO;
import com.mindhub.brothers.Homebanking.dtos.CardDTO;
import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.*;
import com.mindhub.brothers.Homebanking.repositories.CardRepository;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import com.mindhub.brothers.Homebanking.services.CardService;
import com.mindhub.brothers.Homebanking.services.ClientServices;
import com.mindhub.brothers.Homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/api")
public class CardsController {
    @Autowired
    CardService cardService;

    @Autowired
    ClientServices clientServices;

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication){
        return cardService.getCards(authentication);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard(Authentication authentication, @RequestParam String type, @RequestParam String color){
    Client client= clientServices.findByEmail(authentication.getName());

        int cvvN;
        do{
            cvvN = CardUtils.getCardCvv();
        }while (cardService.findByCvv(cvvN)!=null);

        String cardNumber;
        do{
            cardNumber = CardUtils.getCardNumber();
        }while (cardService.findByNumber(cardNumber)!=null);

        if (type.isEmpty() || color.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        int totalCards = client.getCards().size();
        int activeCards = (int) client.getCards().stream().filter(card -> card.getActive()).count();

        if (totalCards >= 12 || activeCards>=6) {
            return new ResponseEntity<>("Client already has the maximum number of cards allowed.",HttpStatus.FORBIDDEN);
        }

        for (Card card : clientServices.findByEmail(authentication.getName()).getCards()) {
                if (card.getType().equals(CardType.valueOf(type)) && card.getColor().equals(CardColor.valueOf(color)) && card.getActive()) {
                    return new ResponseEntity<>("There is a card with this color and type", HttpStatus.FORBIDDEN);
                }
            }

        Card newCard = new Card(CardType.valueOf(type), CardColor.valueOf(color), cardNumber, LocalDate.now(), LocalDate.now().plusYears(5),clientServices.findByEmail(authentication.getName()).getFirstName()+" "+clientServices.findByEmail(authentication.getName()).getLastName(),cvvN,true);
        clientServices.findByEmail(authentication.getName()).addCards(newCard);
        cardService.saveCard(newCard);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    //Ocultar tarjetas
    @PutMapping("/clients/current/cards/{id}")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @PathVariable Long id){
        Client client= clientServices.findByEmail(authentication.getName());
        Card card = cardService.findById(id);

        if (!client.getCards().contains(card)){
            return new ResponseEntity<>("This card is not yours",HttpStatus.FORBIDDEN);
        }

        if (card == null){
            return new ResponseEntity<>("This card not found", HttpStatus.FORBIDDEN);
        }

        if (!card.getActive()){
            return new ResponseEntity<>("This card is inactive", HttpStatus.FORBIDDEN);
        }

        card.setActive(false);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card deleted", HttpStatus.ACCEPTED);
    }
}
