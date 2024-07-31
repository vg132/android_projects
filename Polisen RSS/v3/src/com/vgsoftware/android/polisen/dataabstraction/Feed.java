package com.vgsoftware.android.polisen.dataabstraction;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Feed
{
	@DatabaseField(id = true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String url;
	@DatabaseField
	private boolean active;
	@DatabaseField
	private String code;

	public Feed()
	{
	}

	@Deprecated
	public Feed(int id, String name, String url, boolean active)
	{
		this.id = id;
		this.name = name;
		this.url = url;
		this.active = active;
	}
	
	public Feed(int id, String name, String url, boolean active, String code)
	{
		this.id = id;
		this.name = name;
		this.url = url;
		this.active = active;
		this.code = code;
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

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public int getId()
	{
		return id;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Feed)
		{
			return ((Feed) o).getId() == getId();
		}
		return false;
	}
}
