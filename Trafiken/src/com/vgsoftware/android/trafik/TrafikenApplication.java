package com.vgsoftware.android.trafik;

import android.app.Application;

public class TrafikenApplication extends Application
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
