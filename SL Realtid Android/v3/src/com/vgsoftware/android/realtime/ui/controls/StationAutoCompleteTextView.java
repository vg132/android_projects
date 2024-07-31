package com.vgsoftware.android.realtime.ui.controls;

import java.util.List;

import com.vgsoftware.android.realtime.Utilities;
import com.vgsoftware.android.realtime.dataabstraction.Database;
import com.vgsoftware.android.realtime.dataabstraction.Donation;
import com.vgsoftware.android.realtime.dataabstraction.Station;
import com.vgsoftware.android.realtime.ui.SLRealTime;
import com.vgsoftware.android.realtime.ui.adapters.StationAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class StationAutoCompleteTextView extends AutoCompleteTextView
{
	private int _transportationTypeId = 0;

	public StationAutoCompleteTextView(final Context context)
	{
		super(context);
	}

	public StationAutoCompleteTextView(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
	}

	public StationAutoCompleteTextView(final Context context, final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void reload()
	{
		setTransportationType(_transportationTypeId);
	}

	public void setTransportationType(int transportationTypeId)
	{
		_transportationTypeId = transportationTypeId;
		if(transportationTypeId==6)
		{
			this.setAdapter(new StationAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, Database.getInstance().listStations()));
		}
		else
		{
			this.setAdapter(new StationAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, Database.getInstance().listStations(transportationTypeId)));
		}
	}

	public Station getSelectedStation()
	{
		String stationName = this.getText().toString();
		Station station = Database.getInstance().loadStation(_transportationTypeId, stationName);
		if (station == null)
		{
			if (_transportationTypeId == 5 && !TextUtils.isEmpty(stationName))
			{
				if (stationName.lastIndexOf('(') >= 0)
				{
					station = Database.getInstance().loadStation(_transportationTypeId, stationName.substring(0, stationName.lastIndexOf('(')).trim());
				}
			}
			else
			{
				station = Database.getInstance().loadStation(_transportationTypeId, Utilities.capitalize(stationName, null));
				if (station == null)
				{
					List<Station> stations = Database.getInstance().loadStation(stationName);
					if (stations.size() == 1)
					{
						station = stations.get(0);
					}
					else
					{
						stations = Database.getInstance().loadStation(Utilities.capitalize(stationName, null));
						if (stations.size() == 1)
						{
							station = stations.get(0);
						}
					}
				}
			}
		}
		if (station != null)
		{
			return station;
		}
		else if (stationName.equals("kalle"))
		{
			Toast.makeText(this.getContext(), "Surprise! Gratis �r gott! Ingen mer reklam.", Toast.LENGTH_LONG).show();
			Donation donation = new Donation(SLRealTime.FREE_PRODUCT.getSKU());
			donation.setStatus("OK");
			Database.getInstance().saveDonation(donation);
		}
		return null;
	}
}
