package com.vgsoftware.android.realtime.ui.overlay;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Utilities;
import com.vgsoftware.android.realtime.dataabstraction.Station;

public class StationOverlay extends ItemizedOverlay<StationOverlayItem>
{
	private OnStationSelectedListener _listener = null;
	private ArrayList<StationOverlayItem> _items = null;
	private Context _context = null;

	public StationOverlay(Drawable marker, Context context)
	{
		super(boundCenterBottom(marker));
		_context = context;
		_items = new ArrayList<StationOverlayItem>();
	}

	public void setOnStationSelectedListener(OnStationSelectedListener listener)
	{
		_listener = listener;
	}

	public void addOverlay(StationOverlayItem overlay)
	{
		_items.add(overlay);
		populate();
	}

	@Override
	protected StationOverlayItem createItem(int i)
	{
		return _items.get(i);
	}

	@Override
	public int size()
	{
		return _items.size();
	}

	@Override
	protected boolean onTap(int index)
	{
		StationOverlayItem item = _items.get(index);
		final Station station = item.getStation();
		AlertDialog.Builder dialog = new AlertDialog.Builder(_context);
		dialog.setTitle(station.getName());
		dialog.setMessage(item.getTransportationType().getName() + ": " + station.getName());
		dialog.setIcon(Utilities.getTransportationTypeDrawable(station.getTransportationTypeId()));
		dialog.setPositiveButton(R.string.showTimes, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				if (_listener != null)
				{
					_listener.onStationSelected(station);
				}
			}
		});
		dialog.show();
		return true;
	}
}