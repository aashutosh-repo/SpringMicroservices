package com.bancs.payments.services;

import com.bancs.payments.dto.*;
import com.bancs.payments.entity.Transaction;
import com.bancs.payments.mapper.TransactionMapper;
import com.bancs.payments.repository.PaymentsRepository;
import com.bancs.payments.utility.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentsRepository paymentsRepository;
    private final WebClient.Builder webClientBuilder;

    public List<TransactionDTO> getTransactionDetails(){
        List<Transaction> transactionList = paymentsRepository.findAll();
        return transactionList.stream().map(TransactionMapper::toDTO).toList();
    }

    public PaymentSummary initiatePayment(String customerId, String customerType) {

        Mono<CustomerDto> customerMono = webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8080)
                        .path("/customer/customerById")
                        .queryParam("customerId", customerId)
                        .queryParam("customerType", customerType)
                        .build())
                .retrieve()
                .bodyToMono(CustomerDto.class);

        Flux<AccountDto> accountFlux = webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8080)
                        .path("/account/viewAccountByCustomerId")
                        .queryParam("customerId", customerId)
                        .build())
                .retrieve()
                .bodyToFlux(AccountDto.class);

        return Mono.zip(customerMono, accountFlux.collectList())
                .map(tuple -> {
                    CustomerDto customer = tuple.getT1();
                    List<AccountDto> accounts = tuple.getT2();

                    // Custom logic to build payment summary
                    return new PaymentSummary(customer, accounts.getFirst());
                }).block();
    }

    public String initiate(PaymentRequest req) {
        // generate orderId, call PhonePe/Paytm API
        // return their redirect URL
        return "https://paytm.com/checkout?txnId=123";
    }

    public void updatePaymentStatus(PaymentResponse response) {
        // verify signature from gateway
        // update DB: mark order SUCCESS/FAILED
    }
}
