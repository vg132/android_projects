package com.vgsoftware.android.realtime;

public class Departure
{
	private String _destination;
	private String _time;
	private String _status;

	public String getDestination()
	{
		return _destination;
	}

	public void setDestination(String destination)
	{
		_destination = destination;
	}

	public String getTime()
	{
		return _time;
	}

	public void setTime(String time)
	{
		_time = time;
	}

	public String getStatus()
	{
		return _status;
	}

	public void setStatus(String status)
	{
		_status = status;
	}
}
