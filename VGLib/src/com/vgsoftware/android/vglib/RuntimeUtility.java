package com.vgsoftware.android.vglib;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class RuntimeUtility
{
	public static boolean isDebug(final Context context)
	{
		return BuildConfig.DEBUG;
	}

	public static int getVersion(final Context context)
	{
		try
		{
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}
		catch (NameNotFoundException ex)
		{
		}
		return -1;
	}
}
