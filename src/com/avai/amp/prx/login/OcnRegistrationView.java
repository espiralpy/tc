package com.avai.amp.prx.login;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avai.amp.prx.R;
import com.avai.amp.prx.http.HttpAmpService;
import com.avai.amp.prx.http.OAuthSession.RegistrationResponse;
import com.avai.amp.prx.http.transfer.OcnRegisterTO;
import com.avai.amp.prx.nav.OcnActivity;

public class OcnRegistrationView extends OcnActivity {

	private static final int PASSWORD_MIN_LENGTH = 8;

	Calendar birthday;
	Calendar today;
	String password;
	String rePassword;
	String screenName;
	String email;
	RegisterUserTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_account);
		setTopNavToTitle(R.string.create_account, true, R.string.sign_up);
		birthday = Calendar.getInstance();
		today = (Calendar) birthday.clone();
		task = new RegisterUserTask();
		setupRegisterButton();
		setupBirthdayButton();
	}

	private void loadContent() {
		EditText passwordET = (EditText) findViewById(R.id.passWordET);
		EditText repasswordET = (EditText) findViewById(R.id.repassWordET);
		EditText screenNameET = (EditText) findViewById(R.id.screenNameET);
		EditText emailET = (EditText) findViewById(R.id.emailAddressET);
		password = passwordET.getText().toString();
		rePassword = repasswordET.getText().toString();
		screenName = screenNameET.getText().toString().trim();
		email = emailET.getText().toString().trim();
	}

	private void setupBirthdayButton() {
		ImageButton birthdayButton = (ImageButton) findViewById(R.id.dateOfbirthbtn);
		birthdayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});

	}

	private void showDateDialog() {

		Dialog datePicker = new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				birthday.set(Calendar.YEAR, year);
				birthday.set(Calendar.MONTH, monthOfYear);
				birthday.set(Calendar.DATE, dayOfMonth);
				// setBirthdayText();
			}
		}, birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH), birthday.get(Calendar.DATE));

		datePicker.show();
	}

	// private void setBirthdayText(){
	// TextView birthdayText = (TextView) findViewById(R.id.dateOfBirthET);
	// SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
	// birthdayText.setText(format.format(birthday.getTime()));
	// }

	private void setupRegisterButton() {
		Button signUpButton = (Button) findViewById(R.id.topNavRightButton);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadContent();
				if (verifyEmail() && verifyPassword() && verifyBirthday() && verifyScreenName()) {
					// submit register web service and wait for response
					OcnRegisterTO regInfo = new OcnRegisterTO(screenName, email, password, birthday.getTime(), getString(R.string.ocn_app_id));
					if (task.getStatus() == AsyncTask.Status.FINISHED) {
						task = new RegisterUserTask();
					} else if (task.getStatus() == AsyncTask.Status.RUNNING) {
						return;
					}
					task.execute(regInfo);
				}
			}
		});
	}

	private boolean verifyEmail() {
		if (email != null) {
			int at = email.indexOf("@");
			int dot = email.lastIndexOf(".");
			if (at > 0 && dot > at) {
				return true;
			}
		}
		Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_LONG).show();
		return false;
	}

	private boolean verifyBirthday() {
		if (today.compareTo(birthday) <= 0) {
			Toast.makeText(this, R.string.birthdate_invalid, Toast.LENGTH_LONG).show();
			return false; // calendar hasn't been set
		}
		return true;
	}

	private boolean verifyScreenName() {
		if (screenName == null || screenName.length() < 4 || screenName.length() > 15) {
			Toast.makeText(this, R.string.username_too_short, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private boolean verifyPassword() {

		if (password == null || password.trim().length() < PASSWORD_MIN_LENGTH || password.trim().toLowerCase(Locale.US).equals(password.trim())) {
			Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_LONG).show();
			return false;
		}

		if (!password.equals(rePassword)) {
			Toast.makeText(this, R.string.matchPassword, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private class RegisterUserTask extends AsyncTask<OcnRegisterTO, Void, RegistrationResponse> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// show loading dialog
			findViewById(R.id.progressBarRegister).setVisibility(View.VISIBLE);
		}

		@Override
		protected RegistrationResponse doInBackground(OcnRegisterTO... params) {
			return UserService.getInstance().registerUser(params[0]);
		}

		@Override
		protected void onPostExecute(RegistrationResponse response) {
			super.onPostExecute(response);
			// hide loading dialog
			findViewById(R.id.progressBarRegister).setVisibility(View.GONE);

			if (response.isSuccess()) { // success
				Toast.makeText(OcnRegistrationView.this, R.string.register_success, Toast.LENGTH_LONG).show();
				finish();
				// Intent intent = new Intent(getApplication(), OcnSelectCategoryView.class);
				// startActivity(intent);
			} else {
				Toast.makeText(OcnRegistrationView.this, getString(R.string.registration_error) + " " + response.getError(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			findViewById(R.id.progressBarRegister).setVisibility(View.GONE);
			super.onCancelled();
		}
	}
}
