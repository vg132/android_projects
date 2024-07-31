package com.vgsoftware.android.vglib.logging;

import android.util.Log;

public class ConsoleLog implements ILog
{
	public void info(String tag, String message, Exception ex)
	{
		Log.i(tag, message, ex);
	}

	public void error(String tag, String message, Exception ex)
	{
		Log.e(tag, message, ex);
	}

	public void debug(String tag, String message, Exception ex)
	{
		Log.d(tag, message, ex);
	}

	public void warn(String tag, String message, Exception ex)
	{
		Log.w(tag, message, ex);
	}

	public void verbose(String tag, String message, Exception ex)
	{
		Log.v(tag, message, ex);
	}
}
