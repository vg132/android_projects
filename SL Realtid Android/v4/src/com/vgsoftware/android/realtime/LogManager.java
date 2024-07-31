package com.vgsoftware.android.realtime;

import java.util.Locale;

import android.content.Context;

import com.vgsoftware.android.vglib.RuntimeUtility;

public class LogManager
{
	private static String LogTag = "SL Realtid";

	private static void log(int logLevel, String message, Exception ex)
	{
		Context context = SLRealtidApplication.getAppContext();
		if (context == null)
		{
			return;
		}

		if (logLevel == android.util.Log.ERROR || ex != null)
		{
			Tracking.sendException(context, message, ex);
		}

		if (RuntimeUtility.isDebug(context))
		{
			switch (logLevel)
			{
				case android.util.Log.INFO:
					android.util.Log.i(LogManager.LogTag, message, ex);
					break;
				case android.util.Log.ERROR:
					android.util.Log.e(LogManager.LogTag, message, ex);
					break;
				case android.util.Log.DEBUG:
					android.util.Log.d(LogManager.LogTag, message, ex);
					break;
				case android.util.Log.WARN:
					android.util.Log.w(LogManager.LogTag, message, ex);
					break;
				case android.util.Log.VERBOSE:
					android.util.Log.v(LogManager.LogTag, message, ex);
					break;
			}
		}
	}

	public static void info(String message)
	{
		LogManager.log(android.util.Log.INFO, message, null);
	}

	public static void info(String message, Object... args)
	{
		LogManager.log(android.util.Log.INFO, String.format(Locale.getDefault(), message, args), null);
	}

	public static void error(String message)
	{
		LogManager.log(android.util.Log.ERROR, message, null);
	}

	public static void error(String message, Object... args)
	{
		LogManager.log(android.util.Log.ERROR, String.format(Locale.getDefault(), message, args), null);
	}

	public static void debug(String message)
	{
		LogManager.log(android.util.Log.DEBUG, message, null);
	}

	public static void debug(String message, Object... args)
	{
		LogManager.log(android.util.Log.DEBUG, String.format(Locale.getDefault(), message, args), null);
	}

	public static void warn(String message)
	{
		LogManager.log(android.util.Log.WARN, message, null);
	}

	public static void warn(String message, Object... args)
	{
		LogManager.log(android.util.Log.WARN, String.format(Locale.getDefault(), message, args), null);
	}

	public static void verbose(String message)
	{
		LogManager.log(android.util.Log.VERBOSE, message, null);
	}

	public static void verbose(String message, Object... args)
	{
		LogManager.log(android.util.Log.VERBOSE, String.format(Locale.getDefault(), message, args), null);
	}

	public static void info(String message, Exception ex)
	{
		LogManager.log(android.util.Log.INFO, message, ex);
	}

	public static void error(String message, Exception ex)
	{
		LogManager.log(android.util.Log.ERROR, message, ex);
	}

	public static void debug(String message, Exception ex)
	{
		LogManager.log(android.util.Log.DEBUG, message, ex);
	}

	public static void warn(String message, Exception ex)
	{
		LogManager.log(android.util.Log.WARN, message, ex);
	}

	public static void verbose(String message, Exception ex)
	{
		LogManager.log(android.util.Log.VERBOSE, message, ex);
	}
}
