package com.vgsoftware.android.realtime.ui.factory;

import java.util.List;

import com.vgsoftware.android.realtime.Constants;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.TrafficStatus;
import com.vgsoftware.android.realtime.model.TrafficStatusSetting;
import com.vgsoftware.android.realtime.parse.TrafficStatusParser;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class TrafficStatusFactory implements RemoteViewsFactory
{
	public static final int FACTORY_ID = 20;
	private Context _context = null;
	private List<TrafficStatus> _trafficStatus = null;
	private TrafficStatusParser _parser = null;
	private int _appWidgetId;
	private DataStore _dataStore = null;

	public TrafficStatusFactory(Context context, Intent intent)
	{
		_context = context;
		_appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		_parser = new TrafficStatusParser();
		_dataStore = new DataStore(context);
	}

	@Override
	public int getCount()
	{
		return _trafficStatus != null ? _trafficStatus.size() : 0;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public RemoteViews getLoadingView()
	{
		RemoteViews row = new RemoteViews(_context.getPackageName(), android.R.layout.simple_list_item_1);
		row.setTextViewText(android.R.id.text1, "Laddar");
		row.setTextColor(android.R.id.text1, Color.GRAY);
		return row;
	}

	@Override
	public RemoteViews getViewAt(int position)
	{
		RemoteViews row = new RemoteViews(_context.getPackageName(), R.layout.widget_traffic_status_list_item);
		if (_trafficStatus.size() > position)
		{
			TrafficStatus trafficStatus = _trafficStatus.get(position);
			row.setTextViewText(android.R.id.text1, trafficStatus.getName());
			row.setTextColor(android.R.id.text1, Color.BLACK);
			row.setInt(android.R.id.text1, "setBackgroundResource", R.drawable.list_selector_background_light);

			//row.setTextViewText(android.R.id.text2, trafficStatus.getStatus());
			//row.setTextColor(android.R.id.text2, Color.BLACK);
			//row.setInt(android.R.id.text2, "setBackgroundResource", R.drawable.list_selector_background_light);

			if (trafficStatus.getTrafficEvents() == null || trafficStatus.getTrafficEvents().size() == 0)
			{
				row.setImageViewResource(android.R.id.icon, R.drawable.check);
			}
			else
			{
				row.setImageViewResource(android.R.id.icon, R.drawable.attention);
			}

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.putExtra(Constants.INTENT_EXTRA_SELECT_TAB, 2);
			row.setOnClickFillInIntent(android.R.id.widget_frame, intent);
		}
		return row;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public void onCreate()
	{
	}

	@Override
	public void onDataSetChanged()
	{
		LogManager.debug("onDataSetChanged: " + _appWidgetId);
		_trafficStatus = _parser.parseTrafficStatus();
		TrafficStatusSetting settings = _dataStore.getTrafficStatusSetting(_appWidgetId);
		if (settings != null)
		{
			LogManager.debug("Settings is NOT null");
			for (int i = _trafficStatus.size() - 1; i >= 0; i--)
			{
				if (_trafficStatus.get(i).getName().equals("Tunnelbana") && !settings.isShowSubway())
				{
					_trafficStatus.remove(i);
				}
				else if (_trafficStatus.get(i).getName().equals("Pendeltåg") && !settings.isShowTrain())
				{
					_trafficStatus.remove(i);
				}
				else if (_trafficStatus.get(i).getName().equals("Lokalbana") && !settings.isShowTram())
				{
					_trafficStatus.remove(i);
				}
				else if (_trafficStatus.get(i).getName().equals("Spårvagn") && !settings.isShowTram2())
				{
					_trafficStatus.remove(i);
				}
				else if (_trafficStatus.get(i).getName().equals("Bussar") && !settings.isShowBus())
				{
					_trafficStatus.remove(i);
				}
				else if (_trafficStatus.get(i).getName().equals("Båtar") && !settings.isShowBoat())
				{
					_trafficStatus.remove(i);
				}
			}
		}
		else
		{
			LogManager.debug("Settings is null");
		}
	}

	@Override
	public void onDestroy()
	{
	}
}
