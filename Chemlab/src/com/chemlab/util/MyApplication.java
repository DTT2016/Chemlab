package com.chemlab.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyApplication extends Application {

	
    public static String ID;
	public static String PW;
	public static String RANK;
	public static String HOST = "http://bxw2359770225.my3w.com";
	
	private static Context context;
	private static SharedPreferences pref;
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		
		ID = pref.getString("name", "1111");
		PW = pref.getString("password", "1111");
		RANK = pref.getString("rank", "0");
	}
	
	public static Context getContext() {
		return context;
	}
	
	public static void saveString(Context context, String key, String value) {
		pref.edit().putString(key, value).commit();

	}

	public static String getString(Context context, String key) {
		return pref.getString(key, "");
	}
	 
}
