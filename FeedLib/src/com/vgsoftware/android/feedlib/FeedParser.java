package com.vgsoftware.android.feedlib;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Element;

import android.os.Handler;

import com.vgsoftware.android.feedlib.atom.AtomParser;
import com.vgsoftware.android.feedlib.rss.RssParser;
import com.vgsoftware.android.vglib.XMLUtility;

public class FeedParser extends Thread
{
	private Handler _handler;
	private IFeed _feed;
	private String _feedUrl;

	public FeedParser(Handler handler, String feedUrl)
	{
		_handler = handler;
		_feedUrl = feedUrl;
	}

	public void run()
	{
		_feed = FeedParser.parse(_feedUrl);
		_handler.sendEmptyMessage(0);
	}

	public static IFeed parse(String feedUrl)
	{
		try
		{
			Element rootElement = XMLUtility.getRootElement(new URL(feedUrl));
			if (rootElement != null)
			{
				if (RssParser.canHandle(rootElement))
				{
					return RssParser.parse(rootElement);
				}
				else if (AtomParser.canHandle(rootElement))
				{
					return AtomParser.parse(rootElement);
				}
			}
		}
		catch (MalformedURLException ex)
		{
		}
		return null;
	}

	public IFeed getFeed()
	{
		return _feed;
	}

	public String getFeedUrl()
	{
		return _feedUrl;
	}
}