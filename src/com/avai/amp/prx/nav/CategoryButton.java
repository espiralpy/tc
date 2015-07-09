package com.avai.amp.prx.nav;

import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.avai.amp.prx.lib.LibraryApplication;

public class CategoryButton {
	public Integer offImage;
	public Integer onImage;
	public String pref_name;
	public boolean state;
	public CategoryButton(Integer off, Integer on, String prefName) {
		offImage = off;
		onImage = on;
		pref_name = prefName;
		state = LibraryApplication.getContext().getSharedPreferences(LibraryApplication.PREFS, 0).getBoolean(
				prefName, false);
	}
	
	public boolean toggle() {
		Editor edit = LibraryApplication.getContext().getSharedPreferences(LibraryApplication.PREFS, 0).edit();
		if (state) {
			state = false;
		}
		else {
			state = true;
		}
		edit.putBoolean(pref_name, state);
		edit.commit();
		return state;
	}
	
	public void onClick(View arg0) {
		toggle();
		if (state) {
			((ImageView) arg0).setImageResource(onImage);
		} else {
			((ImageView) arg0).setImageResource(offImage);
		}
	}
}
