package com.vgsoftware.android.polisen.dataabstraction;

import java.sql.SQLException;
import java.util.Date;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.DatabaseTable;
import com.vgsoftware.android.polisen.Log;
import com.vgsoftware.android.vglib.DateUtility;

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
	@DatabaseField
	private int regionId;

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
			if (DateUtility.parse(datePart) != null)
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
	
	public int getRegionId()
	{
		return regionId;
	}
	
	public void setRegionId(int regionId)
	{
		this.regionId = regionId;
	}

	public String getRegionName(Context context)
	{
		try
		{
			PreparedQuery<Region> query = DatabaseHelper.getHelper(context).getRegionDao().queryBuilder().where().eq("id", getRegionId()).prepare();
			Region region = DatabaseHelper.getHelper(context).getRegionDao().queryForFirst(query);
			if (region != null)
			{
				return region.getName();
			}
		}
		catch (SQLException ex)
		{
			Log.error("Unable to load region name from feed item", ex);
		}
		return "";
	}
}