package com.vgsoftware.android.feeddroid;

public class FileLog implements ILog
{

	@Override
	public void info(String tag, String message, Exception ex)
	{
	}

	@Override
	public void error(String tag, String message, Exception ex)
	{
	}

	@Override
	public void debug(String tag, String message, Exception ex)
	{
	}

	@Override
	public void warn(String tag, String message, Exception ex)
	{
	}

	@Override
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
