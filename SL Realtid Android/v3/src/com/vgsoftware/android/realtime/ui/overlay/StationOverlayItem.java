package com.vgsoftware.android.realtime.ui.overlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.vgsoftware.android.realtime.dataabstraction.Database;
import com.vgsoftware.android.realtime.dataabstraction.Station;
import com.vgsoftware.android.realtime.dataabstraction.TransportationType;

public class StationOverlayItem extends OverlayItem
{
	private Station _station = null;

	public StationOverlayItem(GeoPoint point, Station station)
	{
		super(point, "Kalle", "anka");
		_station = station;
	}

	public Station getStation()
	{
		return _station;
	}

	public TransportationType getTransportationType()
	{
		return Database.getInstance().loadTransportationType(_station.getTransportationTypeId());
	}
}