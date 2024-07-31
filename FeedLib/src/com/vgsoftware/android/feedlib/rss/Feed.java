package com.vgsoftware.android.feedlib.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vgsoftware.android.feedlib.IFeed;
import com.vgsoftware.android.feedlib.IFeedItem;

public class Feed implements IFeed
{
	private String _title = null;
	private Date _date = null;
	private String _description = null;
	private List<IFeedItem> _feedItems;

	public Feed()
	{
		_feedItems = new ArrayList<IFeedItem>();
	}

	public void addItem(FeedItem item)
	{
		_feedItems.add(item);
	}

	public IFeedItem getFeedItem(int pos)
	{
		if (pos >= 0 && _feedItems.size() > pos)
		{
			return _feedItems.get(pos);
		}
		return null;
	}

	public List<IFeedItem> getFeedItems()
	{
		return _feedItems;
	}

	public void setTitle(String title)
	{
		_title = title;
	}

	public void setDate(Date date)
	{
		_date = date;
	}

	public String getTitle()
	{
		return _title;
	}

	public Date getDate()
	{
		return _date;
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(String description)
	{
		_description = description;
	}
}