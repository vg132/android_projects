package com.vgsoftware.android.gamelibrary;

import java.util.Date;

import com.vgsoftware.android.vglib.SettingsBase;

public class Settings extends SettingsBase
{
	private static final String LastUpdated = "__Last_Updated__";
	private static Settings _instance = null;

	private Settings()
	{
		super(GameLibraryApplication.getAppContext(), "GameLibrarySettings");		
	}

	public static Settings getInstance()
	{
		if (_instance == null)
		{
			_instance = new Settings();
		}
		return _instance;
	}

	public Date getLastUpdate()
	{
		return new Date(getLong(Settings.LastUpdated, 0));
	}

	public void setLastUpdated(long milliseconds)
	{
		setLong(Settings.LastUpdated, milliseconds);
	}
}
