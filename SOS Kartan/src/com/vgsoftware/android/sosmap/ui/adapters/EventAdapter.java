package com.vgsoftware.android.sosmap.ui.adapters;

import java.util.List;

import com.vgsoftware.android.sosmap.Event;
import com.vgsoftware.android.sosmap.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter<Event>
{
	private List<Event> _events = null;
	private LayoutInflater _layoutInflater = null;
	
	public EventAdapter(Context context, int resource, List<Event> events)
	{
		super(context, resource, events);
		_events = events;
		_layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		if (view == null)
		{
			view = _layoutInflater.inflate(R.layout.list_row, parent, false);
		}
		Event event = _events.get(position);
		if (event != null)
		{
			TextView eventHeading = (TextView) view.findViewById(R.id.eventHeading);
			TextView eventTime = (TextView) view.findViewById(R.id.eventTime);
			TextView eventLocation = (TextView)view.findViewById(R.id.eventLocation);
			TextView eventCounty = (TextView)view.findViewById(R.id.eventCounty);

			eventHeading.setText(event.getIssue());
			eventTime.setText(event.getTime());
			eventLocation.setText(event.getLocation());
			eventCounty.setText(event.getCounty());
		}
		return view;
	}
}
