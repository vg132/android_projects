package com.vgsoftware.android.realtime.dataabstraction;

import android.database.Cursor;

public class Donation
{
	public int _id;
	public String _productId = null;
	public String _status = null;

	public Donation(String productId)
	{
		_productId = productId;
	}

	public Donation(Cursor cursor)
	{
		if (cursor != null)
		{
			_id = cursor.getInt(0);
			_productId = cursor.getString(1);
			_status = cursor.getString(2);
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

	public String getProductId()
	{
		return _productId;
	}

	public void setProductId(String productId)
	{
		_productId = productId;
	}

	public String getStatus()
	{
		return _status;
	}

	public void setStatus(String status)
	{
		_status = status;
	}
}
