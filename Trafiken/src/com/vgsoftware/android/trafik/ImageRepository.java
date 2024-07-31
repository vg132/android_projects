package com.vgsoftware.android.trafik;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Pair;

public class ImageRepository
{
	private LruCache<String, Pair<Long, Bitmap>> _memoryCache = null;
	private Map<String, Long> _notFound = null;
	private static ImageRepository _instance = null;
	private final static long MaxAge = 480000;

	private ImageRepository(Context context)
	{
		final int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		final int cacheSize = 1024 * 1024 * memClass / 8;

		_memoryCache = new LruCache<String, Pair<Long, Bitmap>>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Pair<Long, Bitmap> pair)
			{
				return pair.second.getRowBytes() * pair.second.getHeight();
			}
		};

		_notFound = new HashMap<String, Long>();
	}

	public synchronized static ImageRepository getInstance(Context context)
	{
		if (_instance == null)
		{
			_instance = new ImageRepository(context);
		}
		return _instance;
	}

	public synchronized void addImage(URL url, Bitmap bitmap)
	{
		_memoryCache.remove(url.toString());
		_memoryCache.put(url.toString(), new Pair<Long, Bitmap>(System.currentTimeMillis() + ImageRepository.MaxAge, bitmap));
	}

	public synchronized Bitmap getImage(URL url)
	{
		Pair<Long, Bitmap> pair = _memoryCache.get(url.toString());
		if (pair != null && System.currentTimeMillis() < pair.first)
		{
			return pair.second;
		}
		else if (pair != null)
		{
			_memoryCache.remove(url.toString());
		}
		return null;
	}

	public synchronized void addNotFound(URL url)
	{
		_notFound.remove(url.toString());
		_notFound.put(url.toString(), System.currentTimeMillis() + ImageRepository.MaxAge);
	}

	public synchronized boolean isNotFound(URL url)
	{
		if (_notFound.containsKey(url.toString()))
		{
			if (System.currentTimeMillis() < _notFound.get(url.toString()))
			{
				return true;
			}
			else
			{
				_notFound.remove(url.toString());
			}
		}
		return false;
	}
}
