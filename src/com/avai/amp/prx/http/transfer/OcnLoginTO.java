package com.avai.amp.prx.http.transfer;

public class OcnLoginTO {
	private String requestToken;
	private String email;
	private String password;

	public OcnLoginTO(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
