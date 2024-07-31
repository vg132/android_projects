package com.vgsoftware.android.sosmap;

import java.util.ArrayList;
import java.util.List;

public class EventStore
{
	private static List<Event> _events = new ArrayList<Event>();

	public static int update()
	{
		List<Event> newEvents = SOSParser.parse();
		int newItems = newEvents.size();
		for (Event newEvent : newEvents)
		{
			for (Event oldEvent : _events)
			{
				if (oldEvent.getUniqueId() == newEvent.getUniqueId())
				{
					newItems--;
					break;
				}
			}
		}
		_events=newEvents;
		return newItems;
	}

	public static List<Event> getEvents()
	{
		return _events;
	}
}
