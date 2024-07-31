package com.vgsoftware.android.realtime.parse;

public class Site
{
	private int _siteId;
	private String _name = null;
	private String _areaName = null;

	public int getSiteId()
	{
		return _siteId;
	}

	public void setSiteId(int siteId)
	{
		_siteId = siteId;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public String getAreaName()
	{
		return _areaName;
	}

	public void setAreaName(String areaName)
	{
		_areaName = areaName;
	}
}