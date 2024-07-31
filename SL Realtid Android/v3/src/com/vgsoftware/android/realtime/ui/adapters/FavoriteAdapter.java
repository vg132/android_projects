package com.vgsoftware.android.realtime.ui.adapters;

import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.Utilities;
import com.vgsoftware.android.realtime.dataabstraction.Favorite;
import com.vgsoftware.android.realtime.dataabstraction.Database;
import com.vgsoftware.android.realtime.dataabstraction.Station;

public class FavoriteAdapter extends ArrayAdapter<Favorite>
{
	private static LogManager _log = null;

	public FavoriteAdapter(Context context, int resource, int textViewResourceId, List<Favorite> favorites)
	{
		super(context, resource, textViewResourceId, favorites);
		_log = new LogManager(context);

		this.sort(new Comparator<Favorite>()
		{
			public int compare(Favorite lhs, Favorite rhs)
			{
				return lhs.getStation().getName().compareTo(rhs.getStation().getName());
			}
		});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = super.getView(position, convertView, parent);
		Favorite favorite = getItem(position);
		if (favorite != null)
		{
			_log.info(favorite.toString());
			TextView textView = (TextView) view.findViewById(android.R.id.text1);
			Station station = Database.getInstance().loadStation(favorite.getStationId());
			if (station != null)
			{
				textView.setText(station.getName());
			}
			textView.setCompoundDrawablesWithIntrinsicBounds(Utilities.getTransportationTypeDrawable(favorite.getTransportationTypeId(), "-1"), 0, 0, 0);

			int padding = (int) (5 * this.getContext().getResources().getDisplayMetrics().density + 0.5f);
			textView.setCompoundDrawablePadding(padding);
		}
		return view;
	}

	@Override
	public long getItemId(int position)
	{
		Favorite favorite = getItem(position);
		return favorite.getId();
	}
}
