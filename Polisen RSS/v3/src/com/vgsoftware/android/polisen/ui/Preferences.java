package com.vgsoftware.android.polisen.ui;

import java.sql.SQLException;

import com.vgsoftware.android.polisen.Log;
import com.vgsoftware.android.polisen.ui.R;
import com.vgsoftware.android.polisen.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.Region;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;

public class Preferences extends PreferenceActivity
{
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.activity_preferences);

		final PreferenceScreen regionScreen = (PreferenceScreen) findPreference("activity_preferences_region_select");
		final CheckBoxPreference allRegionCheckBox = (CheckBoxPreference) findPreference("preferences_key_region_all");
		addRegionCheckBoxes(regionScreen);
		setRegionScreenSummary(regionScreen, allRegionCheckBox.isChecked());
		allRegionCheckBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				setRegionScreenSummary(regionScreen, (Boolean) newValue);
				getListView().invalidateViews();
				return true;
			}
		});

		final PreferenceScreen feedScreen = (PreferenceScreen) findPreference("preferences_key_feed_select");
		final CheckBoxPreference allFeedsCheckBox = (CheckBoxPreference) findPreference("preferences_key_feed_all");
		addFeedCheckBoxes(feedScreen);
		setFeedScreenSummary(feedScreen, com.vgsoftware.android.polisen.Preferences.getInstance(getApplicationContext()).getAllFeeds());
		allFeedsCheckBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				setFeedScreenSummary(feedScreen, (Boolean) newValue);
				getListView().invalidateViews();
				return true;
			}
		});

		final ListPreference syncPref = (ListPreference) findPreference("preference_key_sync");
		setSyncSummary(syncPref, com.vgsoftware.android.polisen.Preferences.getInstance(getApplicationContext()).getAutomaticUpdate());
		syncPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				setSyncSummary(syncPref, Integer.parseInt(newValue.toString()));
				return true;
			}
		});
	}

	private void setRegionScreenSummary(PreferenceScreen screen, boolean value)
	{
		screen.setSummary(value ? getString(R.string.activity_preferences_region_all_selected) : getString(R.string.activity_preferences_region_all_notselected));
	}

	private void setFeedScreenSummary(PreferenceScreen screen, boolean value)
	{
		screen.setSummary(value ? getString(R.string.activity_preferences_feed_all_selected) : getString(R.string.activity_preferences_feed_all_notselected));
	}

	private void setSyncSummary(ListPreference preference, int value)
	{
		switch (value)
		{
		case com.vgsoftware.android.polisen.Preferences.AUTOMATIC_UPDATE_ALWAYS:
			preference.setSummary("Vid nya händelser");
			break;
		case com.vgsoftware.android.polisen.Preferences.AUTOMATIC_UPDATE_PROGRAM_START:
			preference.setSummary("Vid programstart");
			break;
		case com.vgsoftware.android.polisen.Preferences.AUTOMATIC_UPDATE_NEVER:
			preference.setSummary("Aldrig");
			break;
		}
	}

	private void addFeedCheckBoxes(PreferenceScreen feedScreen)
	{
		try
		{
			for (Feed feed : DatabaseHelper.getHelper(this).getFeedDao().queryBuilder().orderBy("id", true).query())
			{
				CheckBoxPreference checkBox = new CheckBoxPreference(this);
				checkBox.setTitle(feed.getName());
				checkBox.setKey("preferences_key_feed_" + feed.getId());
				checkBox.setPersistent(false);
				checkBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
				{
					@Override
					public boolean onPreferenceChange(Preference preference, Object newValue)
					{
						Log.verbose("Checkbox changed: " + newValue);
						int feedId = Integer.parseInt(preference.getKey().substring("preferences_key_feed_".length()));
						Feed feed = DatabaseHelper.getHelper(preference.getContext()).getFeedDao().queryForId(feedId);
						if (feed != null)
						{
							feed.setActive((Boolean) newValue);
							DatabaseHelper.getHelper(preference.getContext()).getFeedDao().update(feed);
							return true;
						}
						return false;
					}
				});
				feedScreen.addPreference(checkBox);
				checkBox.setDependency("preferences_key_feed_all");
				checkBox.setChecked(feed.isActive());
			}
		}
		catch (SQLException ex)
		{
			Log.error("Unable to list feeds", ex);
		}
	}

	private void addRegionCheckBoxes(PreferenceScreen regionScreen)
	{
		try
		{
			for (Region region : DatabaseHelper.getHelper(this).getRegionDao().queryBuilder().orderBy("name", true).query())
			{
				if (region.getId() > 0)
				{
					CheckBoxPreference checkBox = new CheckBoxPreference(this);
					checkBox.setTitle(region.getName());
					checkBox.setKey("preferences_key_region_" + region.getId());
					checkBox.setPersistent(false);
					checkBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
					{
						@Override
						public boolean onPreferenceChange(Preference preference, Object newValue)
						{
							int regionId = Integer.parseInt(preference.getKey().substring("preferences_key_region_".length()));
							Region region = DatabaseHelper.getHelper(preference.getContext()).getRegionDao().queryForId(regionId);
							if (region != null)
							{
								region.setActive((Boolean) newValue);
								DatabaseHelper.getHelper(preference.getContext()).getRegionDao().update(region);
								return true;
							}
							return false;
						}
					});
					regionScreen.addPreference(checkBox);
					checkBox.setDependency("preferences_key_region_all");
					checkBox.setChecked(region.getActive());
				}
			}
		}
		catch (SQLException ex)
		{
			Log.error("Unable to list regions", ex);
		}
	}
}
