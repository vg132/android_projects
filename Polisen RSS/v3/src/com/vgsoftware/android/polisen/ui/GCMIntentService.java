package com.vgsoftware.android.polisen.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;
import com.vgsoftware.android.polisen.FeedSync;
import com.vgsoftware.android.polisen.Log;
import com.vgsoftware.android.polisen.Preferences;
import com.vgsoftware.android.polisen.ServerUtilities;
import com.vgsoftware.android.polisen.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.FeedItem;
import com.vgsoftware.android.polisen.dataabstraction.Region;
import com.vgsoftware.android.polisen.ui.R;
import com.vgsoftware.android.vglib.NetworkUtility;

public class GCMIntentService extends GCMBaseIntentService
{
	public final static String SENDER_ID = "957753464103";
	private final static int NOTIFICATION_ID = 780926;

	public GCMIntentService()
	{
		super(GCMIntentService.SENDER_ID);
	}

	private void showNotification(int newItems)
	{
		if (!Preferences.getInstance(getApplicationContext()).getNotificationShow())
		{
			return;
		}
		int defaults = 0;
		if (Preferences.getInstance(getApplicationContext()).getNotificationSound())
		{
			defaults |= Notification.DEFAULT_SOUND;
		}
		if (Preferences.getInstance(getApplication()).getNotificationVibrate())
		{
			defaults |= Notification.DEFAULT_VIBRATE;
		}
		String contentTitle = "";
		if (newItems == 1)
		{
			contentTitle = String.format(getString(R.string.service_notification_title_single), newItems);
		}
		else
		{
			contentTitle = String.format(getString(R.string.service_notification_title), newItems);
		}
		String contentText = "";
		FeedItem feedItem = null;
		int unreadItems = 0;
		try
		{
			DatabaseHelper helper = DatabaseHelper.getHelper(getApplicationContext());
			List<FeedItem> feedItems = helper.getFeedItemDao().queryBuilder().orderBy("date", false).where().in("regionId", getActiveRegionIds(helper)).and().eq("read", false).query();
			if (feedItems.size() > 0)
			{
				unreadItems += feedItems.size();
				contentText = feedItems.get(0).getTitle();
				feedItem = feedItems.get(0);
			}
		}
		catch (SQLException ex)
		{
			Log.error("Unable to get first unread feed item", ex);
		}
		Intent intent = new Intent(this, PolisenRSS.class);
		if (feedItem != null)
		{
			intent.putExtra("FeedId", feedItem.getFeedId());
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplicationContext())
			.setContentTitle(contentTitle)
			.setContentText(contentText)
			.setAutoCancel(true)
			.setNumber(unreadItems)
			.setDefaults(defaults)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
			.setSmallIcon(R.drawable.notification)
			.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
			.setTicker(String.format(getString(R.string.service_notification_ticker), contentText));
		NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(GCMIntentService.NOTIFICATION_ID, nb.getNotification());
		sendBroadcast(new Intent(PolisenRSS.INTENT_UPDATE_DATA));
	}

	private List<Integer> getActiveRegionIds(DatabaseHelper helper)
		throws SQLException
	{
		List<Region> regions = null;
		if (Preferences.getInstance(getApplicationContext()).getAllRegions())
		{
			regions = helper.getRegionDao().queryForAll();
		}
		else
		{
			regions = helper.getRegionDao().queryBuilder().where().eq("active", true).query();
		}
		List<Integer> regionIds = new ArrayList<Integer>();
		for (Region region : regions)
		{
			regionIds.add(region.getId());
		}
		return regionIds;
	}

	@Override
	protected void onRegistered(Context context, String registrationId)
	{
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId)
	{
		ServerUtilities.unregister(context, registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent intent)
	{
		int newItems = 0;
		FeedSync feedSync = new FeedSync(getApplicationContext());

		for (Feed feed : DatabaseHelper.getHelper(getApplicationContext()).getActiveFeeds())
		{
			int items = parseMessage((String) intent.getExtras().get(feed.getCode()));
			newItems += items;
			if (items > 0)
			{
				feedSync.addFeedToSync(feed);
			}
		}
		if (newItems > 0)
		{
			if (Preferences.getInstance(getApplicationContext()).getAutomaticUpdate() == Preferences.AUTOMATIC_UPDATE_ALWAYS && NetworkUtility.isOnline(getApplicationContext()))
			{
				feedSync.updateFeeds();
			}
			else
			{
				Preferences.getInstance(getApplicationContext()).setOutOfSync(true);
			}
			showNotification(newItems);
		}
	}

	private int parseMessage(String message)
	{
		int news = 0;
		if (message == null || message.equals(""))
		{
			return news;
		}
		for (String regionId : message.split("\\|"))
		{
			try
			{
				Region region = DatabaseHelper.getHelper(getApplicationContext()).getRegionDao().queryForId(Integer.parseInt(regionId));
				if (region.getActive() || Preferences.getInstance(getApplicationContext()).getAllRegions())
				{
					news++;
				}
			}
			catch (NumberFormatException ex)
			{
				Log.warn("Unable to parse region id", ex);
			}
		}
		return news;
	}

	@Override
	protected void onError(Context context, String errorId)
	{
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId)
	{
		return super.onRecoverableError(context, errorId);
	}
}
