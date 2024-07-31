package com.vgsoftware.android.autosportrss.service;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.vgsoftware.android.autosportrss.FeedSync;
import com.vgsoftware.android.autosportrss.Log;
import com.vgsoftware.android.autosportrss.Settings;
import com.vgsoftware.android.autosportrss.Utility;
import com.vgsoftware.android.autosportrss.FeedSync.OnFeedSyncFinishedListener;
import com.vgsoftware.android.autosportrss.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.autosportrss.dataabstraction.FeedItem;
import com.vgsoftware.android.autosportrss.ui.AutosportRSS;
import com.vgsoftware.android.autosportrss.ui.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class UpdateService extends Service implements OnFeedSyncFinishedListener, OnSharedPreferenceChangeListener
{
	private static final int NEW_ITEMS_NOTIFICATION_ID = 100;

	private ScheduledThreadPoolExecutor _scheduler;
	private OnFeedsUpdated _listener;
	private FeedDroidDB _database;
	private boolean _serviceSetup;

	public void setOnFeedsUpdated(OnFeedsUpdated listener)
	{
		_listener = listener;
	}

	public interface OnFeedsUpdated
	{
		void FeedsUpdated(List<FeedItem> newFeedItems);
	}

	@Override
	public void onStart(Intent intent, int startid)
	{
		setupService();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		setupService();
		return mBinder;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (_scheduler != null)
		{
			_scheduler.shutdown();
		}
	}

	private synchronized void setupService()
	{
		if (!_serviceSetup)
		{
			_database = new FeedDroidDB();
			PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
			setupUpdateScheduler();
			_serviceSetup = true;
		}
	}

	public class LocalBinder extends Binder
	{
		public UpdateService getService()
		{
			return UpdateService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	private void showNotification(int items)
	{
		int unreadFeedItems = _database.getUnreadFeedItemCount();
		String title = null;
		String description = null;
		if (items > 1)
		{
			title = Utility.stringFormat(getString(R.string.Service_NewItems_Title), items);
		}
		else
		{
			title = Utility.stringFormat(getString(R.string.Service_NewItems_Title_Singular), items);
		}
		if (unreadFeedItems > 1)
		{
			description = Utility.stringFormat(getString(R.string.Service_NewItems_Description), unreadFeedItems);
		}
		else
		{
			description = Utility.stringFormat(getString(R.string.Service_NewItems_Description_Singular), unreadFeedItems);
		}

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_stat_notify_msg, title, System.currentTimeMillis());
		notification.number = unreadFeedItems;

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.defaults |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent notificationIntent = new Intent(this, AutosportRSS.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(getApplicationContext(), title, description, contentIntent);
		notificationManager.notify(UpdateService.NEW_ITEMS_NOTIFICATION_ID, notification);
	}

	@Override
	public void onFeedSyncFinished(List<FeedItem> newFeedItems)
	{
		if (_listener != null)
		{
			_listener.FeedsUpdated(newFeedItems);
		}
		if (newFeedItems.size() > 0)
		{
			showNotification(newFeedItems.size());
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if ("AutoUpdate".equals(key) || "UpdateInterval".equals(key))
		{
			setupUpdateScheduler();
		}
	}

	public void forceUpdate()
	{
		setupUpdateScheduler();
	}
	
	private void setupUpdateScheduler()
	{
		try
		{
			if (_scheduler != null)
			{
				_scheduler.shutdownNow();
			}
			if (Settings.getAutoUpdate())
			{
				_scheduler = new ScheduledThreadPoolExecutor(1);
				_scheduler.scheduleAtFixedRate(new Runnable()
				{
					@Override
					public void run()
					{
						FeedSync sync = new FeedSync(UpdateService.this);
						sync.start();
					}
				}, 0, Settings.getUpdateInterval() * 60, TimeUnit.SECONDS);
			}
		}
		catch (Exception ex)
		{
			Log.error("Exception when running update scheduler", ex);
		}
	}
}