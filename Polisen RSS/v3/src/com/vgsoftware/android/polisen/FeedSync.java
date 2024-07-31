package com.vgsoftware.android.polisen;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.vgsoftware.android.feedlib.FeedParser;
import com.vgsoftware.android.feedlib.IFeed;
import com.vgsoftware.android.feedlib.IFeedItem;
import com.vgsoftware.android.polisen.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.FeedItem;

public class FeedSync extends Thread
{
	private static int MaximumItems = 150;
	private static boolean IsRunning;
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
		DatabaseHelper databaseHelper = OpenHelperManager.getHelper(_context, DatabaseHelper.class);
		List<FeedItem> newFeedItems = new ArrayList<FeedItem>();
		if (!IsRunning)
		{
			try
			{
				IsRunning = true;
				for (Feed feed : getFeedsToSync())
				{
					IFeed rssFeed = FeedParser.parse(feed.getUrl());
					if (rssFeed != null)
					{
						for (IFeedItem item : rssFeed.getFeedItems())
						{
							PreparedQuery<FeedItem> query = databaseHelper.getFeedItemDao().queryBuilder().where().eq("url", item.getLink()).prepare();
							if (databaseHelper.getFeedItemDao().queryForFirst(query) == null)
							{
								FeedItem feedItem = new FeedItem();
								feedItem.setFeedId(feed.getId());
								feedItem.setTitle(item.getTitle().trim());
								feedItem.setDescription(item.getDescription().trim());
								feedItem.setUrl(item.getLink());
								feedItem.setAdded(new Date());
								feedItem.setRead(false);
								feedItem.setRegionId(getRegionId(item.getLink().toLowerCase(Locale.getDefault())));
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
				Preferences.getInstance(_context).setOutOfSync(false);
				Preferences.getInstance(_context).setLastSyncDate(new Date());
			}
			catch (Exception ex)
			{
				Log.error("Exception when updating feeds.", ex);
			}
			finally
			{
				IsRunning = false;
			}
		}
		return newFeedItems;
	}

	private int getRegionId(String url)
	{
		if (url.contains("blekinge"))
		{
			return 1;
		}
		else if (url.contains("dalarna"))
		{
			return 2;
		}
		else if (url.contains("gotland"))
		{
			return 3;
		}
		else if (url.contains("gavleborg"))
		{
			return 4;
		}
		else if (url.contains("halland"))
		{
			return 5;
		}
		else if (url.contains("jamtland"))
		{
			return 6;
		}
		else if (url.contains("jonkoping"))
		{
			return 7;
		}
		else if (url.contains("kalmar"))
		{
			return 8;
		}
		else if (url.contains("kronoberg"))
		{
			return 9;
		}
		else if (url.contains("norrbotten"))
		{
			return 10;
		}
		else if (url.contains("skane"))
		{
			return 11;
		}
		else if (url.contains("stockholm"))
		{
			return 12;
		}
		else if (url.contains("sodermanland"))
		{
			return 13;
		}
		else if (url.contains("uppsala"))
		{
			return 14;
		}
		else if (url.contains("varmland"))
		{
			return 15;
		}
		else if (url.contains("vasterbotten"))
		{
			return 16;
		}
		else if (url.contains("vasternorrland"))
		{
			return 17;
		}
		else if (url.contains("vastmanland"))
		{
			return 18;
		}
		else if (url.contains("vastra-gotaland"))
		{
			return 19;
		}
		else if (url.contains("orebro"))
		{
			return 20;
		}
		else if (url.contains("ostergotland"))
		{
			return 21;
		}
		return 0;
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
				Log.debug("Error: " + ex.getMessage());
			}
		}
	}

	@Override
	public void run()
	{
		List<FeedItem> newFeedItems = updateFeeds();
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
		return DatabaseHelper.getHelper(_context).getActiveFeeds();
	}
}
