package com.avai.amp.prx.myfeed;

import com.avai.amp.prx.R;
import com.avai.amp.prx.http.transfer.OcnPostRetrievalTO;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MyFeedListItem extends RelativeLayout {
	
	private OcnPostRetrievalTO mPost;

	public MyFeedListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.my_feed_list_item, this, true);
	}
	
	public void setPostDetails(OcnPostRetrievalTO mPost) {
		this.mPost = mPost;
		
		//change item params to match post
	}
}
