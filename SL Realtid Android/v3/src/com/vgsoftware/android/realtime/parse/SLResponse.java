package com.vgsoftware.android.realtime.parse;

import java.util.ArrayList;


public class SLResponse
{
	private ArrayList<Departure> _bus;
	private ArrayList<Departure> _metro;
	private ArrayList<Departure> _train;
	private ArrayList<Departure> _tram;

	public ArrayList<Departure> getBus()
	{
		return _bus;
	}

	public void setBus(ArrayList<Departure> bus)
	{
		_bus = bus;
	}

	public ArrayList<Departure> getMetro()
	{
		return _metro;
	}

	public void setMetro(ArrayList<Departure> metro)
	{
		_metro = metro;
	}

	public ArrayList<Departure> getTrain()
	{
		return _train;
	}

	public void setTrain(ArrayList<Departure> train)
	{
		_train = train;
	}

	public ArrayList<Departure> getTram()
	{
		return _tram;
	}

	public void setTram(ArrayList<Departure> tram)
	{
		_tram = tram;
	}
}
