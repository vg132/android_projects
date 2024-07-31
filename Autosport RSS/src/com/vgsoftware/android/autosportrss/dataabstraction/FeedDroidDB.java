package com.vgsoftware.android.autosportrss.dataabstraction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vgsoftware.android.autosportrss.Log;
import com.vgsoftware.android.autosportrss.Utility;

public class FeedDroidDB
{
	private static SimpleDateFormat DateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private DatabaseHelper _databaseHelper;

	public FeedDroidDB()
	{
		if (Utility.getInstance().getContext() != null)
		{
			_databaseHelper = new DatabaseHelper(Utility.getInstance().getContext());
		}
	}

	public ArrayList<Feed> listFeeds()
	{
		Cursor cursor = null;
		SQLiteDatabase database = null;
		ArrayList<Feed> list = new ArrayList<Feed>();
		try
		{
			database = _databaseHelper.getReadableDatabase();
			cursor = database.rawQuery("SELECT Id, Name, Url, Active FROM Feed", null);
			while (cursor.moveToNext())
			{
				list.add(new Feed(cursor));
			}
			cursor.close();
		}
		catch (Exception ex)
		{
			Log.error("Unable to get feeds", ex);
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (database != null)
			{
				database.close();
			}
		}
		return list;
	}

	public ArrayList<Feed> listActiveFeeds()
	{
		Cursor cursor = null;
		SQLiteDatabase database = null;
		ArrayList<Feed> list = new ArrayList<Feed>();
		try
		{
			database = _databaseHelper.getReadableDatabase();
			cursor = database.rawQuery("SELECT Id, Name, Url, Active FROM Feed WHERE Active=1", null);

			while (cursor.moveToNext())
			{
				list.add(new Feed(cursor));
			}
			cursor.close();
		}
		catch (Exception ex)
		{
			Log.error("Unable to get active feeds", ex);
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (database != null)
			{
				database.close();
			}
		}
		return list;
	}

	public Feed loadFeed(int id)
	{
		Cursor cursor = null;
		SQLiteDatabase database = null;
		Feed feed = null;
		try
		{
			database = _databaseHelper.getReadableDatabase();
			cursor = database.rawQuery("SELECT Id, Name, Url, Active FROM Feed WHERE Id=?", new String[] { "" + id });

			if (cursor.moveToNext())
			{
				feed = new Feed(cursor);
			}
		}
		catch (Exception ex)
		{
			Log.error("Unabel to get feed with id: " + id, ex);
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (database != null)
			{
				database.close();
			}
		}
		return feed;
	}

	public void saveFeed(Feed feed)
	{
		SQLiteDatabase database = null;
		try
		{
			database = _databaseHelper.getWritableDatabase();
			if (feed.getId() > 0)
			{
				database.execSQL("UPDATE Feed SET Name=?, Url=?, Active=? WHERE Id=?;", new Object[] { feed.getName(), feed.getUrl(), feed.isActive() ? 1 : 0, feed.getId() });
			}
			else
			{
				database.execSQL("INSERT INTO Feed(Name, Url, Active) VALUES(?,?,?);", new Object[] { feed.getName(), feed.getUrl(), feed.isActive() ? 1 : 0 });
			}
		}
		catch (Exception ex)
		{
			Log.error("Unable to save station", ex);
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
		}
	}

	public void deleteFeed(Feed feed)
	{
		if (feed != null)
		{
			deleteFeed(feed.getId());
		}
	}

	public void deleteFeed(int feedId)
	{
		SQLiteDatabase database = null;
		try
		{
			database = _databaseHelper.getWritableDatabase();
			database.delete("FeedItem","FeedId=?",new String[]{feedId+""});
			database.delete("Feed", "Id=?", new String[] { feedId + "" });
		}
		catch (Exception ex)
		{
			Log.error("Unable to delete feed", ex);
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
		}
	}
	
	public void cleanupFeedItems(int type)
	{
		SQLiteDatabase database=null;
		try
		{
			database=_databaseHelper.getWritableDatabase();
			switch(type)
			{
			case 2:
				database.execSQL("UPDATE FeedItem SET Deleted=1 WHERE Added<DATETIME('now','-1 days')");
				break;
			case 3:
				database.execSQL("UPDATE FeedItem SET Deleted=1 WHERE Added<DATETIME('now','-7 days')");
				break;
			case 4:
				database.execSQL("UPDATE FeedItem SET Deleted=1 WHERE Added<DATETIME('now','-1 month')");
				break;
			}
		}
		catch(Exception ex)
		{
			Log.error("Unable to clean up feed items from type: '"+type+"'",ex);
		}
		finally
		{
			if(database!=null)
			{
				database.close();
			}
		}
	}

	// FeedItem Access

	public ArrayList<FeedItem> listFeedItems(Feed feed)
	{
		if (feed != null)
		{
			return listFeedItems(feed.getId());
		}
		return new ArrayList<FeedItem>();
	}

	public ArrayList<FeedItem> listFeedItems(int feedId)
	{
		Cursor cursor = null;
		SQLiteDatabase database = null;
		ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();
		try
		{
			database = _databaseHelper.getReadableDatabase();
			cursor = database.query("FeedItem", new String[] {}, "FeedId=? AND Deleted=0", new String[] { feedId + "" }, "", "", "date");
			while (cursor.moveToNext())
			{
				feedItems.add(new FeedItem(cursor));
			}
		}
		catch (Exception ex)
		{
			Log.error("Unable to list feed items", ex);
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (database != null)
			{
				database.close();
			}
		}
		Collections.sort(feedItems, FeedItem.DateSorter);
		return feedItems;
	}

	public FeedItem loadFeedItem(int feedItemId)
	{
		Cursor cursor = null;
		SQLiteDatabase database = null;
		try
		{
			database = _databaseHelper.getReadableDatabase();
			cursor = database.query("FeedItem", new String[] {}, "Id=?", new String[] { feedItemId + "" }, "", "", "");
			if (cursor.moveToNext())
			{
				return new FeedItem(cursor);
			}
		}
		catch (Exception ex)
		{
			Log.error("Unable to load feed item", ex);
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (database != null)
			{
				database.close();
			}
		}
		return null;
	}

	public FeedItem loadFeedItem(int feedId, String url)
	{
		if (url != null)
		{
			Cursor cursor = null;
			SQLiteDatabase database = null;
			try
			{
				database = _databaseHelper.getReadableDatabase();
				cursor = database.query("FeedItem", new String[] {}, "Deleted=0 AND FeedId=? AND Url=?", new String[] { feedId+"", url }, "", "", "");
				if (cursor.moveToNext())
				{
					return new FeedItem(cursor);
				}
			}
			catch (Exception ex)
			{
				Log.error("Unable to load feed item", ex);
			}
			finally
			{
				if (cursor != null)
				{
					cursor.close();
				}
				if (database != null)
				{
					database.close();
				}
			}
		}
		return null;
	}

	public void saveFeedItem(FeedItem feedItem)
	{
		if (feedItem != null)
		{
			SQLiteDatabase database = null;
			try
			{
				database = _databaseHelper.getWritableDatabase();
				if (feedItem.getId() > 0)
				{
					database.execSQL("UPDATE FeedItem SET FeedId=?, Title=?, Description=?, Url=?, CategoryId=?, Read=?, Date=?, Deleted=? WHERE Id=?", new String[] { feedItem.getFeedId() + "", feedItem.getTitle(), feedItem.getDescription(), feedItem.getUrl(), feedItem.getCategoryId() + "", (feedItem.isRead() ? 1 : 0) + "", DateFormat.format(feedItem.getDate()) + "",(feedItem.getDeleted()?1:0)+"", feedItem.getId() + "" });
				}
				else
				{
					database.execSQL("INSERT INTO FeedItem(FeedId, Title, Description, Url, CategoryId, Read, Date, Added, Deleted) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[] { feedItem.getFeedId() + "", feedItem.getTitle(), feedItem.getDescription(), feedItem.getUrl(), feedItem.getCategoryId() + "", (feedItem.isRead() ? 1 : 0) + "", DateFormat.format(feedItem.getDate()), DateFormat.format(feedItem.getAdded()), (feedItem.getDeleted()?1:0)+"" });
				}
			}
			catch (Exception ex)
			{
				Log.error("Unable to save feed item. Message: ", ex);
			}
			finally
			{
				if (database != null)
				{
					database.close();
				}
			}
		}
	}

	public void deleteFeedItem(FeedItem feedItem)
	{
		if (feedItem != null)
		{
			SQLiteDatabase database = null;
			try
			{
				database = _databaseHelper.getWritableDatabase();
				database.delete("FeedItem", "Id=?", new String[] { feedItem.getId() + "" });
			}
			catch (Exception ex)
			{
				Log.error("Unable to delete feed item", ex);
			}
			finally
			{
				if (database != null)
				{
					database.close();
				}
			}
		}
	}

	public int getUnreadFeedItemCount()
	{
		Cursor cursor = null;
		SQLiteDatabase database = null;
		try
		{
			database = _databaseHelper.getReadableDatabase();
			cursor = database.rawQuery("SELECT COUNT(FeedItem.Id) FROM FeedItem, Feed WHERE Deleted=0 AND Read=0 AND FeedItem.FeedId=Feed.Id AND Feed.Active=1", null);
			if (cursor.moveToNext())
			{
				return cursor.getInt(0);
			}
		}
		catch (Exception ex)
		{
			Log.error("Unable to count unread feed items", ex);
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (database != null)
			{
				database.close();
			}
		}
		return 0;
	}
}
