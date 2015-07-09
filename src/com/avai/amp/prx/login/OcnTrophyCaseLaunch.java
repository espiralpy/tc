package com.avai.amp.prx.login;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.avai.amp.prx.R;
import com.avai.amp.prx.http.OAuthSession;
import com.avai.amp.prx.lib.LibraryApplication;
import com.avai.amp.prx.nav.OcnActivity;
import com.avai.amp.prx.nav.TopNavFragment;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

//public class OcnLoginActivity extends AMPActivity {
public class OcnTrophyCaseLaunch extends OcnActivity {

	public static final String TAG = "OcnTrophyCaseLaunch";

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.trophycase_launch);
		setTopNavToTitle(R.string.trophy_case, true, R.string.login);
		setupClickEvents();
	}

	private void setupClickEvents() {
		LoginButton fbAuthButton = (LoginButton) findViewById(R.id.authButton);
		fbAuthButton.setReadPermissions(Arrays.asList("email", "user_about_me"));

		View loginButton = findViewById(R.id.topNavRightButton);
		View alreadyhave = findViewById(R.id.alreadyhave);
		OnClickListener loginListener = new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), OcnLoginView.class);
				startActivity(intent);
			}
		};
		loginButton.setOnClickListener(loginListener);
		alreadyhave.setOnClickListener(loginListener);
		Button signUpButton = (Button) findViewById(R.id.signUp);
		signUpButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), OcnRegistrationView.class);
				startActivity(intent);
			}
		});
		View whyRegister = findViewById(R.id.whyregister);
		whyRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), OcnWhyRegister.class);
				startActivity(intent);
			}
		});
	}

	protected void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in to Facebook...");

			// start prx task to log in user with facebook
			FacebookLoginTask fbLogin = new FacebookLoginTask();
			fbLogin.execute(session.getAccessToken());

			// start category launcher
			Intent intent = new Intent(getApplication(), OcnSelectCategoryView.class);
			startActivity(intent);
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out of Facebook...");
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// user hit back without logging in, treat as log out
		OAuthSession session = OAuthSession.getInstance();
		session.logout();
	}

	@Override
	public void onResume() {
		super.onResume();
		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	/**
	 * Contact prx server to log user in via facebook
	 * 
	 */
	private class FacebookLoginTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			UserService userService = UserService.getInstance();
			String fbToken = params[0];
			boolean result = userService.loginUser(fbToken);
			Log.i(TAG, "PRX Facebook login success: " + result);
			return null;
		}
	}
}
