package com.mindhub.brothers.Homebanking.dtos;

import com.mindhub.brothers.Homebanking.models.Card;
import com.mindhub.brothers.Homebanking.models.CardColor;
import com.mindhub.brothers.Homebanking.models.CardType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private CardType type;
    private CardColor color;
    private String number;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private String cardholder;
    private int cvv;

    //Eliminar tarjeta
    private Boolean active;

    public CardDTO (Card card){
        this.id= card.getId();
        this.type= card.getType();
        this.color=card.getColor();
        this.number=card.getNumber();
        this.fromDate=card.getFromDate();
        this.thruDate=card.getThruDate();
        this.cardholder= card.getCardholder();
        this.cvv= card.getCvv();
        this.active = card.getActive();
    }

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getCardholder() {
        return cardholder;
    }

    public int getCvv() {
        return cvv;
    }

    public Boolean getActive() {
        return active;
    }
}
