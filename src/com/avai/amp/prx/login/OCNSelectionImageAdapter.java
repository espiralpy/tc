package com.avai.amp.prx.login;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.avai.amp.prx.R;
import com.avai.amp.prx.lib.LibraryApplication;
import com.avai.amp.prx.nav.CategoryButton;

public class OcnSelectionImageAdapter extends BaseAdapter {

	private Context context;
	
	private static CategoryButton[] myInfo;

	public OcnSelectionImageAdapter(Context context) {
		this.context = context;
		myInfo = new CategoryButton[] {
				new CategoryButton(
						R.drawable.ic_select_catagory_hunting, 
						R.drawable.ic_select_catagory_hunting_on, 
						LibraryApplication.PREFS_VIEW_HUNTING),
				new CategoryButton(
						R.drawable.ic_select_catagory_fishing, 
						R.drawable.ic_select_catagory_fishing_on, 
						LibraryApplication.PREFS_VIEW_FISHING),
				new CategoryButton(
						R.drawable.ic_select_catagory_wildlife, 
						R.drawable.ic_select_catagory_wildlife_on, 
						LibraryApplication.PREFS_VIEW_WILDLIFE),
				new CategoryButton(
						R.drawable.ic_select_catagory_scenic, 
						R.drawable.ic_select_catagory_scenic_on, 
						LibraryApplication.PREFS_VIEW_SCENIC),
		};
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView = null;

		if (convertView == null) {

			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(30, 30, 30, 30);
		} else {
			imageView = (ImageView) convertView;
		}
		int currentImage;
		if (myInfo[position].state) currentImage = myInfo[position].onImage;
		else currentImage = myInfo[position].offImage;
		imageView.setImageResource(currentImage);
		imageView.setTag(myInfo[position]);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CategoryButton myInfo = (CategoryButton) arg0.getTag();
				myInfo.onClick(arg0);
			}
		});
		return imageView;
	}

	@Override
	public int getCount() {
		return myInfo.length;

	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}