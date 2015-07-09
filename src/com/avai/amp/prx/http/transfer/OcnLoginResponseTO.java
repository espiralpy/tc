package com.avai.amp.prx.http.transfer;

public class OcnLoginResponseTO {
	private String accessToken;
	private int expires;
	private String refreshToken;
	private boolean success;
	private String userId;
	private String error;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "OcnLoginResponseTO [accessToken=" + accessToken + ", expiresIn=" + expires + ", refreshToken=" + refreshToken + ", success=" + success + ", userId=" + userId + ", error=" + error + "]";
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expires;
	}

	public void setExpiresIn(int expiresIn) {
		this.expires = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
