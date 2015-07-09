package com.avai.amp.prx.login;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

//import com.avai.amp.prx.lib.Factory;
//import com.avai.amp.prx.lib.DatabaseService;
import com.avai.amp.prx.http.HttpAmpService;
import com.avai.amp.prx.http.OAuthSession;
import com.avai.amp.prx.http.OAuthSession.LoginResponse;
import com.avai.amp.prx.http.OAuthSession.RegistrationResponse;
import com.avai.amp.prx.http.transfer.OcnRegisterTO;
import com.avai.amp.prx.lib.LibraryApplication;
import com.avai.amp.prx.R;

public class UserService {
	private final String REGISTER_SERVICE = "Loginservice.svc/user/create/";

	private String errorMessage;

	private final String TAG = "UserService";

	private static UserService svc;

	private UserService() {
		super();
	}

	public static UserService getInstance() {
		if (svc == null) {
			svc = new UserService();
		}
		return svc;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Registers a user with the service. This is a test service.
	 * @param username username to register
	 * @param password password for the user
	 * @return true if user successfully registered, false otherwise
	 */
	public RegistrationResponse registerUser(OcnRegisterTO registrationInfo) {
		// send username and password to webservice
		OAuthSession session = OAuthSession.getInstance();

		RegistrationResponse r = session.registerUser(LibraryApplication.getContext(), registrationInfo);
		if (!r.isSuccess()) {
			Log.i(TAG, "Registration error: " + r.getError());
		}
		return r;
	}

	/**
	 * Checks if user is logged in to the service
	 * @return true if user is logged in, false otherwise
	 */
	public boolean isUserLoggedIn() {
		OAuthSession session = OAuthSession.getInstance();
		String userId = session.getUserId();
		if (userId == null || userId.trim().length() == 0 || !session.haveValidLogin()) {
			return false;
		}
		return true;
	}

	/**
	 * try to log in automatically from shared preferences. If this doesn't work, you must allow the user to log in manually
	 * @return
	 */
	public boolean loginSavedUserFromSharedPreferences() {
		// log in with null data, program will try to fill with saved user data
		return loginUser(false, null, null, null);
	}

	/**
	 * Logs a user in
	 * @param username username to login
	 * @param password password to login
	 * @return
	 */
	public boolean loginUser(String username, String password) {
		return loginUser(false, username, password, null);
	}

	public boolean loginUser(String fbAccessToken) {
		return loginUser(true, null, null, fbAccessToken);
	}

	private boolean loginUser(boolean useFacebook, String username, String password, String fbAccessToken) {

		OAuthSession session = OAuthSession.getInstance();

		// get request token
		session.getRequestToken();

		LoginResponse response;
		// log in
		if (useFacebook) {
			response = session.logInWithFacebook(fbAccessToken);
		} else {
			response = session.logIn(username, password);
		}

		if (response == null) {
			return false;
		}

		String userId = response.getUserId();
		if (userId != null && userId.trim().length() > 0) {

			// download and sync user data from server
			syncRemoteUserData();

			return true;
		} else {
			this.errorMessage = response.getErrorMessage();
		}

		return false;
	}

	private void syncRemoteUserData() {
		// call the content syncing methods that get the latest user data from the server and put it into the DB
		// TODO
	}
}
