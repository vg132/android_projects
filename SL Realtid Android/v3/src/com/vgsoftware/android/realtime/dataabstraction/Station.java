package com.vgsoftware.android.realtime.dataabstraction;

import com.vgsoftware.android.vglib.MapUtility;

import android.database.Cursor;
import android.text.TextUtils;

public class Station
{
	private int _id;
	private int _siteId;
	private String _name;
	private String _areaName;
	private String _alias;
	private int _transportationTypeId;
	private double _latitude;
	private double _longitude;

	public Station()
	{
	}

	public Station(Cursor cursor)
	{
		if (cursor != null)
		{
			_id = cursor.getInt(0);
			_siteId = cursor.getInt(1);
			_transportationTypeId = cursor.getInt(2);
			_name = cursor.getString(3);
			_areaName = cursor.getString(4);
			_alias = cursor.getString(5);
			_latitude = cursor.getDouble(6);
			_longitude = cursor.getDouble(7);
		}
	}

	public int getId()
	{
		return _id;
	}

	public int getSiteId()
	{
		return _siteId;
	}

	public void setSiteId(int siteId)
	{
		_siteId = siteId;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public String getAreaName()
	{
		return _areaName;
	}

	public void setAreaName(String areaName)
	{
		_areaName = areaName;
	}

	public String getAlias()
	{
		return _alias;
	}

	public void setAlias(String alias)
	{
		_alias = alias;
	}

	public int getTransportationTypeId()
	{
		return _transportationTypeId;
	}

	public void setTransportationTypeId(int transportationTypeId)
	{
		_transportationTypeId = transportationTypeId;
	}

	public double getLatitude()
	{
		return _latitude;
	}

	public void setLatitude(double latitude)
	{
		_latitude = latitude;
	}

	public double getLongitude()
	{
		return _longitude;
	}

	public void setLongitude(double longitude)
	{
		_longitude = longitude;
	}

	public double getDistance(double latitude, double longitude)
	{
		return MapUtility.getDistance(latitude, longitude, this.getLatitude(), this.getLongitude());
	}

	@Override
	public String toString()
	{
		if (!TextUtils.isEmpty(getAreaName()))
		{
			return String.format("%s (%s)", getName(), getAreaName());
		}
		return getName();
	}
}
