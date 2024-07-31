package com.vgsoftware.android.gamelibrary;

import android.app.Application;
import android.content.Context;

public class GameLibraryApplication extends Application
{
	private static Context _context = null;

	public void onCreate()
	{
		super.onCreate();
		_context = getApplicationContext();
	}

	public static Context getAppContext()
	{
		return _context;
	}
}
