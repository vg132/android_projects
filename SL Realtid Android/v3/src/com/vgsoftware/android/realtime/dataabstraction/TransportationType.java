package com.vgsoftware.android.realtime.dataabstraction;

import android.database.Cursor;

public class TransportationType
{
	private int _id;
	private String _name;

	public TransportationType()
	{
	}

	public TransportationType(Cursor cursor)
	{
		if (cursor != null)
		{
			_id = cursor.getInt(0);
			_name = cursor.getString(1);
		}
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		_id = id;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}
}
