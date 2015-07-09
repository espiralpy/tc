package com.avai.amp.prx.facebook;

public class OcnFacebookTO {

	private String AccessToken;

	public OcnFacebookTO(String accessToken) {
		setAccessToken(accessToken);
	}

	public String getAccessToken() {
		return AccessToken;
	}

	public void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}
}
