package com.vgsoftware.android.realtime;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Settings
{
	private static SharedPreferences _preferences;
	private static Settings _instance;
	private Context _context = null;

	private Settings()
	{
	}
	
	public void init(Context context)
	{
		_context = context;
		_preferences = PreferenceManager.getDefaultSharedPreferences(_context);
	}

	public synchronized static Settings getInstance()
	{
		if (_instance == null)
		{
			_instance = new Settings();
		}
		return _instance;
	}

	public boolean isMyLocationEnabled()
	{
		return _preferences.getBoolean(_context.getString(R.string.PreferencesEnableMyLocation_Key), true);
	}

	public boolean isMyLocationGPSEnabled()
	{
		return _preferences.getBoolean(_context.getString(R.string.PreferencesEnableMyLocationGPS_Key), true);
	}

	public boolean showShortDelays()
	{
		return _preferences.getBoolean(_context.getString(R.string.PreferencesShowShortDelays_Key), false);
	}

	public boolean showArrivalTime()
	{
		return _preferences.getBoolean(_context.getString(R.string.PreferencesShowArrivalTime_Key), true);
	}

	public int getStartupScreen()
	{
		return Integer.parseInt(_preferences.getString(_context.getString(R.string.PreferencesStartupScreen_Key), "0"));
	}

	public int getDefaultTransportationType()
	{
		return Integer.parseInt(_preferences.getString(_context.getString(R.string.PreferencesDefaultTransportationType_Key), "1"));
	}
	
	public int getMyLocationDistance()
	{
		String value = _preferences.getString(_context.getString(R.string.PreferencesMyLocationDistance_Key), "5");
		if (TextUtils.isDigitsOnly(value))
		{
			return Integer.parseInt(value);
		}
		return 5;
	}
}
