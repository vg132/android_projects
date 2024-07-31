package com.vgsoftware.android.realtime;

import java.util.List;

public class CachedResult
{
	private long _cacheTimeoutTime = 0;
	private List<?> _data = null;

	public CachedResult(List<?> data)
	{
		_data = data;
		_cacheTimeoutTime = System.currentTimeMillis() + (1000 * 20);
	}

	public long getCacheTimeoutTime()
	{
		return _cacheTimeoutTime;
	}

	public List<?> getData()
	{
		return _data;
	}
}