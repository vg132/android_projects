package com.vgsoftware.android.realtime.parse;

import android.content.Context;
import android.os.Handler;

import com.vgsoftware.android.realtime.ui.SLRealTime;

public class SiteSearchParser extends ParserBase
{
	private Handler _handler = null;
	private String _searchQuery = null;

	public SiteSearchParser(Context context, Handler handler, String searchQuery)
	{
		_handler = handler;
		_searchQuery = searchQuery;
	}

	public void run()
	{
		try
		{
			SiteParser parser = new SiteParser();
			_data = parser.siteSearch(_searchQuery);
		}
		catch (Exception ex)
		{
			_exception = ex;
		}
		_handler.sendEmptyMessage(SLRealTime.HANDLER_MESSAGE_SITE_SEARCH);
	}
}
