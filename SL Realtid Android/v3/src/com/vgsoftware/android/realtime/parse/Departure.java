package com.vgsoftware.android.realtime.parse;

import java.util.Date;

public class Departure
{
	private String _destination;
	private Date _timeTabledDateTime;
	private Date _expectedDateTime;
	private String _line;
	private String _stopAreaName;
	private String _displayTime;
	private int _stopAreaNumber;
	private int _direction;

	public String getDestination()
	{
		return _destination;
	}

	public void setDestination(String destination)
	{
		_destination = destination;
	}

	public Date getTimeTabledDateTime()
	{
		return _timeTabledDateTime;
	}

	public void setTimeTabledDateTime(Date time)
	{
		_timeTabledDateTime = time;
	}

	public Date getExpectedDateTime()
	{
		return _expectedDateTime;
	}

	public void setExpectedDateTime(Date expectedTime)
	{
		_expectedDateTime = expectedTime;
	}

	public String getLine()
	{
		return _line;
	}

	public void setLine(String line)
	{
		_line = line;
	}

	public void setStopAreaName(String stopAreaName)
	{
		_stopAreaName = stopAreaName;
	}

	public String getStopAreaName()
	{
		return _stopAreaName;
	}

	public void setDisplayTime(String displayTime)
	{
		_displayTime = displayTime;
	}

	public String getDisplayTime()
	{
		return _displayTime;
	}

	public int getStopAreaNumber()
	{
		return _stopAreaNumber;
	}

	public void setStopAreaNumber(int stopAreaNumber)
	{
		_stopAreaNumber = stopAreaNumber;
	}

	public int getDirection()
	{
		return _direction;
	}

	public void setDirection(int direction)
	{
		_direction = direction;
	}
}
