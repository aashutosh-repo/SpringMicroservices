package com.bancs.account.account;

import com.bancs.account.audit.AuditInfo;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@ToString 
public class Account extends AuditInfo {
	@EmbeddedId
	protected AccountPk accountId; //pk
	private String internalAcntNumber;
	protected int account_status;
	protected String accountNumber;
	protected LocalDate account_open_dt;
	protected String currency;
	protected String custId;
	protected int cus_type;
	protected int npa_status;
	protected int min_bal;
	protected Date last_withdrawal_dt;
	protected BigDecimal available_balance;
	protected String owner_name;
	protected int atm_req_flag;
	protected int cheq_req_flag;
	protected int sms_req_flag;
	protected LocalDate clsr_dt;
	protected String clsr_reason;
}
