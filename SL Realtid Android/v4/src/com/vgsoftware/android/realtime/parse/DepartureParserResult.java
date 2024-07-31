package com.vgsoftware.android.realtime.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vgsoftware.android.realtime.model.Departure;

public class DepartureParserResult
{
	private Map<Integer, List<Departure>> _departures = new HashMap<Integer, List<Departure>>();

	public Set<Integer> keys()
	{
		return _departures.keySet();
	}
	
	public void set(final int transportationType, final List<Departure> departures)
	{
		_departures.remove(transportationType);
		_departures.put(transportationType, departures);
	}

	public void add(final int transportationType, final Departure departure)
	{
		if (!_departures.containsKey(transportationType))
		{
			_departures.put(transportationType, new ArrayList<Departure>());
		}
		_departures.get(transportationType).add(departure);
	}

	public List<Departure> get(final int transportationType)
	{
		if (_departures.containsKey(transportationType))
		{
			return _departures.get(transportationType);
		}
		return new ArrayList<Departure>();
	}
}
