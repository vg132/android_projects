package com.vgsoftware.android.realtime.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
		{
			Intent serviceIntent = new Intent(context, WidgetUpdateService.class);
			serviceIntent.setAction(Intent.ACTION_SCREEN_ON);
			context.startService(serviceIntent);
		}
	}
}
