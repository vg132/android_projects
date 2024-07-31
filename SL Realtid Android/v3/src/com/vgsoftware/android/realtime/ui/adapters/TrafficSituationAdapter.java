package com.vgsoftware.android.realtime.ui.adapters;

import java.util.ArrayList;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.parse.TrafficEvent;
import com.vgsoftware.android.realtime.parse.TrafficSituation;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TrafficSituationAdapter extends BaseExpandableListAdapter
{
	private ArrayList<TrafficSituation> _trafficSituations = null;
	private LayoutInflater _layoutInflater = null;
	private Context _context = null;

	public TrafficSituationAdapter(Context context, ArrayList<TrafficSituation> trafficSituations)
	{
		_context = context;
		if (trafficSituations != null)
		{
			_trafficSituations = trafficSituations;
		}
		else
		{
			_trafficSituations = new ArrayList<TrafficSituation>();
		}
		_layoutInflater = LayoutInflater.from(context);
	}

	public int getGroupCount()
	{
		return _trafficSituations.size();
	}

	public int getChildrenCount(int groupPosition)
	{
		int size = _trafficSituations.get(groupPosition).getTrafficEvents().size();
		if (size == 0)
		{
			size = 1;
		}
		return size;
	}

	public Object getGroup(int groupPosition)
	{
		return _trafficSituations.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition)
	{
		if (_trafficSituations.get(groupPosition).getTrafficEvents().size() > 0)
		{
			return _trafficSituations.get(groupPosition).getTrafficEvents().get(childPosition);
		}
		TrafficEvent event = new TrafficEvent();
		event.setMessage(_context.getString(R.string.TrafficSituationNoEvents));
		return event;
	}

	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	public boolean hasStableIds()
	{
		return false;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		TrafficSituation trafficSituation = (TrafficSituation) getGroup(groupPosition);
		View view = _layoutInflater.inflate(R.layout.expandable_view_heading, parent, false);
		//view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 32));

		TextView textView = (TextView) view.findViewById(R.id.textView);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setText(trafficSituation.getName());

		ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
		if (trafficSituation.getTrafficEvents().size() == 0)
		{
			imageView.setImageResource(R.drawable.traffic_situation_ok);
		}
		else
		{
			imageView.setImageResource(R.drawable.traffic_situation_not_ok);
		}
		return view;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		TextView textView = new TextView(parent.getContext());

		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding(36, 10, 0, 10);

		if (getChild(groupPosition, childPosition) != null)
		{
			TrafficEvent event = (TrafficEvent) getChild(groupPosition, childPosition);
			if (TextUtils.isEmpty(event.getLine()))
			{
				textView.setText(event.getMessage());
			}
			else
			{
				textView.setText(event.getLine() + ": " + event.getMessage());
			}
		}
		else
		{
			textView.setText(R.string.TrafficSituationNoEvents);
		}
		textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, textView.getHeight()));
		return textView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}
}
