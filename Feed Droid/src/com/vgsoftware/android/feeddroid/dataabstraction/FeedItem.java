package com.vgsoftware.android.feeddroid.dataabstraction;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.vgsoftware.android.feeddroid.LogManager;

import android.database.Cursor;

public class FeedItem extends FeedDroidDB
{
	private static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private int _id;
	private int _feedId;
	private String _title;
	private String _description;
	private String _url;
	private boolean _read;
	private Date _date;
	private Date _added;
	private boolean _deleted;

	public static final Comparator<FeedItem> DateSorter = new Comparator<FeedItem>()
	{
		@Override
		public int compare(FeedItem item1, FeedItem item2)
		{
			return item2.getDate().compareTo(item1.getDate());
		}
	};

	public FeedItem()
	{
	}

	protected FeedItem(Cursor cursor)
	{
		if (cursor != null)
		{
			_id = cursor.getInt(0);
			_feedId = cursor.getInt(1);
			_title = cursor.getString(2);
			_description = cursor.getString(3);
			_url = cursor.getString(4);
			_read = cursor.getInt(5) == 0 ? false : true;
			_deleted = cursor.getInt(8) == 0 ? false : true;
			try
			{
				_date = DateFormat.parse(cursor.getString(6));
				_added = DateFormat.parse(cursor.getString(7));
			}
			catch (Exception ex)
			{
				_date = new Date();
				_added = new Date();
				LogManager.error("Unable to parse feed item dates.", ex);
			}
		}
	}

	public int getId()
	{
		return _id;
	}

	public int getFeedId()
	{
		return _feedId;
	}

	public void setFeedId(int feedId)
	{
		_feedId = feedId;
	}

	public String getTitle()
	{
		return _title;
	}

	public void setTitle(String title)
	{
		_title = title;
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(String description)
	{
		_description = description;
	}

	public String getUrl()
	{
		return _url;
	}

	public void setUrl(String url)
	{
		_url = url;
	}

	public Date getDate()
	{
		return _date;
	}

	public void setDate(Date date)
	{
		_date = date;
	}

	public Date getAdded()
	{
		return _added;
	}

	public void setAdded(Date added)
	{
		_added = added;
	}

	public void setRead(boolean read)
	{
		_read = read;
	}

	public boolean isRead()
	{
		return _read;
	}

	public boolean getDeleted()
	{
		return _deleted;
	}

	public void setDeleted(boolean deleted)
	{
		_deleted = deleted;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof FeedItem && getUrl() != null)
		{
			return getUrl().equals(((FeedItem) o).getUrl());
		}
		return false;
	}
}
