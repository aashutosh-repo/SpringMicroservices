package com.bancs.account.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity	
@Data
public class LineOfCredit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long locId;
	private String importerId;
	private String exporterId;
	private String shipmentId;
	private String bankName;
	private String advisingBankId;
	private String advisingBankType;
	private String locStatus;
	private String pymntStatus;
	private BigDecimal creditAmout;
	private String currency;
}
