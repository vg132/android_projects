package com.vgsoftware.android.fastcheckin;

public class GeoLocation
{
	private double _longitude = 0.0d;
	private double _latitude = 0.0d;
	
	public GeoLocation()
	{
	}

	public GeoLocation(double longitude, double latitude)
	{
		_longitude = longitude;
		_latitude = latitude;
	}
	
	public void setLongitude(double longitude)
	{
		_longitude = longitude;
	}
	
	public double getLongitude()
	{
		return _longitude;
	}
	
	public void setLatitude(double latitude)
	{
		_latitude = latitude;
	}
	
	public double getLatitude()
	{
		return _latitude;
	}
}
