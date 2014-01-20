package edu.mt.sapna.utils;


public class TokenImpl implements Token {

	private String token;
	
	public TokenImpl(String token) {
		this.token = token;
	}
	
	public String getValue() {
		return token;
	}	

}
