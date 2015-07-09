package com.avai.amp.prx.nav;

import com.avai.amp.prx.R;
import com.avai.amp.prx.lib.LibraryApplication;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class OcnActivity extends FragmentActivity {
	
	private static final String TAG = "OcnActivity";
	private TopNavFragment topNavFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LibraryApplication.setContext(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.setContentView(R.layout.nav_bar);
		topNavFragment = (TopNavFragment) getSupportFragmentManager().findFragmentById(R.id.topNavFragment);
	}

	 @Override
	 //formats content view so that it is placed within frame of navigation
	 public void setContentView(int layoutID) {
		 LayoutInflater inflator = (LayoutInflater)getSystemService(
		 Context.LAYOUT_INFLATER_SERVICE);
		 View myView = inflator.inflate(layoutID, null);
		 FrameLayout myFeedContent = (FrameLayout) findViewById(R.id.content_fragment);
		 myFeedContent.addView(myView);
	 }

	public TopNavFragment getTopNavFragment() {
		return topNavFragment;
	}

	public void setTopNavToTitle(int titleId, boolean hasButton, int buttonTitleId) {
		topNavFragment.removeLayoutOptions();
		topNavFragment.addTitle(titleId);
		if (hasButton)
			topNavFragment.addRightButton(buttonTitleId);
		else
			topNavFragment.addRightPrNav();
	}
	
	public void setTopNavToCategoriesAndOptions(CategoryChangeListener categoryCallback) {
		topNavFragment.addCategories(categoryCallback);
		topNavFragment.addRightPrNav();
	}
	
	public void showOptionsMenu(int photoId) {
		 LayoutInflater inflator = (LayoutInflater)getSystemService(
		 Context.LAYOUT_INFLATER_SERVICE);
		 View myView = inflator.inflate(R.layout.options_menu, null);
		 FrameLayout myFeedContent = (FrameLayout) findViewById(R.id.content_fragment);
		 myFeedContent.addView(myView);
	}

}
