package com.avai.amp.prx.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.avai.amp.prx.R;
import com.avai.amp.prx.nav.OcnActivity;

public class OcnWhyRegister extends OcnActivity {

	Button register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.why_register);
		setTopNavToTitle(R.string.why_register_header, true, R.string.sign_up);
		register = (Button) findViewById(R.id.topNavRightButton);
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplication(), OcnRegistrationView.class);
				OcnWhyRegister.this.startActivity(myIntent);
			}
		});
	}

}
