package com.vgsoftware.android.polisen;

import android.app.Application;

public class PolisenRSSApplication extends Application
{
	private static boolean activityVisible;

	public static boolean isActivityVisible()
	{
		return activityVisible;
	}

	public static void activityResumed()
	{
		activityVisible = true;
	}

	public static void activityPaused()
	{
		activityVisible = false;
	}
}
