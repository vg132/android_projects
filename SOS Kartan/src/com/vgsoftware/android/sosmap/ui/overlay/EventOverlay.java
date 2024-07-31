package com.vgsoftware.android.sosmap.ui.overlay;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class EventOverlay extends BalloonItemizedOverlay<EventOverlayItem>
{
	private List<EventOverlayItem> _items = null;

	public EventOverlay(Drawable defaultMarker, MapView mapView)
	{
		super(boundCenterBottom(defaultMarker), mapView);
		_items = new ArrayList<EventOverlayItem>();
		setBalloonBottomOffset(40);
	}

	public void addOverlay(EventOverlayItem item)
	{
		_items.add(item);
		populate();
	}

	@Override
	protected EventOverlayItem createItem(int index)
	{
		return _items.get(index);
	}

	@Override
	public int size()
	{
		return _items.size();
	}

	@Override
	protected boolean onBalloonTap(int index, EventOverlayItem item)
	{
		//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getEvent().getLink()));
		//_context.startActivity(browserIntent);
		return true;
	}
}
