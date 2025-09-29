package com.bancs.payments.services;

import com.bancs.payments.dto.UpiPaymentRequest;
import com.bancs.payments.dto.UpiPaymentRequests;
import com.bancs.payments.dto.UpiPaymentResponse;
import com.bancs.payments.utility.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.bancs.payments.utility.HashUtil.generateHash;

@Service
@RequiredArgsConstructor
public class PayuUpiService {

    @Value("${payu.base-url}")
    private String baseUrl;

    @Value("${payu.merchant-key}")
    private String merchantKey;

    @Value("${payu.merchant-salt}")
    private String merchantSalt;

    private final RestTemplate restTemplate = new RestTemplate();

    public UpiPaymentResponse initiatePayment(Map<String, String> request) throws Exception {
        String paymentUrl = baseUrl + "/_payment";

        String txnId = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        String amount = request.getOrDefault("amount", "10.00");
        String productInfo = request.getOrDefault("productInfo", "TestProduct");
        String firstName = request.getOrDefault("firstname", "John");
        String email = request.getOrDefault("email", "john.doe@example.com");
        String phone = request.getOrDefault("phone", "9999999999");
        String surl = "http://localhost:8080/api/payments/success";
        String furl = "http://localhost:8080/api/payments/failure";

        // Generate hash: key|txnid|amount|productinfo|firstname|email|||||||||||salt
        String hashString = merchantKey + "|" + txnId + "|" + amount + "|" + productInfo + "|" +
                firstName + "|" + email + "|||||||||||" + merchantSalt;
        String hash = this.generateHash(hashString);

        // Build form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("key", merchantKey);
        formData.add("txnid", txnId);
        formData.add("amount", amount);
        formData.add("productinfo", productInfo);
        formData.add("firstname", firstName);
        formData.add("email", email);
        formData.add("phone", phone);
        formData.add("surl", surl);
        formData.add("furl", furl);
        formData.add("hash", hash);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

//        ResponseEntity<String> payuResponse = restTemplate.postForEntity(paymentUrl, entity, String.class);
        restTemplate.getMessageConverters()
                .add(new org.springframework.http.converter.StringHttpMessageConverter());

        ResponseEntity<String> payuResponse = restTemplate.exchange(
                paymentUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        UpiPaymentResponse upiResp = new UpiPaymentResponse();
        upiResp.setStatus("INITIATED");
        upiResp.setMessage("Payment request sent to PayU Sandbox");
        upiResp.setGatewayResponse(payuResponse.getBody());

        return upiResp;
    }

    private String generateHash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

