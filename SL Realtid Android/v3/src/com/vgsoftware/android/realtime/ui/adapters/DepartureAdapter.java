package com.vgsoftware.android.realtime.ui.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Settings;
import com.vgsoftware.android.realtime.parse.Departure;
import com.vgsoftware.android.realtime.ui.controls.TransportationTypeImageView;

public class DepartureAdapter extends ArrayAdapter<Departure>
{
	private final SimpleDateFormat _timeTableSimpleDateFormat = new SimpleDateFormat("HH:mm");
	private final SimpleDateFormat _delaySimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
	private List<Departure> _departures = null;
	private int _transportationTypeId = 1;
	private LayoutInflater _layoutInflater = null;

	public DepartureAdapter(Context context, int textViewResourceId, List<Departure> departures, int transportationTypeId)
	{
		super(context, textViewResourceId, departures);
		_departures = departures;
		_transportationTypeId = transportationTypeId;
		_layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		if (view == null)
		{
			view = _layoutInflater.inflate(R.layout.realtime_result_row, parent, false);
		}
		Departure departure = _departures.get(position);
		if (departure != null)
		{
			TransportationTypeImageView iconImageView = (TransportationTypeImageView) view.findViewById(R.id.ResultRowIcon);
			TextView destinationTextView = (TextView) view.findViewById(R.id.ResultRowDestination);
			TextView timeTableTextView = (TextView) view.findViewById(R.id.ResultRowTimeTableTime);
			TextView expectedTimeTextView = (TextView) view.findViewById(R.id.ResultRowExpectedTime);
			TableRow expectedTableRow = (TableRow) view.findViewById(R.id.ResultRowExpectedTimeRow2);
			TextView expectedTimeTextLabel = (TextView) view.findViewById(R.id.ResultRowExpectedTimeText);

			if (_transportationTypeId == 5)
			{
				destinationTextView.setText(departure.getLine() + " - " + departure.getDestination());
			}
			else
			{
				destinationTextView.setText(departure.getDestination());
			}
			if (departure.getTimeTabledDateTime() != null)
			{
				timeTableTextView.setText(_timeTableSimpleDateFormat.format(departure.getTimeTabledDateTime()));
			}
			if (departure.getExpectedDateTime() != null && departure.getTimeTabledDateTime() != null)
			{
				long delay = (departure.getExpectedDateTime().getTime() - departure.getTimeTabledDateTime().getTime()) / 1000;
				if (delay > 0 && (delay > 60 || Settings.getInstance().showShortDelays()))
				{
					expectedTableRow.setVisibility(View.VISIBLE);
					if (Settings.getInstance().showArrivalTime())
					{
						if (Settings.getInstance().showShortDelays())
						{
							expectedTimeTextView.setText(_delaySimpleDateFormat.format(departure.getExpectedDateTime()));
						}
						else
						{
							expectedTimeTextView.setText(_timeTableSimpleDateFormat.format(departure.getExpectedDateTime()));
						}
					}
					else
					{
						long minutes = delay / 60;
						delay -= minutes * 60;

						if (Settings.getInstance().showShortDelays())
						{
							expectedTimeTextView.setText(minutes + "m" + delay + "s (" + _delaySimpleDateFormat.format(departure.getExpectedDateTime()) + ")");
						}
						else
						{
							expectedTimeTextView.setText(minutes + "m (" + _timeTableSimpleDateFormat.format(departure.getExpectedDateTime()) + ")");
						}
						expectedTimeTextLabel.setText(R.string.ResultRowDeviationText);
					}
				}
				else
				{
					expectedTableRow.setVisibility(View.GONE);
				}
			}
			else
			{
				expectedTableRow.setVisibility(View.GONE);
			}
			iconImageView.setTransportationTypeId(_transportationTypeId, departure.getLine());
		}
		return view;
	}
}
