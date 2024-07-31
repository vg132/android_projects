package com.vgsoftware.android.feedlib.rss;

import java.util.Date;

import com.vgsoftware.android.feedlib.IFeedItem;

public class FeedItem implements IFeedItem
{
	private String _title = null;
	private String _description = null;
	private String _link = null;
	private String _category = null;
	private Date _date = null;

	public void setTitle(String title)
	{
		_title = title;
	}

	public void setDescription(String description)
	{
		_description = description;
	}

	public void setLink(String link)
	{
		_link = link;
	}

	public void setCategory(String category)
	{
		_category = category;
	}

	public void setDate(Date date)
	{
		_date = date;
	}

	public String getTitle()
	{
		return _title;
	}

	public String getDescription()
	{
		return _description;
	}

	public String getLink()
	{
		return _link;
	}

	public String getCategory()
	{
		return _category;
	}

	public Date getDate()
	{
		return _date;
	}
}
