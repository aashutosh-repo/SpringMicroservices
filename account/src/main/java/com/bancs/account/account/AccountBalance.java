package com.bancs.account.account;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class AccountBalance {
	@EmbeddedId
	protected AccountBalancePk acc_bal_id; //PK
	protected LocalDate balance_date;
	protected int txn_type;
	protected long interest_rate;
	protected BigDecimal prncpl_amt;
	protected BigDecimal intrst_rate;
	protected BigDecimal total_bal;
	protected BigDecimal opening_bal;
	protected BigDecimal available_balance;
}
