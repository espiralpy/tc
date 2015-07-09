package com.avai.amp.prx.login;

import com.avai.amp.prx.R;
import com.avai.amp.prx.nav.OcnActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OcnLoginView extends OcnActivity {

	public static final String TAG = "OcnLoginView";
	LoginTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		setTopNavToTitle(R.string.hey_there, true, R.string.login);
		task = new LoginTask();

		// buttons instantiate
		Button loginButton = (Button) findViewById(R.id.topNavRightButton);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String username = ((EditText) findViewById(R.id.username_entry)).getText().toString();
				String password = ((EditText) findViewById(R.id.password_entry)).getText().toString();
				// validate fields
				if (!validateLoginFields(username, password)) {
					return;
				}
				if (task.getStatus() == AsyncTask.Status.FINISHED) {
					task = new LoginTask();
				} else if (task.getStatus() == AsyncTask.Status.RUNNING) {
					return;
				}
				task.execute(username, password);
			}
		});
		TextView signUp = (TextView) findViewById(R.id.needAnAccount);
		signUp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplication(), OcnRegistrationView.class);
				OcnLoginView.this.startActivity(myIntent);
			}
		});
	}

	/**
	 * Validates login fields. displays toast if there's a problem
	 * @param dialog login dialog
	 * @return true if validate, false otherwise
	 */
	private boolean validateLoginFields(String username, String password) {

		boolean valid = true;

		// validate fields
		if (username == null || username.trim().length() == 0 || username.indexOf("@") <= 0) {
			valid = false;
			Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_LONG).show();
		} else if (password == null || password.trim().length() < 8 || password.trim().toLowerCase().equals(password.trim())) {
			valid = false;
			Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_LONG).show();
		}
		return valid;
	}

	private class LoginTask extends AsyncTask<String, Void, Boolean> {

		UserService userService;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// show loading dialog
			findViewById(R.id.progressBarLogin).setVisibility(View.VISIBLE);
		}

		/**
		 * Calls login service, assumes fields have already been validated
		 * @param username
		 * @return
		 */
		private boolean loginUser(String username, String password) {
			userService = UserService.getInstance();
			boolean loggedIn = userService.loginUser(username, password);
			if (loggedIn) {
				Log.i(TAG, "login in successful");
				// send device info for c2dm
				// registerDevice();
				// start location updates
				// startLocationUpdates();
			}

			return loggedIn;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String username = params[0].trim();
			String password = params[1].trim();
			return loginUser(username, password);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			// hide loading dialog
			// TODO
			findViewById(R.id.progressBarLogin).setVisibility(View.GONE);

			if (result) { // login successful
				setResult(RESULT_OK);
				// finish();
				Intent intent = new Intent(getApplication(), OcnSelectCategoryView.class);
				startActivity(intent);
			} else { // login unsuccessful
				String toastMessage;
				if (userService.getErrorMessage() != null) {
					toastMessage = userService.getErrorMessage();
				} else {
					toastMessage = getString(R.string.login_problem_message);
				}
				Toast.makeText(OcnLoginView.this, toastMessage, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			findViewById(R.id.progressBarLogin).setVisibility(View.GONE);
			super.onCancelled();
		}
	}

}
