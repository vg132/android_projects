package com.vgsoftware.android.realtime;

public class RealTimeException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6386556320000000000L;

	private int _resourceId;
	
	public RealTimeException(String message)
	{
		super(message);
	}
	
	public RealTimeException(int resourceId)
	{
		_resourceId=resourceId;
	}
	
	public int getResourceId()
	{
		return _resourceId;
	}
}
