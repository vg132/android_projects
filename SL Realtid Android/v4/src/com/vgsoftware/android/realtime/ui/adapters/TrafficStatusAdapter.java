package com.vgsoftware.android.realtime.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.TrafficEvent;
import com.vgsoftware.android.realtime.model.TrafficStatus;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TrafficStatusAdapter extends BaseExpandableListAdapter
{
	private List<TrafficStatus> _trafficStatus = null;
	private LayoutInflater _layoutInflater = null;
	private Context _context = null;

	public TrafficStatusAdapter(Context context, List<TrafficStatus> trafficStatus)
	{
		_context = context;
		if (trafficStatus != null)
		{
			_trafficStatus = trafficStatus;
		}
		else
		{
			_trafficStatus = new ArrayList<TrafficStatus>();
		}
		_layoutInflater = LayoutInflater.from(context);
	}

	public int getGroupCount()
	{
		return _trafficStatus.size();
	}

	public int getChildrenCount(int groupPosition)
	{
		return _trafficStatus.get(groupPosition).getTrafficEvents().size();
	}

	public Object getGroup(int groupPosition)
	{
		return _trafficStatus.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition)
	{
		if (_trafficStatus.get(groupPosition).getTrafficEvents().size() > 0)
		{
			return _trafficStatus.get(groupPosition).getTrafficEvents().get(childPosition);
		}
		return null;
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
		View view = _layoutInflater.inflate(R.layout.expandable_heading_list_item, parent, false);
		if (getGroup(groupPosition) instanceof TrafficStatus)
		{
			TrafficStatus trafficStatus = (TrafficStatus) getGroup(groupPosition);
			TextView textView = (TextView) view.findViewById(android.R.id.text1);
			textView.setText(trafficStatus.getName());
			// textView = (TextView) view.findViewById(android.R.id.text2);
			// textView.setText(trafficStatus.getStatus());
			ImageView imageView = (ImageView) view.findViewById(android.R.id.icon);
			if (trafficStatus.getTrafficEvents() == null || trafficStatus.getTrafficEvents().size() == 0 ||
					(trafficStatus.getStatus() != null && trafficStatus.getStatus().equals("EventGood")))
			{
				imageView.setImageDrawable(_context.getResources().getDrawable(R.drawable.check));
			}
			else
			{
				imageView.setImageDrawable(_context.getResources().getDrawable(R.drawable.attention));
			}
		}
		return view;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		TrafficEvent event = (TrafficEvent) getChild(groupPosition, childPosition);
		View view = _layoutInflater.inflate(R.layout.expandable_list_item, parent, false);

		TextView lineOneTextView = (TextView) view.findViewById(android.R.id.text1);
		TextView lineTwoTextView = (TextView) view.findViewById(android.R.id.text2);

		if (TextUtils.isEmpty(event.getLine()))
		{
			lineOneTextView.setText(event.getMessage());
		}
		else
		{
			lineOneTextView.setText(event.getLine());
			lineTwoTextView.setText(event.getMessage());
		}
		return view;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}
}
