package com.vgsoftware.android.realtime.parse;

import java.util.ArrayList;
import java.util.List;

public class TrafficSituation
{
	private String _name = null;
	private List<TrafficEvent> _trafficEvent = null;

	public TrafficSituation()
	{
		_trafficEvent = new ArrayList<TrafficEvent>();
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public List<TrafficEvent> getTrafficEvents()
	{
		return _trafficEvent;
	}

	public void setTrafficEvents(List<TrafficEvent> trafficEvent)
	{
		_trafficEvent = trafficEvent;
	}
}
