package com.vgsoftware.android.sosmap;

public class Event
{
	private double _latitude = 0.0;
	private double _longitude = 0.0;
	private String _heading = null;
	private String _time = null;
	private String _issue = null;
	private String _location = null;
	private String _priority = null;
	private String _callCenter = null;
	private String _link = null;
	private String _county = null;

	public double getLongitude()
	{
		return _longitude;
	}

	public void setLongitude(double longitude)
	{
		_longitude = longitude;
	}

	public double getLatitude()
	{
		return _latitude;
	}

	public void setLatitude(double latitude)
	{
		_latitude = latitude;
	}

	public String getHeading()
	{
		return _heading;
	}

	public void setHeading(String heading)
	{
		_heading = heading;
	}

	public String getTime()
	{
		return _time;
	}

	public void setTime(String time)
	{
		_time = time;
	}

	public String getIssue()
	{
		return _issue;
	}

	public void setIssue(String issue)
	{
		_issue = issue;
	}

	public String getLocation()
	{
		return _location;
	}

	public void setLocation(String location)
	{
		_location = location;
	}

	public String getPriority()
	{
		return _priority;
	}

	public void setPriority(String priority)
	{
		_priority = priority;
	}

	public String getCallCenter()
	{
		return _callCenter;
	}

	public void setCallCenter(String callCenter)
	{
		_callCenter = callCenter;
	}
	
	public String getLink()
	{
		return _link;
	}
	
	public void setLink(String link)
	{
		_link = link;
	}

	public int getUniqueId()
	{
		return (getCallCenter() + getTime() + getIssue() + getTime() + getLongitude() + getLatitude() + getPriority() + getLocation()+getCounty()).hashCode();
	}
	
	public String getCounty()
	{
		return _county;
	}
	
	public void setCountry(String county)
	{
		_county = county;
	}
	
	public String toString()
	{
		return "Tid: " + getTime() + "\n" +
						"Händelse: " + getIssue() + "\n" +
						"Plats: " + getLocation() + "\n" +
						"Kommun: " + getCounty() + "\n"+
						"SOS-larmcentral: " + getCallCenter() + "\n" +
						"Uttryckningsprioritet: " + getPriority() + "\n";
	}
}
