package com.vgsoftware.android.polisen.dataabstraction;

import android.database.Cursor;

public class Category
{
	private int _id;
	private String _name;
	private boolean _active;

	public Category(String name)
	{
		_name = name;
	}

	public Category(Cursor cursor)
	{
		if (cursor != null)
		{
			_id = cursor.getInt(0);
			_name = cursor.getString(1);
			_active = cursor.getInt(2) == 0 ? false : true;
		}
	}

	public int getId()
	{
		return _id;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public boolean getActive()
	{
		return _active;
	}

	public void setActive(boolean active)
	{
		_active = active;
	}
}