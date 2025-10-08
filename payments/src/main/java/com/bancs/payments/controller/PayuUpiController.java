package com.bancs.payments.controller;

import com.bancs.payments.dto.UpiPaymentResponse;
import com.bancs.payments.services.PayUService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/upi")
@RequiredArgsConstructor
public class PayuUpiController {

    private final PayUService payuService;

    @Operation(summary = "Initiate UPI Payment via PayU Sandbox")
    @PostMapping("/pay")
    public UpiPaymentResponse pay(@RequestBody Map<String,String> request) throws Exception {
        return payuService.initiatePayment(request);
    }

    @Operation(summary = "Webhook callback for PayU (sandbox)")
    @PostMapping("/callback")
    public Object callback(@RequestBody Object payload) {
        // Just log / return payload for testing
        return payload;
    }
}
