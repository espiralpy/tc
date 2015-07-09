package com.avai.amp.prx.nav;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.avai.amp.prx.R;
import com.avai.amp.prx.bulletin.OcnBulletinFragmentView;
import com.avai.amp.prx.myfeed.OcnMyFeedFragmentView;
import com.avai.amp.prx.nav.BottomNavFragment.Tabs;
import com.avai.amp.prx.profile.OcnProfileFragmentView;
import com.avai.amp.prx.trophycase.OcnTrophyCaseFragmentView;

public class OcnMainView extends OcnActivity implements ContextSwitcher, CategoryChangeListener {
	
	private BottomNavFragment bottomNavFragment;

	private OcnMyFeedFragmentView myFeedFragment;
	private OcnTrophyCaseFragmentView trophyCaseFragment;
	private OcnBulletinFragmentView bulletinFragment;
	private OcnProfileFragmentView profileFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopNavToCategoriesAndOptions(this);
		setContentView(R.layout.main_view);
		bottomNavFragment = (BottomNavFragment) getSupportFragmentManager().findFragmentById(R.id.bottomNavFragment);
		bottomNavFragment.setContextSwitcher(this);
		myFeedFragment = new OcnMyFeedFragmentView();
		trophyCaseFragment = new OcnTrophyCaseFragmentView();
		bulletinFragment = new OcnBulletinFragmentView();
		profileFragment = new OcnProfileFragmentView();
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.main_view_fragment, trophyCaseFragment);
		transaction.hide(trophyCaseFragment);
		transaction.add(R.id.main_view_fragment, bulletinFragment);
		transaction.hide(bulletinFragment);
		transaction.add(R.id.main_view_fragment, profileFragment);
		transaction.hide(profileFragment);
		transaction.add(R.id.main_view_fragment, myFeedFragment);
		transaction.commit();
	}
	
	@Override
	public void onContextSwitch(Tabs newActivity) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.hide(getSupportFragmentManager().findFragmentById(R.id.main_view_fragment));
		Fragment newFrag = null;
		switch (newActivity) {
		case BULLETIN:
			newFrag = bulletinFragment;
			break;
		case MY_FEED:
			newFrag = myFeedFragment;
			break;
		case PROFILE:
			newFrag = profileFragment;
			break;
		case TROPHY_CASE:
			newFrag = trophyCaseFragment;
			break;
		}
		transaction.show(newFrag);
		//transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		//transaction.addToBackStack(null);
		transaction.commit();
	}

//	public void removeBottomNav() {
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		ft.hide(bottomNavFragment);
//		ft.commit();
//	}
//
//	public void addBottomNav() {
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		ft.show(bottomNavFragment);
//		ft.commit();
//	}
	

	public BottomNavFragment getBottomNavFragment() {
		return bottomNavFragment;
	}

	@Override
	public void onCategoryChanged() {
		// TODO Auto-generated method stub
		
	}

}
