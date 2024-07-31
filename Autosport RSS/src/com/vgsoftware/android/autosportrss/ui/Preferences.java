package com.vgsoftware.android.autosportrss.ui;

import com.vgsoftware.android.autosportrss.ui.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.layout.preferences);
	}

	@Override
	protected void onStop()
	{
		setResult(RESULT_OK);
		super.onStop();
	}
}