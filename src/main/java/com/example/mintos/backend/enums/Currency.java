package com.example.mintos.backend.enums;

public enum Currency {
    USD, EUR, JPY, GBP, AUD, CAD, CHF, CNY, SEK, NZD,
    MXN, SGD, HKD, NOK, KRW, TRY, INR, RUB, BRL, ZAR,
    DKK, PLN, TWD, THB, IDR, HUF, CZK, ILS, CLP, PHP,
    AED, COP, SAR, MYR, RON, PEN, VND, KWD, MAD, HRK,
    BGN, JOD, BHD, UAH, KES, OMR, NGN, DOP, PKR, QAR;

    public static boolean contains(String test) {
        for (Currency c : Currency.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}