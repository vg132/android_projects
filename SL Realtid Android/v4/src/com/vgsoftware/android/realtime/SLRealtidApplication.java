package com.vgsoftware.android.realtime;

import android.app.Application;
import android.content.Context;

public class SLRealtidApplication extends Application
{
	private static Context context;

	public void onCreate()
	{
		super.onCreate();
		SLRealtidApplication.context = getApplicationContext();
	}

	public static Context getAppContext()
	{
		return SLRealtidApplication.context;
	}
}
