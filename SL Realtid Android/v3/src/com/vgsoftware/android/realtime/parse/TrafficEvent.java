package com.vgsoftware.android.realtime.parse;

public class TrafficEvent
{
	private String _message = null;
	private String _line = null;

	public TrafficEvent()
	{
	}

	public String getMessage()
	{
		return _message;
	}

	public void setMessage(String message)
	{
		_message = message;
	}

	public String getLine()
	{
		return _line;
	}

	public void setLine(String line)
	{
		_line = line;
	}
}
