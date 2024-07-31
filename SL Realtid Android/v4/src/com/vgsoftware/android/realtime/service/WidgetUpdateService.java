package com.vgsoftware.android.realtime.service;

import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.DatabaseHelper;
import com.vgsoftware.android.realtime.model.DepartureSetting;
import com.vgsoftware.android.realtime.model.TrafficStatusSetting;
import com.vgsoftware.android.realtime.ui.widget.DepartureProvider;
import com.vgsoftware.android.realtime.ui.widget.TrafficStatusProvider;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;

public class WidgetUpdateService extends OrmLiteBaseService<DatabaseHelper>
{
	private DataStore _dataStore = null;
	private final static long TrafficStatusTimeout = (10 * 60 * 1000) - 500;
	private final static long DepartureTimeout = (1 * 60 * 1000) - 500;

	private ScreenReceiver _screenReceiver = null;

	@Override
	public void onCreate()
	{
		super.onCreate();
		_dataStore = new DataStore(getApplicationContext());

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		if (_screenReceiver == null)
		{
			_screenReceiver = new ScreenReceiver();
		}
		registerReceiver(_screenReceiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		buildUpdate(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	private void buildUpdate(Intent intent)
	{
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		if (!powerManager.isScreenOn())
		{
			return;
		}
		updateTrafficStatusWidget();
		updateDepartureWidget();
	}

	private void updateDepartureWidget()
	{
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, DepartureProvider.class));

		for (int widgetId : ids)
		{
			List<DepartureSetting> settings = _dataStore.listDepartureSettingsByWidgetId(widgetId);
			for (DepartureSetting setting : settings)
			{
				if (setting.isAutoUpdate() && setting.getNextExecution() <= System.currentTimeMillis())
				{
					appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, android.R.id.list);
					setting.setNextExecution(System.currentTimeMillis() + WidgetUpdateService.DepartureTimeout);
					_dataStore.saveDepartureSetting(setting);
				}
			}
		}
	}

	private void updateTrafficStatusWidget()
	{
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, TrafficStatusProvider.class));
		for (int widgetId : ids)
		{
			TrafficStatusSetting setting = _dataStore.getTrafficStatusSetting(widgetId);
			if (setting != null && setting.getNextExecution() <= System.currentTimeMillis())
			{
				appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, android.R.id.list);
				setting.setNextExecution(System.currentTimeMillis() + WidgetUpdateService.TrafficStatusTimeout);
				_dataStore.saveTrafficStatusSetting(setting);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		unregisterReceiver(_screenReceiver);
		super.onDestroy();
	}
}
