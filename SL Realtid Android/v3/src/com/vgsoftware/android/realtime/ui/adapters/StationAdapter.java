package com.vgsoftware.android.realtime.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.realtime.dataabstraction.Station;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class StationAdapter extends ArrayAdapter<Station> implements Filterable
{
	private List<Station> _stations = null;
	private List<Station> _filteredStations = null;
	private LayoutInflater _layoutInflater = null;

	public StationAdapter(Context context, int textViewResourceId, List<Station> stations)
	{
		super(context, textViewResourceId, stations);
		_stations = stations;
		_filteredStations = stations;
		_layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		if (_filteredStations != null)
		{
			return _filteredStations.size();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public Station getItem(int position)
	{
		return _filteredStations.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return _filteredStations.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		if (view == null)
		{
			view = _layoutInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
		}
		Station station = _filteredStations.get(position);
		if (station != null)
		{
			TextView stationTextView = (TextView) view.findViewById(android.R.id.text1);
			stationTextView.setText(station.toString());
		}
		return view;
	}

	private StationFilter _filter = new StationFilter();

	@Override
	public Filter getFilter()
	{
		return _filter;
	}

	private class StationFilter extends Filter
	{
		@Override
		protected FilterResults performFiltering(CharSequence constraint)
		{
			FilterResults results = new FilterResults();
			List<Station> values = new ArrayList<Station>();
			if (constraint != null)
			{
				String inputText = fixTextForCompare(constraint.toString());
				boolean added = false;
				for (Station station : _stations)
				{
					added = false;
					String name = fixTextForCompare(station.getName());
					if (!added && name.startsWith(inputText))
					{
						values.add(station);
						added = true;
					}
					if (!added && isMatch(inputText, name.split("/")))
					{
						values.add(station);
						added = true;
					}
					if (!added && isMatch(inputText, fixTextForCompare(station.getAlias()).split(",")))
					{
						values.add(station);
						added = true;
					}
				}
				results.values = values;
				results.count = values.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			if (results != null)
			{
				_filteredStations = (List<Station>) results.values;
				notifyDataSetChanged();
			}
		}

		private String fixTextForCompare(String text)
		{
			if (text != null)
			{
				return text.toLowerCase().replace('å', 'a').replace('ä', 'a').replace('ö', 'o');
			}
			return "";
		}

		private boolean isMatch(String inputText, String[] stationNames)
		{
			for (String stationName : stationNames)
			{
				if (stationName.startsWith(inputText))
				{
					return true;
				}
			}
			return false;
		}
	}
}
