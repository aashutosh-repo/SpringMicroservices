package com.bancs.account.account;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class AccountBalancePk implements Serializable{
	private int account_id;
	protected int account_type;
	protected int balance_seq_id;
	
}
