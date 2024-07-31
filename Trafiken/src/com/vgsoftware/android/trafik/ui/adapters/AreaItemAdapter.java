package com.vgsoftware.android.trafik.ui.adapters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vgsoftware.android.trafik.dataabstraction.Camera;
import com.vgsoftware.android.trafik.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.trafik.dataabstraction.Feed;
import com.vgsoftware.android.trafik.R;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AreaItemAdapter extends BaseAdapter
{
	private Context _context = null;
	private Map<String, Pair<Integer, Integer>> _areas = null;
	private List<String> _areaNames = null;
	private Feed _feed = null;

	public AreaItemAdapter(Context context, Feed feed)
	{
		_context = context;
		_areas = new HashMap<String, Pair<Integer, Integer>>();
		_areaNames = new ArrayList<String>();
		_feed = feed;
		try
		{
			for (Camera camera : DatabaseHelper.getHelper(context).getCameraDao().queryBuilder().where().eq("feedId", feed.getId()).query())
			{
				if (!_areas.containsKey(camera.getArea()))
				{
					if (camera.getActive())
					{
						_areas.put(camera.getArea(), new Pair<Integer, Integer>(1, 1));
					}
					else
					{
						_areas.put(camera.getArea(), new Pair<Integer, Integer>(1, 0));
					}
					_areaNames.add(camera.getArea());
				}
				else
				{
					Pair<Integer, Integer> c = _areas.get(camera.getArea());
					_areas.remove(camera.getArea());
					_areas.put(camera.getArea(), new Pair<Integer, Integer>(c.first + 1, c.second));
				}
				if (camera.getActive())
				{
					Pair<Integer, Integer> c = _areas.get(camera.getArea());
					_areas.remove(camera.getArea());
					_areas.put(camera.getArea(), new Pair<Integer, Integer>(c.first + 1, c.second + 1));
				}
			}
		}
		catch (SQLException ex)
		{
		}
	}

	public Feed getFeed()
	{
		return _feed;
	}

	@Override
	public int getCount()
	{
		return _areaNames.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _areaNames.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final String area = (String) getItem(position);
		if (area != null)
		{
			View view = LayoutInflater.from(_context).inflate(R.layout.area_item_row, parent, false);
			TextView areaTextView = (TextView) view.findViewById(R.id.areaNameTextView);
			areaTextView.setText(area + " (" + _areas.get(area).first.toString() + "/" + _areas.get(area).second.toString() + ")");
			return view;
		}
		return convertView;
	}
}
