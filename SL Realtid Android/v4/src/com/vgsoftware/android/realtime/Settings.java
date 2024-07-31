package com.vgsoftware.android.realtime;

import com.vgsoftware.android.realtime.model.DataStore;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings
{
	private static final String PreferencesName = "SLRealTimeStartupPreferences";
	private static final String StartsKey = "Starts";
	private static final String LastDonationMessageKey = "LastDonationMessage";
	private static final String LastVersionUsedKey = "LastVersionUsed";
	private static final String RestoreTransactionsKey = "RestoreTransactions";
	private static final String LastFullScreenAdKey = "LastFullScreenAd";

	private static final String ShowBus = "ShowBus";
	private static final String ShowTrain = "ShowTrain";
	private static final String ShowTram = "ShowTram";
	private static final String ShowSubway = "ShowSubway";
	private static final String UseGPS = "UseGPS";
	private static final String StartTab = "StartTab";

	private SharedPreferences _sharedPreferences = null;
	private SharedPreferences _defaultPreferences = null;
	private DataStore _dataStore = null;

	public Settings(Context context)
	{
		_defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		_sharedPreferences = context.getSharedPreferences(Settings.PreferencesName, 0);
		_dataStore = new DataStore(context);
	}

	public int getStarts()
	{
		return _sharedPreferences.getInt(Settings.StartsKey, 0);
	}

	public void setStarts(int starts)
	{
		setInt(Settings.StartsKey, starts);
	}

	public int getLastVersionUsed()
	{
		return _sharedPreferences.getInt(Settings.LastVersionUsedKey, 0);
	}

	public void setLastVerionUsed(int version)
	{
		setInt(Settings.LastVersionUsedKey, version);
	}

	public int getLastDonationMessage()
	{
		return _sharedPreferences.getInt(Settings.LastDonationMessageKey, 0);
	}

	public void setLastDonationMessage(int lastDonationMessage)
	{
		setInt(Settings.LastDonationMessageKey, lastDonationMessage);
	}

	public boolean getRestoreTransactions()
	{
		return _sharedPreferences.getBoolean(Settings.RestoreTransactionsKey, false);
	}

	public void setRestoreTransactions(boolean value)
	{
		setBoolean(Settings.RestoreTransactionsKey, value);
	}

	public int getLastFullScreenAd()
	{
		return _sharedPreferences.getInt(Settings.LastFullScreenAdKey, 0);
	}

	public void setLastFullScreenAd(int lastFullScreenAd)
	{
		setInt(Settings.LastFullScreenAdKey, lastFullScreenAd);
	}

	public boolean getUseGPS()
	{
		return _defaultPreferences.getBoolean(Settings.UseGPS, true);
	}

	public boolean getShowBus()
	{
		return _defaultPreferences.getBoolean(Settings.ShowBus, true);
	}

	public int getStartTab()
	{
		return Integer.parseInt(_defaultPreferences.getString(Settings.StartTab, "0"));
	}

	public boolean getShowSubway()
	{
		return _defaultPreferences.getBoolean(Settings.ShowSubway, true);
	}

	public boolean getShowTrain()
	{
		return _defaultPreferences.getBoolean(Settings.ShowTrain, true);
	}

	public boolean getShowTram()
	{
		return _defaultPreferences.getBoolean(Settings.ShowTram, true);
	}

	public boolean isFreeEdition()
	{
		return _dataStore.listDonations().size() == 0;
	}

	private void setInt(String key, int value)
	{
		SharedPreferences.Editor settingsEditor = _sharedPreferences.edit();
		settingsEditor.putInt(key, value);
		settingsEditor.commit();
	}

	private void setBoolean(String key, boolean value)
	{
		SharedPreferences.Editor settingsEditor = _sharedPreferences.edit();
		settingsEditor.putBoolean(key, value);
		settingsEditor.commit();
	}
}
