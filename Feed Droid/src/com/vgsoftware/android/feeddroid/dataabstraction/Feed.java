package com.vgsoftware.android.feeddroid.dataabstraction;

import android.database.Cursor;

public class Feed
{
	private int _id;
	private String _name;
	private String _url;
	private boolean _active;
	private int _categoryId;

	public Feed()
	{
		_active = true;
	}

	public Feed(String name, String url)
	{
		_name = name;
		_url = url;
		_active = true;
	}

	protected Feed(Cursor cursor)
	{
		if (cursor != null)
		{
			_id = cursor.getInt(0);
			_name = cursor.getString(1);
			_url = cursor.getString(2);
			_active = cursor.getInt(3) == 0 ? false : true;
			_categoryId = cursor.getInt(4);
		}
	}


	public String getName()
	{
		return _name;
	}

	public void setName(String _name)
	{
		this._name = _name;
	}

	public String getUrl()
	{
		return _url;
	}

	public void setUrl(String _url)
	{
		this._url = _url;
	}

	public boolean isActive()
	{
		return _active;
	}

	public void setActive(boolean _active)
	{
		this._active = _active;
	}

	public int getId()
	{
		return _id;
	}
	
	public void setCategoryId(int categoryId)
	{
		_categoryId = categoryId;
	}
	
	public int getCategoryId()
	{
		return _categoryId;
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
