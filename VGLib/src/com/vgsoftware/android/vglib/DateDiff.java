package com.vgsoftware.android.vglib;

public class DateDiff
{
	private long _milliseconds = 0;
	private long _seconds = 0;
	private long _minutes = 0;
	private long _hours = 0;
	private long _days = 0;

	public DateDiff()
	{
	}

	public DateDiff(long days, long hours, long minutes, long seconds, long milliseconds)
	{
		_days = days;
		_hours = hours;
		_minutes = minutes;
		_seconds = seconds;
		_milliseconds = milliseconds;
	}

	public long getMilliseconds()
	{
		return _milliseconds;
	}

	public void setMilliseconds(long milliseconds)
	{
		_milliseconds = milliseconds;
	}

	public long getSeconds()
	{
		return _seconds;
	}

	public void setSeconds(long seconds)
	{
		_seconds = seconds;
	}

	public long getMinutes()
	{
		return _minutes;
	}

	public void setMinutes(long minutes)
	{
		_minutes = minutes;
	}

	public long getHours()
	{
		return _hours;
	}

	public void setHours(long hours)
	{
		_hours = hours;
	}

	public long getDays()
	{
		return _days;
	}

	public void setDays(long days)
	{
		_days = days;
	}
}
