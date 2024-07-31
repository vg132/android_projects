package com.vgsoftware.android.realtime;

import android.util.LruCache;
import android.util.Pair;

public class CacheRepository
{
	private LruCache<String, Pair<Long, Object>> _cache = null;

	private static CacheRepository _instance = null;
	private final static long MaxAge = 86400;

	private CacheRepository()
	{
		_cache = new LruCache<String, Pair<Long, Object>>(4 * 1024 * 1024);
	}

	public synchronized static CacheRepository getInstance()
	{
		if (_instance == null)
		{
			_instance = new CacheRepository();
		}
		return _instance;
	}

	public synchronized void put(String key, Object item)
	{
		put(key, item, CacheRepository.MaxAge);
	}

	public synchronized void put(String key, Object item, long maxAgeSeconds)
	{
		_cache.remove(key);
		long maxAge = (maxAgeSeconds > CacheRepository.MaxAge ? CacheRepository.MaxAge : maxAgeSeconds) * 1000;
		_cache.put(key, new Pair<Long, Object>(System.currentTimeMillis() + maxAge, item));
		LogManager.info("Cache: Added key: '%s'", key);
	}

	public Object get(String key)
	{
		Pair<Long, Object> item = _cache.get(key);
		if (item != null)
		{
			if (item.first > System.currentTimeMillis())
			{
				LogManager.info("Cache: Found key: '%s'", key);
				return item.second;
			}
			else
			{
				_cache.remove(key);
				LogManager.info("Cache: Removed key: '%s'", key);
			}
		}
		else
		{
			LogManager.info("Cache: Not found key: '%s'", key);
		}
		return null;
	}
}
