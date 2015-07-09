package com.avai.amp.prx.nav;

import com.avai.amp.prx.R;
import com.avai.amp.prx.lib.LibraryApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopNavFragment extends Fragment {

	public enum Layouts {
		GRID_VIEW, LIST_VIEW
	}

	private static final String TAG = "TopNavFragment";
	private TextView mTitle;
	private Button mRButton;
	private LinearLayout mCategories;
	private ImageView mPrNav;
	private LinearLayout mLayoutOpts;
	
	private CategoryButton[] topNavCategories;
	private CategoryChangeListener categoryChangeListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.tab_top_bar_fragment, container, false);
		mTitle = (TextView) root.findViewById(R.id.topNavTitle);
		mRButton = (Button) root.findViewById(R.id.topNavRightButton);
		mCategories = (LinearLayout) root.findViewById(R.id.topNavCategoryButtons);
		mPrNav = (ImageView) root.findViewById(R.id.topNavPrPathButton);
		mLayoutOpts = (LinearLayout) root.findViewById(R.id.topNavViewModes);
		return root;
	}

	public void addTitle(int resId) {
		mCategories.setVisibility(View.GONE);
		mTitle.setText(resId);
		mTitle.setVisibility(View.VISIBLE);
	}

	public void addRightButton(int stringResId) {
		mPrNav.setVisibility(View.GONE);
		mRButton.setText(stringResId);
		mRButton.setVisibility(View.VISIBLE);
	}

	public void addCategories(CategoryChangeListener callback) {
		mTitle.setVisibility(View.GONE);
		topNavCategories = new CategoryButton[] {
				new CategoryButton(
						R.drawable.hunting_on, //this is BACKWARDS!! check with devs about it
						R.drawable.hunting_off, 
						LibraryApplication.PREFS_VIEW_HUNTING),
				new CategoryButton(
						R.drawable.fishing_on, 
						R.drawable.fishing_off, 
						LibraryApplication.PREFS_VIEW_FISHING),
				new CategoryButton(
						R.drawable.wildlife_on, 
						R.drawable.wildlife_off, 
						LibraryApplication.PREFS_VIEW_WILDLIFE),
				new CategoryButton(
						R.drawable.scenic_on, 
						R.drawable.scenic_off, 
						LibraryApplication.PREFS_VIEW_SCENIC),
		};
		categoryChangeListener = callback;
		
		ImageView hunting = (ImageView) mCategories.findViewById(R.id.topNavHunting);
		if (topNavCategories[0].state) hunting.setImageResource(topNavCategories[0].onImage);
		else hunting.setImageResource(topNavCategories[0].offImage);
		hunting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				topNavCategories[0].onClick(v);
				getCategoryChangeListener().onCategoryChanged();
			}
		});
		
		ImageView fishing = (ImageView) mCategories.findViewById(R.id.topNavFishing);
		if (topNavCategories[1].state) fishing.setImageResource(topNavCategories[1].onImage);
		else fishing.setImageResource(topNavCategories[1].offImage);
		fishing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				topNavCategories[1].onClick(v);
				getCategoryChangeListener().onCategoryChanged();
			}
		});
		
		ImageView wildlife = (ImageView) mCategories.findViewById(R.id.topNavWildlife);
		if (topNavCategories[2].state) wildlife.setImageResource(topNavCategories[2].onImage);
		else wildlife.setImageResource(topNavCategories[2].offImage);
		wildlife.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				topNavCategories[2].onClick(v);
				getCategoryChangeListener().onCategoryChanged();
			}
		});
		
		ImageView scenic = (ImageView) mCategories.findViewById(R.id.topNavScenic);
		if (topNavCategories[3].state) scenic.setImageResource(topNavCategories[3].onImage);
		else scenic.setImageResource(topNavCategories[3].offImage);
		scenic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				topNavCategories[3].onClick(v);
				getCategoryChangeListener().onCategoryChanged();
			}
		});
		
		mCategories.setVisibility(View.VISIBLE);
	}

	public void addRightPrNav() {
		mRButton.setVisibility(View.GONE);
		mPrNav.setVisibility(View.VISIBLE);
	}

	public void addLayoutOptions() {
		mLayoutOpts.setVisibility(View.VISIBLE);
	}

	public void removeLayoutOptions() {
		mLayoutOpts.setVisibility(View.GONE);
	}

	public CategoryChangeListener getCategoryChangeListener() {
		return categoryChangeListener;
	}
}
