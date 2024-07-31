package com.vgsoftware.android.realtime.dataabstraction;

import android.database.Cursor;

public class Favorite implements Comparable<Favorite>
{
	private int _id;
	private int _stationId;
	private int _transportationTypeId;
	private Station _station = null;

	public Favorite(int stationId)
	{
		_stationId = stationId;
	}

	public Favorite(Cursor cursor)
	{
		if (cursor != null)
		{
			_id = cursor.getInt(0);
			_stationId = cursor.getInt(1);
			_transportationTypeId = cursor.getInt(2);
		}
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		_id = id;
	}

	public int getStationId()
	{
		return _stationId;
	}

	public void setStationId(int id)
	{
		_stationId = id;
	}

	public int getTransportationTypeId()
	{
		return _transportationTypeId;
	}

	public void setTransportationTypeId(int id)
	{
		_transportationTypeId = id;
	}

	public synchronized Station getStation()
	{
		if(_station==null)
		{
			_station = Database.getInstance().loadStation(getStationId());
		}
		return _station;
	}

	public int compareTo(Favorite fav)
	{
		return this.getStation().getName().compareTo(fav.getStation().getName());
  }
}