package com.vgsoftware.android.realtime.ui.fragments;

import com.vgsoftware.android.realtime.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.fragment_settings);
	}
}
