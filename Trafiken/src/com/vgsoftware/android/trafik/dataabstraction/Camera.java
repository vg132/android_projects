package com.vgsoftware.android.trafik.dataabstraction;

import com.j256.ormlite.field.DatabaseField;

public class Camera
{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int feedId;
	@DatabaseField
	private String area = null;
	@DatabaseField
	private String location = null;
	@DatabaseField
	private String name = null;
	@DatabaseField
	private String url = null;
	@DatabaseField
	private boolean active;

	public Camera()
	{
	}

	public Camera(int feedId, String area, String location, String name, String url)
	{
		this.feedId = feedId;
		this.area = area;
		this.location = location;
		this.name = name;
		this.url = url;
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

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public boolean getActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}
}
