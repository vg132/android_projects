package com.vgsoftware.android.sosmap.ui.overlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.vgsoftware.android.sosmap.Event;

public class EventOverlayItem extends OverlayItem
{
	private Event _event = null;
	
	public EventOverlayItem(GeoPoint point, Event event)
	{
		super(point, event.getHeading(), event.toString());
		_event=event;
	}
	
	public Event getEvent()
	{
		return _event;
	}
}
