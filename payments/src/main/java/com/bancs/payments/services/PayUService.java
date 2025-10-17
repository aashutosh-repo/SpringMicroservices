package com.bancs.payments.services;

import com.bancs.payments.constants.TransactionConstants;
import com.bancs.payments.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayUService {

    @Value("${payu.base-url}")
    private String baseUrl;

    @Value("${payu.merchant-key}")
    private String merchantKey;

    @Value("${payu.merchant-salt}")
    private String merchantSalt;
    private final TransactionService transactionService;

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

    public String buildHostedCheckoutForm(PayURequest request) throws Exception {

        String txnId = request.getOrderId();  // or generate unique txn id
        String productinfo = "MyProduct";
        String surl = "http://localhost:8085/payments/payu/success";
        String furl = "http://localhost:8085/payments/payu/failure";


        //initiate the payment request
        PaymentInitResponse response = initiatePaymentRequest(request);
        if (response.getTransactionId() == null) {
            throw new Exception("Failed to initiate transaction");
        }
        // Hash string: key|txnid|amount|productinfo|firstname|email||||||||||salt
//        String hashString = merchantKey + "|" + response.getTransactionId() + "|" + request.getAmount() + "|" + productinfo + "|" +
//                request.getFirstname() + "|" + request.getEmail() + "||||||" + merchantSalt;
        String hashString = merchantKey + "|" + txnId + "|" + request.getAmount() + "|" + productinfo + "|" +
                request.getFirstname() + "|" + request.getEmail() + "|||||||||||"+ merchantSalt;

        String hash = hashSha512(hashString);

        // Build the HTML auto-submit form
        StringBuilder html = new StringBuilder();
        html.append("<html><body onload='document.forms[\"payu_form\"].submit()'>\n");
        html.append("<form id='payu_form' method='post' action='")
                .append(HtmlUtils.htmlEscape(baseUrl+"/_payment")).append("'>\n");

        Map<String, String> params = new LinkedHashMap<>();
        params.put("key", merchantKey);
        params.put("txnid", txnId);
        params.put("amount", request.getAmount());
        params.put("productinfo", productinfo);
        params.put("firstname", request.getFirstname());
        params.put("email", request.getEmail());
        params.put("phone", request.getPhone());
        params.put("surl", surl);
        params.put("furl", furl);
        params.put("hash", hash);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            html.append("<input type='hidden' name='")
                    .append(HtmlUtils.htmlEscape(entry.getKey()))
                    .append("' value='")
                    .append(HtmlUtils.htmlEscape(entry.getValue()))
                    .append("'/>\n");
        }

        html.append("</form>\n");
        html.append("<p>Redirecting to PayU...</p>\n");
        html.append("</body></html>");

        return html.toString();
    }

    private PaymentInitResponse initiatePaymentRequest(PayURequest request) {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setTransactionAmount(BigDecimal.valueOf(Double.parseDouble(request.getAmount())));
        transaction.setCurrency("INR");
        transaction.setTransactionId(request.getOrderId());
        //capture other details as needed
//        transaction.setTransactionType(request.getpaymentMethod);
//        transaction.setDescription("Payment for Order ID: " + request.getOrderId());
//        transaction.setMerchantName(request.getGateway);
        transaction.setTransactionMode(String.valueOf(TransactionConstants.UPI));
        transaction.setTransactionStatus(String.valueOf(TransactionConstants.TransactionStatus.INITIATED));
         return transactionService.initiateTransaction(transaction);
    }

    public boolean verifyTransactionHash(Map<String, String> params) throws Exception {
        String receivedHash = params.get("hash");
        String txnId = params.get("txnid");
        String amount = params.get("amount");
        String productinfo = params.get("productinfo");
        String firstname = params.get("firstname");
        String email = params.get("email");

        String hashString = merchantSalt + "|" + params.get("status") + "|||||||||||" + email + "|" +
                firstname + "|" + productinfo + "|" + amount + "|" + txnId + "|" + merchantKey;

        String calculatedHash = PayUService.hashSha512(hashString);

        return calculatedHash.equals(receivedHash);
    }

    public static String hashSha512(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] digest = md.digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

