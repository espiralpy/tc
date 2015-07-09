package com.avai.amp.prx.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class LibraryApplication {

	private static Context context;
	public static final String PREFS = "OcnPreferences";
	public static final String PREFS_USERNAME = "Username";
	public static final String PREFS_PASSWORD = "Password";
	public static final String PREFS_SCREENNAME = "ScreenName";
	public static final String PREFS_CATEGORIES_SET = "Categories";
	public static final String PREFS_VIEW_WILDLIFE = "Wildlife";
	public static final String PREFS_VIEW_HUNTING = "Hunting";
	public static final String PREFS_VIEW_FISHING = "Fishing";
	public static final String PREFS_VIEW_SCENIC = "Scenic";

	public static void setContext(Context c) {
		context = c;
	}
	
	public static Context getContext() {
		if (context == null) throw new IllegalStateException("Library Application has not set a context");
		else return context;
	}
	
	public static boolean categoryPrefsAreSet() {
		return getContext().getSharedPreferences(PREFS, 0).getBoolean(PREFS_CATEGORIES_SET, false);
	}
	
	public static void setCategoryPrefs(boolean wildlife, boolean hunting, boolean fishing, boolean scenic) {
		SharedPreferences prefs = getContext().getSharedPreferences(PREFS, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(PREFS_VIEW_WILDLIFE, wildlife);
		editor.putBoolean(PREFS_VIEW_HUNTING, hunting);
		editor.putBoolean(PREFS_VIEW_FISHING, fishing);
		editor.putBoolean(PREFS_VIEW_SCENIC, scenic);
		editor.putBoolean(PREFS_CATEGORIES_SET, true);
		editor.commit();
	}

	public static String toISOString(Date birthday) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(birthday);
	}

	public static Date toISODate(String birthday) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return df.parse(birthday);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method convets dp unit to equivalent device specific value in pixels.
	 * 
	 * @param dp A value in dp(Device independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent Pixels equivalent to dp according to device
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to device independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent db equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;

	}

}
