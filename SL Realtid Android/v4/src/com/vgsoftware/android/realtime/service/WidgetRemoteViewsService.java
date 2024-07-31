package com.vgsoftware.android.realtime.service;

import com.vgsoftware.android.realtime.ui.factory.FavoriteListFactory;
import com.vgsoftware.android.realtime.ui.factory.TrafficStatusFactory;
import com.vgsoftware.android.realtime.ui.factory.DepartureFactory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViewsService;

public class WidgetRemoteViewsService extends RemoteViewsService
{
	public static final String EXTRA_FACTORY_ID = "FactoryId";

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent)
	{
		Bundle extras = intent.getExtras();
		int factoryId = extras.getInt(EXTRA_FACTORY_ID);
		switch (factoryId)
		{
			case FavoriteListFactory.FACTORY_ID:
				return new FavoriteListFactory(getApplicationContext(), intent);
			case TrafficStatusFactory.FACTORY_ID:
				return new TrafficStatusFactory(getApplicationContext(), intent);
			case DepartureFactory.FACTORY_ID:
				return new DepartureFactory(getApplicationContext(), intent);
		}
		return null;
	}
}
