package com.vgsoftware.android.vglib.logging;

public class AbstractLogManager
{
	private String _tagName = null;
	private ILog _logger = new ConsoleLog();
	private boolean _enabled = true;

	public AbstractLogManager(String tagName)
	{
		_tagName = tagName;
	}

	private void log(int logLevel, String message, Exception ex)
	{
		if (_enabled)
		{
			switch (logLevel)
			{
			case android.util.Log.INFO:
				_logger.info(_tagName, message, ex);
				break;
			case android.util.Log.ERROR:
				_logger.error(_tagName, message, ex);
				break;
			case android.util.Log.DEBUG:
				_logger.debug(_tagName, message, ex);
				break;
			case android.util.Log.WARN:
				_logger.warn(_tagName, message, ex);
				break;
			case android.util.Log.VERBOSE:
				_logger.verbose(_tagName, message, ex);
				break;
			}
		}
	}

	public void info(String message)
	{
		log(android.util.Log.INFO, message, null);
	}

	public void error(String message)
	{
		log(android.util.Log.ERROR, message, null);
	}

	public void debug(String message)
	{
		log(android.util.Log.DEBUG, message, null);
	}

	public void warn(String message)
	{
		log(android.util.Log.WARN, message, null);
	}

	public void verbose(String message)
	{
		log(android.util.Log.VERBOSE, message, null);
	}

	public void info(String message, Exception ex)
	{
		log(android.util.Log.INFO, message, ex);
	}

	public void error(String message, Exception ex)
	{
		log(android.util.Log.ERROR, message, ex);
	}

	public void debug(String message, Exception ex)
	{
		log(android.util.Log.DEBUG, message, ex);
	}

	public void warn(String message, Exception ex)
	{
		log(android.util.Log.WARN, message, ex);
	}

	public void verbose(String message, Exception ex)
	{
		log(android.util.Log.VERBOSE, message, ex);
	}
	
	public void setEnabled(boolean enabled)
	{
		_enabled = enabled;
	}
	
	public boolean getEnabled()
	{
		return _enabled;
	}
}
