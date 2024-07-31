package com.vgsoftware.android.trafik;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.vgsoftware.android.feedlib.FeedParser;
import com.vgsoftware.android.feedlib.IFeed;
import com.vgsoftware.android.feedlib.IFeedItem;
import com.vgsoftware.android.trafik.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.trafik.dataabstraction.Feed;
import com.vgsoftware.android.trafik.dataabstraction.FeedItem;
import com.vgsoftware.android.vglib.GoogleAnalytics;
import com.vgsoftware.android.vglib.StringUtility;

public class FeedSync extends Thread
{
	private static int MaximumItems = 150;
	private static boolean isRunning = false;
	private Context _context = null;
	private List<Feed> _feedsToSync = null;

	private OnFeedSyncFinishedListener _listener;

	public FeedSync(Context context)
	{
		_context = context;
	}

	public FeedSync(Context context, OnFeedSyncFinishedListener listener)
	{
		_context = context;
		_listener = listener;
	}

	public List<FeedItem> updateFeeds()
	{
		Log.debug("updateFeed");
		DatabaseHelper databaseHelper = OpenHelperManager.getHelper(_context, DatabaseHelper.class);
		List<FeedItem> newFeedItems = new ArrayList<FeedItem>();

		if (!isRunning)
		{
			try
			{
				isRunning = true;
				for (Feed feed : getFeedsToSync())
				{
					Log.debug("Feed" + feed.getName());
					IFeed rssFeed = FeedParser.parse(feed.getUrl());
					if (rssFeed != null)
					{
						for (IFeedItem item : rssFeed.getFeedItems())
						{
							PreparedQuery<FeedItem> query = databaseHelper.getFeedItemDao().queryBuilder().where().eq("title", item.getTitle().trim()).prepare();
							if (databaseHelper.getFeedItemDao().queryForFirst(query) == null)
							{
								FeedItem feedItem = new FeedItem();
								feedItem.setFeedId(feed.getId());
								feedItem.setTitle(item.getTitle().trim());
								feedItem.setDescription(StringUtility.stipHtml(item.getDescription()).trim());
								feedItem.setUrl(item.getLink());
								feedItem.setAdded(new Date());
								feedItem.setRead(false);
								if (item.getDate() != null)
								{
									feedItem.setDate(item.getDate());
								}
								else
								{
									feedItem.setDate(new Date());
								}
								databaseHelper.getFeedItemDao().createOrUpdate(feedItem);
								newFeedItems.add(feedItem);
							}
						}
					}
				}
				trimFeeds();
			}
			catch (Exception ex)
			{
				GoogleAnalytics.getInstance(_context).trackEvent("sql exception", "update feeds", ex.getMessage());
				Log.error("Exception when updating feeds.", ex);
			}
			finally
			{
				isRunning = false;
			}
		}
		Log.debug("update sync done");
		return newFeedItems;
	}

	private void trimFeeds()
	{
		for (Feed feed : DatabaseHelper.getHelper(_context).getFeedDao().queryForAll())
		{
			try
			{
				List<FeedItem> _feedItems = DatabaseHelper.getHelper(_context).getFeedItemDao().queryBuilder().orderBy("date", false).where().eq("feedId", feed.getId()).query();
				if (_feedItems.size() > FeedSync.MaximumItems)
				{
					for (int i = FeedSync.MaximumItems; i < _feedItems.size(); i++)
					{
						DatabaseHelper.getHelper(_context).getFeedItemDao().delete(_feedItems.get(i));
					}
				}
				Log.debug("Feed name: " + feed.getName() + ", Feed items: " + _feedItems.size());
			}
			catch (SQLException ex)
			{
				GoogleAnalytics.getInstance(_context).trackEvent("sql exception", "trim feeds", ex.getMessage());
				Log.debug("Error: " + ex.getMessage());
			}
		}
	}

	@Override
	public void run()
	{
		Log.debug("run start");
		List<FeedItem> newFeedItems = updateFeeds();
		Log.debug("run end");
		if (_listener != null)
		{
			_listener.onFeedSyncFinished(newFeedItems);
		}
	}

	public void setOnFeedSyncFinishedListener(OnFeedSyncFinishedListener listener)
	{
		_listener = listener;
	}

	public interface OnFeedSyncFinishedListener
	{
		public void onFeedSyncFinished(List<FeedItem> newItems);
	}

	public void addFeedToSync(Feed feed)
	{
		if (_feedsToSync == null)
		{
			_feedsToSync = new ArrayList<Feed>();
		}
		_feedsToSync.add(feed);
	}

	public List<Feed> getFeedsToSync()
	{
		if (_feedsToSync != null)
		{
			return _feedsToSync;
		}
		return DatabaseHelper.getHelper(_context).getFeedDao().queryForAll();
	}
}
