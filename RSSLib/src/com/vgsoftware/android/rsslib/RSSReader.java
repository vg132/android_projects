package com.vgsoftware.android.rsslib;

import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

public class RSSReader extends Thread
{
	private Handler _handler;
	private RSSFeed _feed;
	private String _feedUrl;
	private String _content;
	
	public RSSReader(Handler handler, String feedUrl)
	{
		_handler=handler;
		_feedUrl=feedUrl;
	}
	
	public RSSReader(Handler handler, String content, String feedName)
	{
		_handler=handler;
		_content=content;
	}

	public void run()
	{
		RSSReader.readFeed(_feedUrl, _content);
		_handler.sendEmptyMessage(0);
	}

	public static RSSFeed readFeed(String feedUrl)
	{
		return RSSReader.readFeed(feedUrl, null);
	}
	
	public static RSSFeed readFeed(String feedUrl, String content)
	{
		try
		{
			URL url = null;
			if(!TextUtils.isEmpty(feedUrl))
			{
				url=new URL(feedUrl);
			}
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();

			XMLReader xmlReader = parser.getXMLReader();
			RSSHandler rssHandler = new RSSHandler();
			xmlReader.setContentHandler(rssHandler);
			InputSource is = url==null?new InputSource(new StringReader(content)): new InputSource(url.openStream());
			xmlReader.parse(is);
			return rssHandler.getFeed();
		}
		catch (Exception ex)
		{
			Log.d("RSSLib","Unable to read feed: '" + feedUrl + "'.");
			ex.printStackTrace();
		}
		return null;
	}
	
	public RSSFeed getFeed()
	{
		return _feed;
	}
	
	public String getFeedUrl()
	{
		return _feedUrl;
	}
}
