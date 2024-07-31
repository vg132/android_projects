package com.vgsoftware.android.realtime.ui.widget;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.TrafficStatusSetting;
import com.vgsoftware.android.realtime.service.WidgetRemoteViewsService;
import com.vgsoftware.android.realtime.service.WidgetUpdateService;
import com.vgsoftware.android.realtime.ui.SLRealTimeActivity;
import com.vgsoftware.android.realtime.ui.factory.TrafficStatusFactory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class TrafficStatusProvider extends AppWidgetProvider
{
	private PendingIntent _service = null;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		for (int widgetId : appWidgetIds)
		{
			TrafficStatusProvider.updateWidget(context, appWidgetManager, widgetId);
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

	public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId)
	{
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_traffic_status_list);
		Intent updateIntent = new Intent(context, WidgetRemoteViewsService.class);

		updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		updateIntent.putExtra(WidgetRemoteViewsService.EXTRA_FACTORY_ID, TrafficStatusFactory.FACTORY_ID);
		updateIntent.setData(Uri.parse(updateIntent.toUri(Intent.URI_INTENT_SCHEME)));
		views.setRemoteAdapter(android.R.id.list, updateIntent);
		views.setEmptyView(android.R.id.list, R.id.empty_list_view);
		appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, android.R.id.list);

		PendingIntent departurePendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, SLRealTimeActivity.class), 0);
		views.setPendingIntentTemplate(android.R.id.list, departurePendingIntent);

		PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, SLRealTimeActivity.class), 0);
		views.setOnClickPendingIntent(android.R.id.title, appPendingIntent);

		appWidgetManager.updateAppWidget(widgetId, views);
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
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		DataStore dataStore = new DataStore(context);
		for (int appWidgetId : appWidgetIds)
		{
			TrafficStatusSetting settings = dataStore.getTrafficStatusSetting(appWidgetId);
			if (settings != null)
			{
				dataStore.deleteTrafficStatusSetting(settings);
			}
		}
		super.onDeleted(context, appWidgetIds);
	}
}