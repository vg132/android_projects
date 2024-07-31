package com.vgsoftware.android.rsslib;

import java.util.List;
import java.util.Vector;

public class RSSFeed 
{
	private String _title = null;
	private String _pubdate = null;
	private int _itemcount = 0;
	private List<RSSItem> _itemlist;
	private long _lastUpdated;
		
	public RSSFeed()
	{
		_itemlist = new Vector<RSSItem>(0);
		_lastUpdated=System.currentTimeMillis();
	}

	public int addItem(RSSItem item)
	{
		_itemlist.add(item);
		_itemcount++;
		return _itemcount;
	}

	public RSSItem getItem(int location)
	{
		return _itemlist.get(location);
	}

	public List<RSSItem> getAllItems()
	{
		return _itemlist;
	}

	public int getItemCount()
	{
		return _itemcount;
	}

	public void setTitle(String title)
	{
		_title = title;
	}

	public void setPubDate(String pubdate)
	{
		_pubdate = pubdate;
	}

	public String getTitle()
	{
		return _title;
	}

	public String getPubDate()
	{
		return _pubdate;
	}
	
	public long getLastUpdated()
	{
		return _lastUpdated;
	}
}