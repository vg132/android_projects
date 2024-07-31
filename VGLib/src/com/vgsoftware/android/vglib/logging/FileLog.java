package com.vgsoftware.android.vglib.logging;

public class FileLog implements ILog
{
	public void info(String tag, String message, Exception ex)
	{
	}

	public void error(String tag, String message, Exception ex)
	{
	}

	public void debug(String tag, String message, Exception ex)
	{
	}

	public void warn(String tag, String message, Exception ex)
	{
	}

	public void verbose(String tag, String message, Exception ex)
	{
	}
	/*
	 * try { FileHandler handler = new FileHandler("logfile.log");
	 * 
	 * Logger logger = Logger.getLogger("com.somename");
	 * logger.addHandler(handler);
	 * 
	 * } catch (IOException e) {
	 * 
	 * }
	 */
}
