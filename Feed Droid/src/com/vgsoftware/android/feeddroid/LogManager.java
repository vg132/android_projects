package com.vgsoftware.android.feeddroid;

public class LogManager
{
	private static String LogTag = "Feed Droid";
	private static ILog _logger = new ConsoleLog();

	private static void log(int logLevel, String message, Exception ex)
	{
		switch (logLevel)
		{
		case android.util.Log.INFO:
			_logger.info(LogManager.LogTag, message, ex);
			break;
		case android.util.Log.ERROR:
			_logger.error(LogManager.LogTag, message, ex);
			break;
		case android.util.Log.DEBUG:
			_logger.debug(LogManager.LogTag, message, ex);
			break;
		case android.util.Log.WARN:
			_logger.warn(LogManager.LogTag, message, ex);
			break;
		case android.util.Log.VERBOSE:
			_logger.verbose(LogManager.LogTag, message, ex);
			break;
		}
	}

	public static void info(String message)
	{
		LogManager.log(android.util.Log.INFO, message, null);
	}

	public static void error(String message)
	{
		LogManager.log(android.util.Log.ERROR, message, null);
	}

	public static void debug(String message)
	{
		LogManager.log(android.util.Log.DEBUG, message, null);
	}

	public static void warn(String message)
	{
		LogManager.log(android.util.Log.WARN, message, null);
	}

	public static void verbose(String message)
	{
		LogManager.log(android.util.Log.VERBOSE, message, null);
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
