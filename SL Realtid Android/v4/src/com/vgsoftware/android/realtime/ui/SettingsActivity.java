package com.vgsoftware.android.realtime.ui;

import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.ui.fragments.SettingsFragment;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}

	@Override
	protected void onStart()
	{
		Tracking.activityStart(this, this);
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		Tracking.activityStop(this, this);
		super.onStop();
	}
}
