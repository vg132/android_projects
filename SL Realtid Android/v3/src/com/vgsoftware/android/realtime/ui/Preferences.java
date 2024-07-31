package com.vgsoftware.android.realtime.ui;

import java.util.List;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Settings;
import com.vgsoftware.android.realtime.dataabstraction.Database;
import com.vgsoftware.android.realtime.dataabstraction.TransportationType;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity
{
	private ListPreference _transportationTypeListPreference = null;
	private ListPreference _startViewListPreference = null;
	private EditTextPreference _myLocationDistanceEditTextPreference = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);

		setupControls();

		List<TransportationType> types = Database.getInstance().listTransportationTypes();
		CharSequence[] values = new CharSequence[types.size()];
		CharSequence[] entries = new CharSequence[types.size()];
		for (int i = 0; i < values.length; i++)
		{
			values[i] = types.get(i).getId() + "";
			entries[i] = types.get(i).getName();
		}
		_transportationTypeListPreference.setEntries(entries);
		_transportationTypeListPreference.setEntryValues(values);

		TransportationType type = Database.getInstance().loadTransportationType(Settings.getInstance().getDefaultTransportationType());
		if (type != null)
		{
			_transportationTypeListPreference.setSummary(getSummaryText(type.getName()));
		}

		_transportationTypeListPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				if (newValue != null)
				{
					_transportationTypeListPreference.setSummary(getSummaryText((String) _transportationTypeListPreference.getEntries()[Integer.parseInt(newValue.toString())-1]));
					return true;
				}
				return false;
			}
		});

		_startViewListPreference.setSummary(getSummaryText((String) _startViewListPreference.getEntries()[Settings.getInstance().getStartupScreen()]));
		_startViewListPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				if (newValue != null)
				{
					_startViewListPreference.setSummary(getSummaryText((String) _startViewListPreference.getEntries()[Integer.parseInt(newValue.toString())]));
					return true;
				}
				return false;
			}
		});

		_myLocationDistanceEditTextPreference.setSummary(getSummaryText(Settings.getInstance().getMyLocationDistance() + " km"));
		_myLocationDistanceEditTextPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				if (newValue != null)
				{
					_myLocationDistanceEditTextPreference.setSummary(getSummaryText(newValue + " km"));
					return true;
				}
				return false;
			}
		});
	}

	private void setupControls()
	{
		_transportationTypeListPreference = (ListPreference) findPreference(getString(R.string.PreferencesDefaultTransportationType_Key));
		_startViewListPreference = (ListPreference) findPreference(getString(R.string.PreferencesStartupScreen_Key));
		_myLocationDistanceEditTextPreference = (EditTextPreference) findPreference(getString(R.string.PreferencesMyLocationDistance_Key));
	}

	private String getSummaryText(String value)
	{
		return String.format(getString(R.string.PreferencesCurrentSelection), value);
	}

	@Override
	protected void onStop()
	{
		setResult(RESULT_OK);
		super.onStop();
	}
}
