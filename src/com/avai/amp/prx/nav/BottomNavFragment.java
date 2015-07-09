package com.avai.amp.prx.nav;

import com.avai.amp.prx.R;
import com.avai.amp.prx.camera.OcnCameraView;
import com.avai.amp.prx.lib.LibraryApplication;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class BottomNavFragment extends Fragment {

	public enum Tabs {
		MY_FEED, TROPHY_CASE, BULLETIN, PROFILE
	}

	private Tabs selectedTab;
	private ContextSwitcher callback;
	private boolean isOpen = true;
	private boolean isToggling = false;
	private View layoutRoot;
	private int toggleDeltaPixels;
	private static final float TOGGLE_VISIBLE_DP = 25.0f;
	private static final String TAG = "BottomNavFragment";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_bottom_bar_fragment, container, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		selectedTab = Tabs.MY_FEED;
		highlightTab(Tabs.MY_FEED, true);
		addButtonOnClickEvents();
		layoutRoot = getView().findViewById(R.id.bottomNavBar);
		toggleDeltaPixels = (int) LibraryApplication.convertDpToPixel(TOGGLE_VISIBLE_DP, getActivity());
	}

	private void addButtonOnClickEvents() {
		ImageView cam = (ImageView) getView().findViewById(R.id.bottomNavCamera);
		cam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getView().getContext(), OcnCameraView.class);
				startActivity(intent);
			}
		});
		ImageView mf = (ImageView) getView().findViewById(R.id.bottomNavMyFeed);
		mf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (selectedTab != Tabs.MY_FEED) {
					switchTab(Tabs.MY_FEED);
					callback.onContextSwitch(Tabs.MY_FEED);
				}
			}
		});
		ImageView tc = (ImageView) getView().findViewById(R.id.bottomNavTrophyCase);
		tc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (selectedTab != Tabs.TROPHY_CASE) {
					switchTab(Tabs.TROPHY_CASE);
					callback.onContextSwitch(Tabs.TROPHY_CASE);
				}
			}
		});
		ImageView b = (ImageView) getView().findViewById(R.id.bottomNavBulletin);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (selectedTab != Tabs.BULLETIN) {
					switchTab(Tabs.BULLETIN);
					callback.onContextSwitch(Tabs.BULLETIN);
				}
			}
		});
		ImageView p = (ImageView) getView().findViewById(R.id.bottomNavProfile);
		p.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (selectedTab != Tabs.PROFILE) {
					switchTab(Tabs.PROFILE);
					callback.onContextSwitch(Tabs.PROFILE);
				}
			}
		});
		ImageView hider = (ImageView) getView().findViewById(R.id.bottomNavCloseButton);
		hider.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!isToggling)
					toggleHide();
			}
		});
	}
	
	public void switchTab(Tabs newTab) {
		highlightTab(selectedTab, false);
		selectedTab = newTab;
		highlightTab(selectedTab, true);
	}

	private void highlightTab(Tabs tab, boolean turnOn) {
		switch (tab) {
		case BULLETIN:
			if (turnOn)
				((ImageView) getView().findViewById(R.id.bottomNavBulletin)).setImageDrawable(getResources().getDrawable(R.drawable.bulletin_on));
			else
				((ImageView) getView().findViewById(R.id.bottomNavBulletin)).setImageDrawable(getResources().getDrawable(R.drawable.bulletin_off));
			break;
		case MY_FEED:
			if (turnOn)
				((ImageView) getView().findViewById(R.id.bottomNavMyFeed)).setImageDrawable(getResources().getDrawable(R.drawable.myfeed_on));
			else
				((ImageView) getView().findViewById(R.id.bottomNavMyFeed)).setImageDrawable(getResources().getDrawable(R.drawable.myfeed_off));
			break;
		case PROFILE:
			if (turnOn)
				((ImageView) getView().findViewById(R.id.bottomNavProfile)).setImageDrawable(getResources().getDrawable(R.drawable.profile_on));
			else
				((ImageView) getView().findViewById(R.id.bottomNavProfile)).setImageDrawable(getResources().getDrawable(R.drawable.profile_off));
			break;
		case TROPHY_CASE:
			if (turnOn)
				((ImageView) getView().findViewById(R.id.bottomNavTrophyCase)).setImageDrawable(getResources().getDrawable(R.drawable.trophycase_on));
			else
				((ImageView) getView().findViewById(R.id.bottomNavTrophyCase)).setImageDrawable(getResources().getDrawable(R.drawable.trophycase_off));
			break;
		}
	}

	void setContextSwitcher(ContextSwitcher callback) {
		this.callback = callback;
	}

	void toggleHide() {
		isToggling = true;
		TranslateAnimation anim = null;
		int yDelta = layoutRoot.getHeight() - toggleDeltaPixels;
		int replacementImage;
		if (isOpen) {
			anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, yDelta);
			replacementImage = R.drawable.arrow_up_state;
		} else {
			getView().findViewById(R.id.bottomNavCamera).setVisibility(View.VISIBLE);
			anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -yDelta);
			replacementImage = R.drawable.arrow_down_state;
		}
		isOpen = !isOpen;
		anim.setAnimationListener(collapseListener);
		anim.setDuration(150);
		// anim.setInterpolator(new AccelerateInterpolator(1.0f));
		layoutRoot.startAnimation(anim);
		((ImageView) getView().findViewById(R.id.bottomNavCloseButton)).setImageDrawable(layoutRoot.getResources().getDrawable(replacementImage));
	}

	Animation.AnimationListener collapseListener = new Animation.AnimationListener() {
		public void onAnimationEnd(Animation a) {
			layoutRoot.clearAnimation();
			int yDelta = layoutRoot.getHeight() - toggleDeltaPixels;
			if (isOpen) {
				layoutRoot.offsetTopAndBottom(-yDelta);
			} else {
				getView().findViewById(R.id.bottomNavCamera).setVisibility(View.INVISIBLE);
				layoutRoot.offsetTopAndBottom(yDelta);
			}
			isToggling = false;
		}

		@Override
		public void onAnimationRepeat(Animation a) {
		}

		@Override
		public void onAnimationStart(Animation a) {
		}
	};

}
