package com.vgsoftware.android.vglib;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class SettingsBase
{
	private SharedPreferences _sharedPreferences = null;

	protected SettingsBase(Context context, String name)
	{
		_sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	protected int getInt(String key, int fallback)
	{
		return _sharedPreferences.getInt(key, fallback);
	}

	protected long getLong(String key, long fallback)
	{
		return _sharedPreferences.getLong(key, fallback);
	}
	
	protected boolean getBoolean(String key, boolean fallback)
	{
		return _sharedPreferences.getBoolean(key, fallback);
	}

	protected String getString(String key, String fallback)
	{
		return _sharedPreferences.getString(key, fallback);
	}

	protected void setInt(String key, int value)
	{
		SharedPreferences.Editor settingsEditor = _sharedPreferences.edit();
		settingsEditor.putInt(key, value);
		settingsEditor.commit();
	}

	protected void setLong(String key, long value)
	{
		SharedPreferences.Editor settingsEditor = _sharedPreferences.edit();
		settingsEditor.putLong(key, value);
		settingsEditor.commit();
	}
	
	protected void setBoolean(String key, boolean value)
	{
		SharedPreferences.Editor settingsEditor = _sharedPreferences.edit();
		settingsEditor.putBoolean(key, value);
		settingsEditor.commit();
	}

	protected void setString(String key, String value)
	{
		SharedPreferences.Editor settingsEditor = _sharedPreferences.edit();
		settingsEditor.putString(key, value);
		settingsEditor.commit();
	}
}
