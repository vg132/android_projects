package com.vgsoftware.android.realtime.ui.factory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.vgsoftware.android.realtime.DepartureComparator;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.Departure;
import com.vgsoftware.android.realtime.model.DepartureSetting;
import com.vgsoftware.android.realtime.model.ISite;
import com.vgsoftware.android.realtime.parse.DepartureParser;
import com.vgsoftware.android.realtime.parse.DepartureParserResult;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class DepartureFactory implements RemoteViewsFactory
{
	private static final SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
	public static final int FACTORY_ID = 30;

	private Context _context = null;
	private int _appWidgetId;
	private DepartureSetting _departureSettings = null;
	private ISite _site = null;
	DepartureParser _parser = null;
	List<Departure> _departures = null;

	public DepartureFactory(Context context, Intent intent)
	{
		_context = context;
		_appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		DataStore dataStore = new DataStore(context);
		_departureSettings = dataStore.getDepartureSettingByWidgetId(_appWidgetId);
		if (_departureSettings != null)
		{
			_site = dataStore.getStation(_departureSettings.getSiteId());
		}
		_parser = new DepartureParser();
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDataSetChanged()
	{
		if (_departureSettings != null && _site != null)
		{
			DepartureParserResult result = _parser.parseDepartures(_site);
			_departures = result.get(_departureSettings.getTransportationType());
			if (_departures == null)
			{
				_departures = new ArrayList<Departure>();
			}
			Collections.sort(_departures, new DepartureComparator());
		}
	}

	@Override
	public void onDestroy()
	{
	}

	@Override
	public int getCount()
	{
		return _departures != null ? _departures.size() + 1 : 0;
	}

	@Override
	public RemoteViews getViewAt(int position)
	{
		RemoteViews row = null;
		if (position == 0 && _site != null)
		{
			row = new RemoteViews(_context.getPackageName(), R.layout.listview_row_departure_heading);
			row.setTextViewText(android.R.id.text1, _site.getName() + " - " + _simpleDateFormat.format(new Date(System.currentTimeMillis())));
		}
		else if (position > 0)
		{
			Departure departure = _departures.get(position - 1);
			row = new RemoteViews(_context.getPackageName(), R.layout.listview_row_departure);

			row.setViewVisibility(R.id.delay_heading, View.GONE);
			row.setViewVisibility(R.id.delay_departure_time, View.GONE);
			row.setViewVisibility(R.id.departure_time_heading, View.GONE);
			row.setViewVisibility(R.id.departure_time, View.GONE);
			row.setTextViewText(R.id.destination, departure.getLine() + " " + departure.getDestination());
			row.setTextViewText(R.id.time_to_departure, departure.getDisplayTime());
			if (departure.getExpectedDateTime() != null && departure.getTimeTabledDateTime() != null)
			{
				long delay = (departure.getExpectedDateTime().getTime() - departure.getTimeTabledDateTime().getTime()) / 1000;
				if (delay >= 60)
				{
					row.setTextViewText(R.id.delay_departure_time, _simpleDateFormat.format(departure.getExpectedDateTime()));
					row.setViewVisibility(R.id.delay_heading, View.VISIBLE);
					row.setViewVisibility(R.id.delay_departure_time, View.VISIBLE);
				}
				else
				{
					row.setTextViewText(R.id.departure_time, _simpleDateFormat.format(departure.getTimeTabledDateTime()));
					row.setViewVisibility(R.id.departure_time_heading, View.VISIBLE);
					row.setViewVisibility(R.id.departure_time, View.VISIBLE);
				}
			}
		}
		return row;
	}

	@Override
	public RemoteViews getLoadingView()
	{
		LogManager.debug("Departure Factory - getLoadingView");
		return new RemoteViews(_context.getPackageName(), R.layout.loading);
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}
}
