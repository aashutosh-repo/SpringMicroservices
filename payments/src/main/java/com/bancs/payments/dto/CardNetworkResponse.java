package com.bancs.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardNetworkResponse {
    private boolean approved;
    private String authCode;
    private String declineReason;
}
