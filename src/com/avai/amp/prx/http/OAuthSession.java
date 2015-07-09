package com.avai.amp.prx.http;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import com.avai.amp.prx.lib.LibraryApplication;
import com.avai.amp.prx.facebook.OcnFacebookTO;
import com.avai.amp.prx.http.HttpAmpService;
//import com.avai.amp.pbn.PBNApplication;
import com.avai.amp.prx.R;
import com.avai.amp.prx.http.transfer.OcnLoginResponseTO;
import com.avai.amp.prx.http.transfer.OcnLoginTO;
import com.avai.amp.prx.http.transfer.OcnRegisterTO;
import com.avai.amp.prx.http.transfer.RequestTokenTO;

public class OAuthSession {
	private String requestToken;
	private String accessToken;
	private String refreshToken;
	private long accessTokenIssueTime;
	private int accessTokenExpireSeconds;
	private String userId;
	private String pass;
	private boolean validLoginState;

	private final String PREFS_USERID = "UserId";
	private final String PREFS_PW = "Password";
	private static final String OAUTH_PREFS = "OAuthPrefs";
	private static final String ACCESS_TOKEN_ISSUE_TIME_PREF = "AccessTokenIssueTime";
	private static final String ACCESS_TOKEN_EXPIRE_SECONDS_PREF = "AccessTokenExpireSeconds";
	private static final String ACCESS_TOKEN_PREF = "AccessToken";
	private static final String REFRESH_TOKEN_PREF = "RefreshToken";
	private static final String TAG = "OAuthSession";

	private static OAuthSession session;

	private OAuthSession() {

	}

	public void setLoginState(boolean state) {
		validLoginState = state;
	}

	public static OAuthSession getInstance() {
		if (session == null) {
			session = new OAuthSession();
		}
		return session;
	}

	public void getRequestToken() {
		String requestUrl = LibraryApplication.getContext().getString(R.string.ocn_host) + LibraryApplication.getContext().getString(R.string.uri_requesttoken);
		HttpAmpService httpService = new HttpAmpService(requestUrl);
		RequestTokenTO token = httpService.getJsonForGet(RequestTokenTO.class);
		if (token != null)
			requestToken = token.getRequestToken();
	}

	public void logout() {
		forgetTokenInfo();
		userId = null;
		SharedPreferences settings = LibraryApplication.getContext().getSharedPreferences(OAUTH_PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(PREFS_USERID);
		editor.remove(PREFS_PW);
		editor.commit();
		validLoginState = false;
		// TODO take user to login screen again?
	}

	public RegistrationResponse registerUser(Context ctx, OcnRegisterTO registrationInfo) {
		String url = ctx.getString(R.string.ocn_host) + ctx.getString(R.string.uri_register);
		HttpAmpService httpService = new HttpAmpService(url);
		httpService.getJsonForPost(registrationInfo);

		// save username and password saved prefs
		if (httpService.isSuccess()) {
			SharedPreferences settings = ctx.getSharedPreferences(OAUTH_PREFS, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(PREFS_USERID, registrationInfo.getEmailAddress());
			editor.putString(PREFS_PW, registrationInfo.getPassword());
			editor.commit();
			return new RegistrationResponse(true, null);
		} else {
			// TODO registration failed
			return new RegistrationResponse(false, httpService.getStatusCode() + " " + httpService.getReasonPhrase());
		}
	}

	public LoginResponse logIn(String username, String password) {
		if (requestToken == null || requestToken.trim().length() == 0) {
			return new LoginResponse(null, "Could not access network");
		}
		String un = username, pw = password;
		if (username == null && password == null) {
			// use defaults to login
			SharedPreferences settings = LibraryApplication.getContext().getSharedPreferences(OAUTH_PREFS, 0);
			un = settings.getString(PREFS_USERID, null);
			pw = settings.getString(PREFS_PW, null);
			if (un == null && pw == null)
				return null;
		}
		String url = LibraryApplication.getContext().getString(R.string.ocn_host) + LibraryApplication.getContext().getString(R.string.ocn_app_id) + LibraryApplication.getContext().getString(R.string.uri_login_end);

		HttpAmpService httpService = new HttpAmpService(url);
		OcnLoginTO loginTO = new OcnLoginTO(un, pw);
		loginTO.setRequestToken(requestToken);

		Date requestTime = new Date();
		OcnLoginResponseTO response = (OcnLoginResponseTO) httpService.getJsonForPost(loginTO, OcnLoginResponseTO.class);

		if (response != null) {
			pass = pw;
			validLoginState = true;
			saveTokenInfo(requestTime.getTime(), response);
			return new LoginResponse(response.getUserId(), response.getError());
		}

		return null;

	}

	public LoginResponse logInWithFacebook(String fbAccessToken) {
		if (requestToken == null || requestToken.trim().length() == 0) {
			return new LoginResponse(null, "Could not access network");
		}

		String url = LibraryApplication.getContext().getString(R.string.ocn_host) + LibraryApplication.getContext().getString(R.string.uri_fb) + LibraryApplication.getContext().getString(R.string.ocn_app_id) + LibraryApplication.getContext().getString(R.string.uri_login_end);

		HttpAmpService httpService = new HttpAmpService(url);
		OcnFacebookTO fbTO = new OcnFacebookTO(fbAccessToken);

		Date requestTime = new Date();
		OcnLoginResponseTO response = (OcnLoginResponseTO) httpService.getJsonForPost(fbTO, OcnLoginResponseTO.class);

		if (response != null) {
			pass = "";
			validLoginState = true;
			saveTokenInfo(requestTime.getTime(), response);
			return new LoginResponse(response.getUserId(), response.getError());
		}
		return null;

	}

	public String getAccessToken() {
		long accessTokenExpireMillis = accessTokenIssueTime + accessTokenExpireSeconds * 1000;
		if (accessToken == null) {
			getTokenInfo();
		}

		if (accessToken == null || accessTokenExpireMillis < (new Date()).getTime()) { // either don't have a token or token has expired
			if (refreshToken != null && refreshToken.trim().length() > 0) {
				refreshAccessToken();
			}
		}
		return accessToken;
	}

	public String getRefreshToken() {
		if (refreshToken == null) {
			getTokenInfo();
		}

		return refreshToken;
	}

	private void getTokenInfo() {
		SharedPreferences settings = LibraryApplication.getContext().getSharedPreferences(OAUTH_PREFS, 0);
		accessTokenIssueTime = settings.getLong(ACCESS_TOKEN_ISSUE_TIME_PREF, 0);
		accessToken = settings.getString(ACCESS_TOKEN_PREF, null);
		accessTokenExpireSeconds = settings.getInt(ACCESS_TOKEN_EXPIRE_SECONDS_PREF, 0);
		refreshToken = settings.getString(REFRESH_TOKEN_PREF, null);
		userId = settings.getString(PREFS_USERID, null);
	}

	private void saveTokenInfo(long requestTime, OcnLoginResponseTO response) {
		if (response == null) {
			return;
		}
		accessTokenIssueTime = requestTime;
		accessToken = response.getAccessToken();
		refreshToken = response.getRefreshToken();
		accessTokenExpireSeconds = response.getExpiresIn();
		userId = response.getUserId();
		SharedPreferences settings = LibraryApplication.getContext().getSharedPreferences(OAUTH_PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(ACCESS_TOKEN_ISSUE_TIME_PREF, accessTokenIssueTime);
		editor.putString(ACCESS_TOKEN_PREF, accessToken);
		editor.putInt(ACCESS_TOKEN_EXPIRE_SECONDS_PREF, accessTokenExpireSeconds);
		editor.putString(REFRESH_TOKEN_PREF, refreshToken);
		editor.putString(PREFS_USERID, userId);
		editor.putString(PREFS_PW, pass);
		editor.commit();
	}

	private void forgetTokenInfo() {
		accessTokenIssueTime = 0;
		accessTokenExpireSeconds = 0;
		accessToken = null;
		refreshToken = null;

		SharedPreferences settings = LibraryApplication.getContext().getSharedPreferences(OAUTH_PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(ACCESS_TOKEN_ISSUE_TIME_PREF);
		editor.remove(ACCESS_TOKEN_PREF);
		editor.remove(ACCESS_TOKEN_EXPIRE_SECONDS_PREF);
		editor.remove(REFRESH_TOKEN_PREF);
		editor.commit();
		validLoginState = false;
	}

	public boolean refreshAccessToken() {
		if (refreshToken == null || refreshToken.trim().length() == 0) {
			forgetTokenInfo();
			return false;
		}
		Date requestTime = new Date();
		String refreshUrl = LibraryApplication.getContext().getString(R.string.ocn_host) + "oauth/refreshtoken?refreshtoken=" + refreshToken;
		HttpAmpService service = new HttpAmpService(refreshUrl);
		OcnLoginResponseTO response = (OcnLoginResponseTO) service.getJsonForGet(OcnLoginResponseTO.class);
		String newAccessToken = response.getAccessToken();
		if (newAccessToken == null || newAccessToken.trim().length() == 0) {
			// bad access token, user has been logged out
			forgetTokenInfo();
			return false;
		} else {
			saveTokenInfo(requestTime.getTime(), response);
			validLoginState = true;
			return true;
		}

	}

	public String getUserId() {
		if (userId == null) {
			getTokenInfo();
		}
		if (userId != null && userId.trim().length() == 0) {
			userId = null;
		}
		return userId;
	}

	public boolean haveValidLogin() {
		return validLoginState;
	}

	public static class LoginResponse {
		String userId;
		String errorMessage;

		public LoginResponse(String userId, String errorMessage) {
			super();
			this.userId = userId;
			this.errorMessage = errorMessage;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

	}

	public class RegistrationResponse {
		boolean success;
		String error;

		public RegistrationResponse(boolean success, String error) {
			this.success = success;
			this.error = error;
		}

		public boolean isSuccess() {
			return success;
		}

		public String getError() {
			return error;
		}
	}
}
