package com.vgsoftware.android.trafik.dataabstraction;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.vgsoftware.android.vglib.DateParser;

@DatabaseTable
public class FeedItem
{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int feedId;
	@DatabaseField
	private String title;
	@DatabaseField
	private String description;
	@DatabaseField
	private String url;
	@DatabaseField
	private boolean read;
	@DatabaseField
	private Date date;
	@DatabaseField
	private Date added;

	public FeedItem()
	{
	}

	public int getId()
	{
		return id;
	}

	public int getFeedId()
	{
		return feedId;
	}

	public void setFeedId(int feedId)
	{
		this.feedId = feedId;
	}

	public String getTitle()
	{
		if (title != null && title.indexOf(',') != -1 && title.indexOf(',') < title.length() + 2)
		{
			String datePart = title.substring(0, title.indexOf(',')).replace(",", "").trim();
			if (DateParser.parse(datePart) != null)
			{
				return title.substring(title.indexOf(',') + 1).trim();
			}
		}
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title != null ? title : "";
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description != null ? description : "";
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url != null ? url : "";
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public Date getAdded()
	{
		return added;
	}

	public void setAdded(Date added)
	{
		this.added = added;
	}

	public void setRead(boolean read)
	{
		this.read = read;
	}

	public boolean isRead()
	{
		return read;
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