package com.spring.core.services.si;

public interface TokenInterface {
    String tokenizeCard(String cardNumber);
    String detokenize(String token);
}
