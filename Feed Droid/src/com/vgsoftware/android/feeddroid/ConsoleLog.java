package com.vgsoftware.android.feeddroid;

import android.util.Log;

public class ConsoleLog implements ILog
{/*
	static
	{
		String name;
		name = Environment.getExternalStorageDirectory().getAbsolutePath();
		name += "/mylogfile.log";

		try
		{
			FileHandler fh = new FileHandler(name, true);
			Logger l = Logger.getLogger("com.vgsoftware.android");
			l.addHandler(fh);
		}
		catch (Exception e)
		{
			Log.e("MyLog", "FileHandler exception", e);
		}
	}
*/
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
