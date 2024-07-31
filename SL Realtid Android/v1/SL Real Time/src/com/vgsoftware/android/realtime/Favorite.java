package com.vgsoftware.android.realtime;

public class Favorite
{
	private int _id;
	private int _stationId;
	private int _transportationTypeId;
	
	public Favorite()
	{
	}

	public Favorite(int stationId, int transportationTypeId)
	{
		_stationId=stationId;
		_transportationTypeId=transportationTypeId;
	}
	
	public Favorite(int id, int stationId, int transportationTypeId)
	{
		_stationId=stationId;
		_transportationTypeId=transportationTypeId;
		_id=id;
	}

	public int getId()
	{
		return _id;
	}
	
	public void setId(int id)
	{
		_id=id;
	}
	
	public int getStationId()
	{
		return _stationId;
	}

	public void setStationId(int id)
	{
		_stationId=id;
	}

	public int getTransportationTypeId()
	{
		return _transportationTypeId;
	}

	public void setTransportationTypeId(int id)
	{
		_transportationTypeId=id;
	}
}
