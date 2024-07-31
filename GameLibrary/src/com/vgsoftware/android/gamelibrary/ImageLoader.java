package com.vgsoftware.android.gamelibrary;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageLoader
{
	private final int _maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	private final int _cacheSize = _maxMemory / 8;
	private LruCache<String, Bitmap> _memoryCache = null;
	private static ImageLoader _instance = null;

	private ImageLoader()
	{
		_memoryCache = new LruCache<String, Bitmap>(_cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap bitmap)
			{
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	public synchronized static ImageLoader getInstance()
	{
		if (_instance == null)
		{
			_instance = new ImageLoader();
		}
		return _instance;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap)
	{
		if (getBitmapFromMemCache(key) == null)
		{
			_memoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key)
	{
		return _memoryCache.get(key);
	}
}
