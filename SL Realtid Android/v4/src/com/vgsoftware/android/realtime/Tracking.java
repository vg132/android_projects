package com.vgsoftware.android.realtime;

import com.vgsoftware.android.vglib.RuntimeUtility;

import android.app.Activity;
import android.content.Context;

public class Tracking
{
	public static final String CATEGORY_NETWORK = "Network";
	public static final String CATEGORY_DONATION = "Donation";
	public static final String CATEGORY_ADMOB = "AdMob";

	public static void sendException(Context context, String message)
	{
		if (context == null || RuntimeUtility.isDebug(context))
		{
			return;
		}
		try
		{
			//EasyTracker.getInstance().setContext(context);
			//EasyTracker.getTracker().sendException(message, false);
		}
		catch (Exception ex)
		{
		}
	}

	public static void sendException(Context context, String message, Exception ex)
	{
		if (context == null || RuntimeUtility.isDebug(context))
		{
			return;
		}
		try
		{
			//EasyTracker.getInstance().setContext(context);
			//EasyTracker.getTracker().sendException(message, ex, false);
		}
		catch (Exception e)
		{
		}
	}

	public static void sendEvent(Context context, String category, String action, String label)
	{
		if (context == null || RuntimeUtility.isDebug(context))
		{
			return;
		}
		try
		{
			//EasyTracker.getInstance().setContext(context);
			//EasyTracker.getTracker().sendEvent(category, action, label, 0L);
		}
		catch (Exception ex)
		{
		}
	}

	public static void sendView(Context context, String appScreen)
	{
		if (context == null || RuntimeUtility.isDebug(context))
		{
			return;
		}
		try
		{
			//EasyTracker.getInstance().setContext(context);
			//EasyTracker.getTracker().sendView(appScreen);
		}
		catch (Exception ex)
		{
		}
	}

	public static void activityStart(Context context, Activity activity)
	{
		if (context == null || RuntimeUtility.isDebug(context))
		{
			return;
		}
		try
		{
			//EasyTracker.getInstance().setContext(context);
			//EasyTracker.getInstance().activityStart(activity);
		}
		catch (Exception ex)
		{
		}
	}

	public static void activityStop(Context context, Activity activity)
	{
		if (context == null || RuntimeUtility.isDebug(context))
		{
			return;
		}
		try
		{
			//EasyTracker.getInstance().setContext(context);
			//EasyTracker.getInstance().activityStop(activity);
		}
		catch (Exception ex)
		{
		}
	}
}
