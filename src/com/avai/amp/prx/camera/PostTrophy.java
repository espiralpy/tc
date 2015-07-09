package com.avai.amp.prx.camera;

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
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

//public class OcnLoginActivity extends AMPActivity {
public class PostTrophy extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_trophy);
	}

}
