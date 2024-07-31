package com.vgsoftware.android.realtime;

import android.app.Activity;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class GoogleAnalytics
{
	private static Activity _activity;
	private static GoogleAnalytics _instance;
	private GoogleAnalyticsTracker _tracker;

	private static boolean _initialized;

	private GoogleAnalytics()
	{
		if(_initialized)
		{
			_tracker=GoogleAnalyticsTracker.getInstance();
			_tracker.start(_activity.getResources().getString(R.string.GoogleAnalyticsTrackerId),_activity);
		}
	}

	public static synchronized void initialize(Activity activity)
	{
		_activity=activity;
		_initialized=true;
	}
	
	public static synchronized GoogleAnalytics getInstance()
	{
		if(_instance==null)
		{
			_instance=new GoogleAnalytics();
		}
		return _instance;
	}
	
	public void trackEvent(String category, String action, String label, int value)
	{
		if(_tracker!=null)
		{
			_tracker.trackEvent(category,action,label,value);
		}
	}
	
	public void trackPageView(String url)
	{
		if(_tracker!=null)
		{
			_tracker.trackPageView(url);
		}
	}
	
	public void stop()
	{
		if(_tracker!=null)
		{
			_tracker.stop();
		}
	}
}
