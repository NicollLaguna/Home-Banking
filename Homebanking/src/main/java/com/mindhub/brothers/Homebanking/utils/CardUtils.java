package com.mindhub.brothers.Homebanking.utils;

import java.util.Random;

public final class CardUtils {

    public static int getCardCvv(){
        int cvvN;
        Random randomN = new Random();
        int min2 = 100;
        int cvv = 999;
        cvvN = randomN.nextInt((cvv-min2)+1)+min2;
        return cvvN;
    }

    public  static  String getCardNumber(){
        String numberCard;
        Random randomN = new Random();
        int min = 1000;
        int max = 9999;
        int number = randomN.nextInt((max-min)+1)+min;
        int number2 = randomN.nextInt((max-min)+1)+min;
        int number3 = randomN.nextInt((max-min)+1)+min;
        int number4 = randomN.nextInt((max-min)+1)+min;
        numberCard = number+"-"+number2+"-"+number3+"-"+number4;
        return numberCard;
    }
}
