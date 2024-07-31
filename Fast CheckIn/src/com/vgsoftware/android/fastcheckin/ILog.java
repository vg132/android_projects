package com.vgsoftware.android.fastcheckin;

public interface ILog
{
	public void info(String tag, String message, Exception ex);
	public void error(String tag, String message, Exception ex);
	public void debug(String tag, String message, Exception ex);
	public void warn(String tag, String message, Exception ex);
	public void verbose(String tag, String message, Exception ex);
}