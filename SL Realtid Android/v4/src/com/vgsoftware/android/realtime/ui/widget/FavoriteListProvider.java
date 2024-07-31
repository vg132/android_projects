package com.vgsoftware.android.realtime.ui.widget;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.service.WidgetRemoteViewsService;
import com.vgsoftware.android.realtime.ui.DepartureActivity;
import com.vgsoftware.android.realtime.ui.SLRealTimeActivity;
import com.vgsoftware.android.realtime.ui.factory.FavoriteListFactory;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class FavoriteListProvider extends AppWidgetProvider
{
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		for (int i = 0; i < appWidgetIds.length; i++)
		{
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_favorite_list);
			Intent updateIntent = new Intent(context, WidgetRemoteViewsService.class);

			updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			updateIntent.putExtra(WidgetRemoteViewsService.EXTRA_FACTORY_ID, FavoriteListFactory.FACTORY_ID);
			updateIntent.setData(Uri.parse(updateIntent.toUri(Intent.URI_INTENT_SCHEME)));
			views.setRemoteAdapter(android.R.id.list, updateIntent);
			views.setEmptyView(android.R.id.list, R.id.empty_list_view);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], android.R.id.list);

			PendingIntent departurePendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, DepartureActivity.class), 0);
			views.setPendingIntentTemplate(android.R.id.list, departurePendingIntent);

			PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, SLRealTimeActivity.class), 0);
			views.setOnClickPendingIntent(android.R.id.title, appPendingIntent);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
