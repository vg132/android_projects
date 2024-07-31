package com.vgsoftware.android.feedlib.atom;

import java.util.Date;

import com.vgsoftware.android.feedlib.IFeedItem;

public class FeedItem implements IFeedItem
{
	private Date _date;
	private String _title;
	private String _description;
	private String _link;

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
	public String getLink()
	{
		return _link;
	}

	public void setLink(String link)
	{
		_link=link;
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
}
