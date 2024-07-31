package com.vgsoftware.android.realtime;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences
{
	private static final String PreferencesName = "SLRealTimeStartupPreferences";

	private static final String StartsKey = "Starts";
	private static final String LastDonationMessageKey = "LastDonationMessage";
	private static final String LastVersionUsedKey = "LastVersionUsed";
	private static final String RestoreTransactionsKey = "RestoreTransactions";
	private static final String CloseToMeViewKey = "ClosetoMeView";

	private Context _context = null;

	private int _starts;
	private int _lastDonationMessage;
	private int _lastVersionUsed;
	private boolean _restoreTransactions;
	private int _closeToMeView;
	private static UserPreferences _instance = null;

	public synchronized static UserPreferences getInstance()
	{
		if (_instance == null)
		{
			_instance = new UserPreferences();
		}
		return _instance;
	}

	public void init(Context context)
	{
		_context = context;
		load();
	}

	private UserPreferences()
	{
	}

	private void load()
	{
		SharedPreferences settings = _context.getSharedPreferences(UserPreferences.PreferencesName, 0);
		_starts = settings.getInt(UserPreferences.StartsKey, 0);
		_lastDonationMessage = settings.getInt(UserPreferences.LastDonationMessageKey, 0);
		_lastVersionUsed = settings.getInt(UserPreferences.LastVersionUsedKey, 0);
		_restoreTransactions = settings.getBoolean(UserPreferences.RestoreTransactionsKey, true);
		_closeToMeView = settings.getInt(UserPreferences.CloseToMeViewKey, R.id.listView);
	}

	public void save()
	{
		SharedPreferences settings = _context.getSharedPreferences(UserPreferences.PreferencesName, 0);
		SharedPreferences.Editor settingsEditor = settings.edit();
		settingsEditor.putInt(UserPreferences.StartsKey, _starts);
		settingsEditor.putInt(UserPreferences.LastDonationMessageKey, _lastDonationMessage);
		settingsEditor.putInt(UserPreferences.LastVersionUsedKey, _lastVersionUsed);
		settingsEditor.putBoolean(UserPreferences.RestoreTransactionsKey, _restoreTransactions);
		settingsEditor.putInt(UserPreferences.CloseToMeViewKey, _closeToMeView);
		settingsEditor.commit();
	}

	public int getStarts()
	{
		return _starts;
	}

	public void setStarts(int starts)
	{
		_starts = starts;
	}

	public int getLastDonationMessage()
	{
		return _lastDonationMessage;
	}

	public void setLastDonationMessage(int lastDonationMessage)
	{
		_lastDonationMessage = lastDonationMessage;
	}

	public int getLastVersionUsed()
	{
		return _lastVersionUsed;
	}

	public void setLastVersionUsed(int lastVersionUsed)
	{
		_lastVersionUsed = lastVersionUsed;
	}

	public boolean getRestoreTransactions()
	{
		return _restoreTransactions;
	}

	public void setRestoreTransactions(boolean restoreTransactions)
	{
		_restoreTransactions = restoreTransactions;
	}

	public int getCloseToMeView()
	{
		return _closeToMeView;
	}

	public void setCloseToMeView(int view)
	{
		_closeToMeView = view;
	}
}
