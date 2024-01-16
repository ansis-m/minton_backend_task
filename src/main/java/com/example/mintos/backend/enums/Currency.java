package com.example.mintos.backend.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Currency {
    EUR("Euro", "EUR", 1.09),
    USD("US Dollar", "USD", 1.00),
    JPY("Japanese Yen", "JPY", 0.068),
    BGN("Bulgarian Lev", "BGN", 0.56),
    CZK("Czech Republic Koruna", "CZK", 0.044),
    DKK("Danish Krone", "DKK", 0.15),
    GBP("British Pound Sterling", "GBP", 1.27),
    HUF("Hungarian Forint", "HUF", 0.0029),
    PLN("Polish Zloty", "PLN", 0.248),
    RON("Romanian Leu", "RON", 0.22),
    SEK("Swedish Krona", "SEK", 0.096),
    CHF("Swiss Franc", "CHF", 1.16),
    ISK("Icelandic Króna", "ISK", 0.0073),
    NOK("Norwegian Krone", "NOK", 0.096),
    HRK("Croatian Kuna", "HRK", 0.142),
    RUB("Russian Ruble", "RUB", 0.011),
    TRY("Turkish Lira", "TRY", 0.033),
    AUD("Australian Dollar", "AUD", 0.66),
    BRL("Brazilian Real", "BRL", 0.20),
    CAD("Canadian Dollar", "CAD", 0.74),
    CNY("Chinese Yuan", "CNY", 0.14),
    HKD("Hong Kong Dollar", "HKD", 0.13),
    IDR("Indonesian Rupiah", "IDR", 0.000064),
    ILS("Israeli New Sheqel", "ILS", 0.27),
    INR("Indian Rupee", "INR", 0.012),
    KRW("South Korean Won", "KRW", 0.00075),
    MXN("Mexican Peso", "MXN", 0.058),
    MYR("Malaysian Ringgit", "MYR", 0.21),
    NZD("New Zealand Dollar", "NZD", 0.62),
    PHP("Philippine Peso", "PHP", 0.018),
    SGD("Singapore Dollar", "SGD", 0.75),
    THB("Thai Baht", "THB", 0.028),
    ZAR("South African Rand", "ZAR", 0.053);

    private String currencyName;
    private String currencyCode;
    private double exchangeRateToUSD;

    Currency(String currencyName, String currencyCode, double exchangeRateToUSD) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.exchangeRateToUSD = exchangeRateToUSD;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getExchangeRateToUSD() {
        return exchangeRateToUSD;
    }

    public static Currency getCurrency(String currencyIdentifier) {
        for (Currency currency : values()) {
            if (currency.getCurrencyCode().equalsIgnoreCase(currencyIdentifier) || currency.getCurrencyName().equalsIgnoreCase(currencyIdentifier)) {
                return currency;
            }
        }
        return null;
    }

    public static boolean contains(String currencyIdentifier) {
        return Arrays.stream(values()).anyMatch(currency ->
                currency.getCurrencyCode().equalsIgnoreCase(currencyIdentifier) || currency.getCurrencyName().equalsIgnoreCase(currencyIdentifier)
        );
    }

    public static Map<String, String> getCurrencyCodeToNameMap() {
        Map<String, String> currencyMap = new HashMap<>();
        for (Currency currency : values()) {
            currencyMap.put(currency.getCurrencyCode(), currency.getCurrencyName());
        }
        return currencyMap;
    }
}