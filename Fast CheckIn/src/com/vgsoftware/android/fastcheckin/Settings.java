package com.vgsoftware.android.fastcheckin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings
{
	private Context _context = null;
	private SharedPreferences _preferences = null;
	
	public Settings(Context context)
	{
		_context = context;
		_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public boolean getUseGPS()
	{
		return _preferences.getBoolean(_context.getString(R.string.Preferences_UseGPS_Id),true);
	}
	
	public boolean getShowMessage()
	{
		return _preferences.getBoolean(_context.getString(R.string.Preferences_ShowMessageDialog_Id),true);
	}
}
