package com.bancs.payments.error;

public class CustomErrorMessage extends RuntimeException{

	public CustomErrorMessage(String message) {
		super(message);
	}
	public CustomErrorMessage(String message, String err2) {
		super(message+ err2);
	}

	public CustomErrorMessage(String message, String err2, String err3) {
		super(message+ err2 + err3);
	}
}
