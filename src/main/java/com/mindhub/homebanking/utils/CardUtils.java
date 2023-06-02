package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public final class CardUtils {
    private CardUtils() {
    }

    public static int generarNumberCvv() {
        int Min= 100;
        int Max = 899;
        return (int) (Math.random() * Max + Min);
    }

    public static String getCards() {
        String numberCard = "";
        String numberCardInit = "";
        for (int i = 0; i < 4; i++) {
            int min = 1000;
            int max = 8999;
            numberCardInit += (int) (Math.random() * max + min) + "-";
        }
        numberCard = numberCardInit.substring(0 , numberCardInit.length() - 1);
        return numberCard;
    }

    public static String aleatoryNumber() {
        Random random = new Random();
        int min = 100000;
        int max = 899999;
        return ("VIN-" + random.nextInt(max + min));
    }
}
