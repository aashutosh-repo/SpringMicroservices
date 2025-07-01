package com.spring.core.utils;

public class CardMaskUtil {

    /**
     * Masks a credit card number by showing only the last 4 digits.
     * Example: "4676 7755 9898 4444" → "************4444"
     *
     * @param cardNumber The full credit card number as a string or numeric string
     * @return Masked card number
     */
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 4) {
            throw new IllegalArgumentException("Invalid card number");
        }
        String cleaned = cardNumber.replaceAll("[\\s-]", "");
        int length = cleaned.length();
        String last4 = cleaned.substring(length - 4);
        String masked = "*".repeat(length - 4);

        return masked + last4;
    }
}
