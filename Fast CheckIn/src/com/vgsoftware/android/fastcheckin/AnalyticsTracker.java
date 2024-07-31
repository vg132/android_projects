package com.vgsoftware.android.fastcheckin;

import android.content.Context;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.vgsoftware.android.fastcheckin.R;

public class AnalyticsTracker
{	
	private GoogleAnalyticsTracker _tracker=null;
	private Context _context=null;

	public AnalyticsTracker(Context context)
	{
		LogManager.info("AnalyticsTracker.AnalyticsTracker");

		_context=context;
		_tracker = GoogleAnalyticsTracker.getInstance();
		if(!Utilities.isDebug())
		{
			_tracker.start(context.getString(R.string.google_analytics_id), context);
			_tracker.dispatch();
		}
	}
	
	public void trackPageView(String message)
	{
		LogManager.info("AnalyticsTracker.trackPageView - '" + message + "'");
		if(!Utilities.isDebug())
		{
			_tracker.setCustomVar(1,"Version",Utilities.getVersion(_context));
			_tracker.trackPageView(message);
			_tracker.dispatch();
		}
	}
	
	public void dispatch()
	{
		LogManager.info("AnalyticsTracker.dispatch");
		_tracker.dispatch();
	}
}
