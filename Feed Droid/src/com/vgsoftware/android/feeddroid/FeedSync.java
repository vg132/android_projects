package com.vgsoftware.android.feeddroid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vgsoftware.android.feeddroid.dataabstraction.Feed;
import com.vgsoftware.android.feeddroid.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.feeddroid.dataabstraction.FeedItem;
import com.vgsoftware.android.feedlib.FeedParser;
import com.vgsoftware.android.feedlib.IFeed;
import com.vgsoftware.android.feedlib.IFeedItem;

public class FeedSync extends Thread
{
	private static boolean IsRunning;

	private OnFeedSyncFinishedListener _listener;

	public FeedSync()
	{
	}

	public FeedSync(OnFeedSyncFinishedListener listener)
	{
		_listener = listener;
	}

	public static List<FeedItem> updateFeeds()
	{
		List<FeedItem> newFeedItems = new ArrayList<FeedItem>();
		try
		{
			if (!IsRunning)
			{
				IsRunning = true;
				FeedDroidDB db = new FeedDroidDB();
				for (Feed feed : db.listActiveFeeds())
				{
					IFeed rssFeed = FeedParser.parse(feed.getUrl());
					if (rssFeed != null)
					{
						for (IFeedItem item : rssFeed.getFeedItems())
						{
							if (db.loadFeedItem(feed.getId(), item.getLink()) == null)
							{
								FeedItem feedItem = new FeedItem();
								feedItem.setFeedId(feed.getId());
								feedItem.setTitle(item.getTitle());
								feedItem.setDescription(item.getDescription());
								feedItem.setUrl(item.getLink());
								feedItem.setAdded(new Date());
								if (item.getDate() != null)
								{
									feedItem.setDate(item.getDate());
								}
								else
								{
									feedItem.setDate(new Date());
								}
								db.saveFeedItem(feedItem);
								newFeedItems.add(feedItem);
							}
						}
					}
				}
				db.cleanupFeedItems(Settings.getClearInterval());
			}
		}
		finally
		{
			IsRunning = false;
		}
		return newFeedItems;
	}

	@Override
	public void run()
	{
		List<FeedItem> newFeedItems = FeedSync.updateFeeds();
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
}
