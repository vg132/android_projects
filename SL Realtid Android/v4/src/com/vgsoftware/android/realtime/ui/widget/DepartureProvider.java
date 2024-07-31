package com.vgsoftware.android.realtime.ui.widget;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.DepartureSetting;
import com.vgsoftware.android.realtime.service.WidgetRemoteViewsService;
import com.vgsoftware.android.realtime.service.WidgetUpdateService;
import com.vgsoftware.android.realtime.ui.SLRealTimeActivity;
import com.vgsoftware.android.realtime.ui.factory.DepartureFactory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class DepartureProvider extends AppWidgetProvider
{
	public static final String REFRESH_ACTION = "com.vgsoftware.android.realtime.ui.widget.departure.REFRESH";
	private PendingIntent _service = null;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		for (int appWidgetId : appWidgetIds)
		{
			DepartureProvider.updateWidget(context, appWidgetManager, appWidgetId);
		}

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent serviceIntent = new Intent(context, WidgetUpdateService.class);

		if (_service == null)
		{
			_service = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		}
		alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 60, _service);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
	{
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_departure_list);

		final Intent updateIntent = new Intent(context, WidgetRemoteViewsService.class);
		updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		updateIntent.putExtra(WidgetRemoteViewsService.EXTRA_FACTORY_ID, DepartureFactory.FACTORY_ID);
		updateIntent.setData(Uri.parse(updateIntent.toUri(Intent.URI_INTENT_SCHEME)));
		views.setRemoteAdapter(android.R.id.list, updateIntent);
		views.setEmptyView(android.R.id.list, R.id.empty_list_view);

		// final Intent nextIntent = new Intent(context, DepartureProvider.class);
		// nextIntent.setAction(DepartureProvider.NEXT_ACTION);
		// nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		// final PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// views.setOnClickPendingIntent(android.R.id.button1, nextPendingIntent);

		final Intent refreshIntent = new Intent(context, DepartureProvider.class);

		Uri data = Uri.withAppendedPath(Uri.parse("com.vgsoftware.android.realtime.widget.departure://widget/id/"), String.valueOf(appWidgetId));
		refreshIntent.setData(data);

		refreshIntent.setAction(DepartureProvider.REFRESH_ACTION);
		refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(android.R.id.button1, refreshPendingIntent);

		final PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, SLRealTimeActivity.class), 0);
		views.setOnClickPendingIntent(android.R.id.title, appPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onDisabled(Context context)
	{
		if (_service != null)
		{
			final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(_service);
		}
		super.onDisabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		final String action = intent.getAction();
		if (action.equals(REFRESH_ACTION))
		{
			final int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, android.R.id.list);
		}
		// else if (action.equals(NEXT_ACTION))
		// {
		// RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_departure);
		// rv.showNext(R.id.page_flipper);
		// AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID), rv);
		// }

		super.onReceive(context, intent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		DataStore dataStore = new DataStore(context);
		for (int appWidgetId : appWidgetIds)
		{
			for (DepartureSetting setting : dataStore.listDepartureSettingsByWidgetId(appWidgetId))
			{
				dataStore.deleteDepartureSetting(setting);
			}
		}
		super.onDeleted(context, appWidgetIds);
	}
}
