package com.vgsoftware.android.trafik;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
//import android.preference.PreferenceManager;

public class Preferences
{
	private static Preferences _instance = null;

	private SharedPreferences _sharedPreferences = null;
	//private SharedPreferences _defaultPreferences = null;
	private final static String PreferencesName = "TrafikenPreferences";

	private final static String SelectedFeed = "SelectedFeed";
	private final static String LastSyncDate = "LastSyncDate";
	private final static String OutOfSync = "OutOfSync";
	private final static String LastVersionUsed = "LastVersionUsed";

	public final static int AUTOMATIC_UPDATE_ALWAYS = 0;
	public final static int AUTOMATIC_UPDATE_PROGRAM_START = 1;
	public final static int AUTOMATIC_UPDATE_NEVER = 2;

	private Preferences(Context context)
	{
		//_defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		_sharedPreferences = context.getSharedPreferences(Preferences.PreferencesName, 0);
	}

	public static synchronized Preferences getInstance(Context context)
	{
		if (_instance == null)
		{
			_instance = new Preferences(context);
		}
		return _instance;
	}

	public void setLastSyncDate(Date date)
	{
		SharedPreferences.Editor editor = _sharedPreferences.edit();
		editor.putLong(Preferences.LastSyncDate, date.getTime());
		editor.commit();
	}

	public Date getLastSyncDate()
	{
		return new Date(_sharedPreferences.getLong(Preferences.LastSyncDate, 0));
	}

	public void setOutOfSync(boolean outOfSync)
	{
		SharedPreferences.Editor editor = _sharedPreferences.edit();
		editor.putBoolean(Preferences.OutOfSync, outOfSync);
		editor.commit();
	}

	public boolean getOutOfSync()
	{
		return _sharedPreferences.getBoolean(Preferences.OutOfSync, true);
	}

	public int getLastVersionUsed()
	{
		return _sharedPreferences.getInt(Preferences.LastVersionUsed, 0);
	}

	public void setLastVersionUsed(int version)
	{
		SharedPreferences.Editor editor = _sharedPreferences.edit();
		editor.putInt(Preferences.LastVersionUsed, version);
		editor.commit();
	}
	
	public int getSelectedFeedId()
	{
		return _sharedPreferences.getInt(Preferences.SelectedFeed,-1);
	}
	
	public void setSelectedFeedId(int id)
	{
		SharedPreferences.Editor editor = _sharedPreferences.edit();
		editor.putInt(Preferences.SelectedFeed,id);
		editor.commit();
	}
}
