package com.mindhub.brothers.Homebanking.utils;

import java.util.Random;

public final class AccountUtils {

    public static String getAccountNumber(){
        String accountNumber;
        Random randomN = new Random();
        int min = 0;
        int max = 99999999;
        int number = randomN.nextInt((max-min)+1)+min;
        accountNumber = "VIN"+number;
        return accountNumber;
    }
}
