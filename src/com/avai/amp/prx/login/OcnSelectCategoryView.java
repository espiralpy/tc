package com.avai.amp.prx.login;

import com.avai.amp.prx.R;
import com.avai.amp.prx.lib.LibraryApplication;
import com.avai.amp.prx.login.OcnSelectionImageAdapter;
import com.avai.amp.prx.nav.OcnActivity;
import com.avai.amp.prx.nav.OcnMainView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

public class OcnSelectCategoryView extends OcnActivity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_category);
		setTopNavToTitle(R.string.select_category, true, R.string.next);
		GridView ocnSelectionGridView = (GridView) findViewById(R.id.gridSelectCatagory);

		ocnSelectionGridView.setAdapter(new OcnSelectionImageAdapter(this));
		Button next = (Button) findViewById(R.id.topNavRightButton);
		next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplication(), OcnMainView.class);
				OcnSelectCategoryView.this.startActivity(myIntent);
				finish();
			}
		});
	}
}