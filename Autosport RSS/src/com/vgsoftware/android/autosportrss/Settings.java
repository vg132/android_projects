package com.vgsoftware.android.autosportrss;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings
{
	public static boolean getAutoUpdate()
	{
		if(getPreferences()!=null)
		{
			return getPreferences().getBoolean("AutoUpdate", true);
		}
		return true;
	}

	public static boolean getNotification()
	{
		if(getPreferences()!=null)
		{
			return getPreferences().getBoolean("NotificationCheckBox", true);
		}
		return true;
	}

	public static int getUpdateInterval()
	{
		if(getPreferences()!=null)
		{
			return Integer.parseInt(getPreferences().getString("UpdateInterval", "30"));
		}
		return 30;
	}

	public static int getClearInterval()
	{
		if(getPreferences()!=null)
		{
			return Integer.parseInt(getPreferences().getString("ClearInterval", "0"));
		}
		return 0;
	}

	public static SharedPreferences getPreferences()
	{
		return PreferenceManager.getDefaultSharedPreferences(Utility.getInstance().getContext());
	}
}
