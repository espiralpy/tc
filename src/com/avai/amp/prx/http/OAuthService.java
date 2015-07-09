package com.avai.amp.prx.http;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.avai.amp.prx.http.HttpAmpService;

public class OAuthService extends HttpAmpService {

	private static final String TAG = "OAuthService";
	private OAuthSession session;

	public OAuthService(String url, String contentType, boolean useGzip, boolean setTimeout) {
		super(url, contentType, useGzip, setTimeout);
		session = OAuthSession.getInstance();
	}

	public OAuthService(String url) {
		super(url);
		session = OAuthSession.getInstance();
	}

	@Override
	public void sendRequestOnly(String body, String contentType, int methodType) {
		// check accessToken
		String accessToken = session.getAccessToken();
		if (accessToken == null || accessToken.trim().length() == 0) {
			if (!session.refreshAccessToken()) { // have no access token and can't refresh
				return;
			}
		}
		// send request and stores response on this object
		super.sendRequestOnly(body, contentType, methodType);
		// check response for failure, handle failure
		int statusCode = getStatusCode();
		if (statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN) { // unauthorized, get new access token and try again
			if (session.refreshAccessToken()) {
				super.sendRequestOnly(body, contentType, methodType);
			} else { // problem refreshing
				session.logout();
			}

		}

		statusCode = getStatusCode();
		if (statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN) { // there is still an authorization problem, force logout
			session.logout();
		}

	}

	@Override
	public void addRequestHeaders(HttpUriRequest request, String contentType) {
		super.addRequestHeaders(request, contentType);
		// add oauth header to standard request headers
		request.addHeader("Authorization", "OAuth " + session.getAccessToken()); // getAccessToken forces refresh if necessary
	}

	public boolean haveAccessToken() {
		String accessToken = session.getAccessToken();

		return (accessToken != null && accessToken.trim().length() > 0);
	}

}
