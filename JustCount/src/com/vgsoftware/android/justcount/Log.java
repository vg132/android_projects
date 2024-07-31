package com.vgsoftware.android.justcount;

public class Log
{
	private static String LogTag = "PolisenRSS";

	private static void log(int logLevel, String message, Exception ex)
	{
		switch (logLevel)
		{
		case android.util.Log.INFO:
			android.util.Log.i(Log.LogTag, message, ex);
			break;
		case android.util.Log.ERROR:
			android.util.Log.e(Log.LogTag, message, ex);
			break;
		case android.util.Log.DEBUG:
			android.util.Log.d(Log.LogTag, message, ex);
			break;
		case android.util.Log.WARN:
			android.util.Log.w(Log.LogTag, message, ex);
			break;
		case android.util.Log.VERBOSE:
			android.util.Log.v(Log.LogTag, message, ex);
			break;
		}
	}

	public static void info(String message)
	{
		Log.log(android.util.Log.INFO, message, null);
	}

	public static void error(String message)
	{
		Log.log(android.util.Log.ERROR, message, null);
	}

	public static void debug(String message)
	{
		Log.log(android.util.Log.DEBUG, message, null);
	}

	public static void warn(String message)
	{
		Log.log(android.util.Log.WARN, message, null);
	}

	public static void verbose(String message)
	{
		Log.log(android.util.Log.VERBOSE, message, null);
	}

	public static void info(String message, Exception ex)
	{
		Log.log(android.util.Log.INFO, message, ex);
	}

	public static void error(String message, Exception ex)
	{
		Log.log(android.util.Log.ERROR, message, ex);
	}

	public static void debug(String message, Exception ex)
	{
		Log.log(android.util.Log.DEBUG, message, ex);
	}

	public static void warn(String message, Exception ex)
	{
		Log.log(android.util.Log.WARN, message, ex);
	}

	public static void verbose(String message, Exception ex)
	{
		Log.log(android.util.Log.VERBOSE, message, ex);
	}
}
