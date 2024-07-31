package com.vgsoftware.android.realtime.ui.adapters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.FrameLayout;
import android.widget.TextView;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.Departure;
import com.vgsoftware.android.vglib.ui.adapter.MapAdapter;

public class DepartureAdapter extends MapAdapter<String, Departure>
{
	private final SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

	public DepartureAdapter(Context context, Map<String, List<Departure>> departures)
	{
		super(context, departures, R.layout.listview_row_departure_heading, R.layout.listview_row_departure);
	}

	@Override
	public View getHeadingView(int position, String heading, View convertView, ViewGroup parent)
	{
		View headingView = super.getHeadingView(position, heading, convertView, parent);
		((TextView) headingView.findViewById(android.R.id.text1)).setText(heading.split("\\|")[0]);
		return headingView;
	}

	@Override
	public View getItemView(int position, String key, Departure item, View convertView, ViewGroup parent)
	{
		View view = super.getItemView(position, key, item, convertView, parent);

//		final Button hide = (Button) view.findViewById(android.R.id.button1);

//		FrameLayout layout = (FrameLayout) view.findViewById(android.R.id.content);
//		layout.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View view)
//			{
//				if (hide.getVisibility() == View.GONE)
//				{
//					hide.setVisibility(View.VISIBLE);
//				}
//				else
//				{
//					hide.setVisibility(View.GONE);
//				}
//			}
//		});

		TextView destinationTextView = (TextView) view.findViewById(R.id.destination);
		TextView timeToDeparture = (TextView) view.findViewById(R.id.time_to_departure);

		TextView departureTimeHeading = (TextView) view.findViewById(R.id.departure_time_heading);
		TextView departureTime = (TextView) view.findViewById(R.id.departure_time);

		TextView delayHeading = (TextView) view.findViewById(R.id.delay_heading);
		TextView delayTime = (TextView) view.findViewById(R.id.delay_departure_time);

		departureTimeHeading.setVisibility(View.GONE);
		departureTime.setVisibility(View.GONE);
		delayHeading.setVisibility(View.GONE);
		delayTime.setVisibility(View.GONE);

		destinationTextView.setText(item.getLine() + " " + item.getDestination());

		if (item.getDisplayTime().equals("0 min"))
		{
			timeToDeparture.setText("Nu");
		}
		else
		{
			timeToDeparture.setText(item.getDisplayTime());
		}
		if (item.getExpectedDateTime() != null && item.getTimeTabledDateTime() != null)
		{
			long delay = (item.getExpectedDateTime().getTime() - item.getTimeTabledDateTime().getTime()) / 1000;
			if (delay >= 60)
			{
				delayTime.setText(_simpleDateFormat.format(item.getExpectedDateTime()));
				delayHeading.setVisibility(View.VISIBLE);
				delayTime.setVisibility(View.VISIBLE);
			}
			else if (!item.getDisplayTime().contains(":"))
			{
				departureTime.setText(_simpleDateFormat.format(item.getTimeTabledDateTime()));
				departureTimeHeading.setVisibility(View.VISIBLE);
				departureTime.setVisibility(View.VISIBLE);
			}
		}
		return view;
	}
}
