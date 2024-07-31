package com.vgsoftware.android.fastcheckin;

import android.util.Log;

public class ConsoleLog implements ILog
{
	@Override
	public void info(String tag, String message, Exception ex)
	{
		Log.i(tag, message, ex);
	}

	@Override
	public void error(String tag, String message, Exception ex)
	{
		Log.e(tag, message, ex);
	}

	@Override
	public void debug(String tag, String message, Exception ex)
	{
		Log.d(tag, message, ex);
	}

	@Override
	public void warn(String tag, String message, Exception ex)
	{
		Log.w(tag, message, ex);
	}

	@Override
	public void verbose(String tag, String message, Exception ex)
	{
		Log.v(tag, message, ex);
	}
}
