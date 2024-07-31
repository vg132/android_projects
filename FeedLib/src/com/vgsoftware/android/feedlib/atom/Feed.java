package com.vgsoftware.android.feedlib.atom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vgsoftware.android.feedlib.IFeed;
import com.vgsoftware.android.feedlib.IFeedItem;

public class Feed implements IFeed
{
	private String _title;
	private String _description;
	private Date _date;
	private List<IFeedItem> _items;

	public Feed()
	{
		_items=new ArrayList<IFeedItem>();
	}
	
	@Override
	public String getTitle()
	{
		return _title;
	}
	
	public void setTitle(String title)
	{
		_title=title;
	}

	@Override
	public String getDescription()
	{
		return _description;
	}
	
	public void setDescription(String description)
	{
		_description=description;
	}

	@Override
	public Date getDate()
	{
		return _date;
	}
	
	public void setDate(Date date)
	{
		_date=date;
	}

	@Override
	public IFeedItem getFeedItem(int pos)
	{
		if(pos>=0 && pos<_items.size())
		{
			return _items.get(pos);
		}
		return null;
	}

	@Override
	public List<IFeedItem> getFeedItems()
	{
		return _items;
	}
	
	public void addItem(FeedItem item)
	{
		_items.add(item);
	}
}
