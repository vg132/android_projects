package com.vgsoftware.android.realtime.ui.adapters;

import java.text.DecimalFormat;
import java.util.List;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Utilities;
import com.vgsoftware.android.realtime.dataabstraction.Station;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CloseToMeAdapter extends ArrayAdapter<Station>
{
	private List<Station> _stations = null;
	private Location _currentLocation = null;
	private LayoutInflater _layoutInflater = null;

	public CloseToMeAdapter(Context context, int resource, List<Station> stations, Location currentLocation)
	{
		super(context, resource, stations);
		_stations = stations;
		_currentLocation = currentLocation;
		_layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		if (view == null)
		{
			view = _layoutInflater.inflate(R.layout.close_to_me_row, parent, false);
		}
		Station station = _stations.get(position);
		if (station != null)
		{
			ImageView iconImageView = (ImageView) view.findViewById(R.id.imageView);
			TextView stationTextView = (TextView) view.findViewById(R.id.textView);
			TextView distanceTextView = (TextView) view.findViewById(R.id.textView2);

			stationTextView.setText(station.getName());
			long distance = Math.round(station.getDistance(_currentLocation.getLatitude(),_currentLocation.getLongitude()) * 1000);
			DecimalFormat twoDecimalFormat = new DecimalFormat("#.00");
			if (distance < 1000)
			{
				distanceTextView.setText(distance + "m");
			}
			else
			{
				distanceTextView.setText(twoDecimalFormat.format((float)(distance) / 1000) + "km");
			}
			int resourceId = Utilities.getTransportationTypeDrawable(station.getTransportationTypeId());
			iconImageView.setImageDrawable(getContext().getResources().getDrawable(resourceId));
		}
		return view;
	}
}
