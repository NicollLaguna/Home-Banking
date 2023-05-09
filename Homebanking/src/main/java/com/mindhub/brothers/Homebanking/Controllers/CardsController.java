package com.mindhub.brothers.Homebanking.Controllers;

import com.mindhub.brothers.Homebanking.dtos.AccountDTO;
import com.mindhub.brothers.Homebanking.dtos.CardDTO;
import com.mindhub.brothers.Homebanking.dtos.ClientDTO;
import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.Card;
import com.mindhub.brothers.Homebanking.models.CardColor;
import com.mindhub.brothers.Homebanking.models.CardType;
import com.mindhub.brothers.Homebanking.repositories.CardRepository;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import com.mindhub.brothers.Homebanking.services.CardService;
import com.mindhub.brothers.Homebanking.services.ClientServices;
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

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.GET)
    public List<CardDTO> getCards(Authentication authentication){
        return cardService.getCards(authentication);
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(Authentication authentication, @RequestParam String type, @RequestParam String color){

        if (type.isEmpty() || color.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Random randomN = new Random();
        int min = 1000;
        int min2 = 100;
        int max = 9999;
        int cvv = 999;
        int number = randomN.nextInt((max-min)+1)+min;
        int number2 = randomN.nextInt((max-min)+1)+min;
        int number3 = randomN.nextInt((max-min)+1)+min;
        int number4 = randomN.nextInt((max-min)+1)+min;
        int cvvN = randomN.nextInt((cvv-min2)+1)+min2;
        if (cardService.findByCvv(cvvN) != null){
            return  new ResponseEntity<>("Cvv cannot be use", HttpStatus.FORBIDDEN);
        }
        if (cardService.findByNumber(number+" "+number+" "+number+" "+number) != null){
            return  new ResponseEntity<>("Number cannot be use", HttpStatus.FORBIDDEN);
        }
        if (clientServices.findByEmail(authentication.getName()).getCards().size()<=5){
            for (Card card : clientServices.findByEmail(authentication.getName()).getCards()) {
                if (card.getType().equals(CardType.valueOf(type)) && card.getColor().equals(CardColor.valueOf(color))) {
                    return new ResponseEntity<>("There is a card with this color and type", HttpStatus.FORBIDDEN);
                }
            }
            Card newCard = new Card(CardType.valueOf(type), CardColor.valueOf(color), number+"-"+number2+"-"+number3+"-"+number4, LocalDate.now(), LocalDate.now().plusYears(5),clientServices.findByEmail(authentication.getName()).getFirstName()+" "+clientServices.findByEmail(authentication.getName()).getLastName(),cvvN);
            clientServices.findByEmail(authentication.getName()).addCards(newCard);
            cardService.saveCard(newCard);
        }else {
            return new ResponseEntity<>("Cannot create more than 6 cards",HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
