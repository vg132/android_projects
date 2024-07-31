package com.vgsoftware.android.rsslib;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSHandler extends DefaultHandler
{
	private RSSFeed _feed;
	private RSSItem _item;
	private final int RSS_TITLE = 1;
	private final int RSS_LINK = 2;
	private final int RSS_DESCRIPTION = 3;
	private final int RSS_CATEGORY = 4;
	private final int RSS_PUBDATE = 5;

	int depth = 0;
	int currentState = 0;

	public RSSHandler()
	{
	}

	public RSSFeed getFeed()
	{
		return _feed;
	}

	public void startDocument()
	throws SAXException
	{
		// initialize our RSSFeed object - this will hold our parsed contents
		_feed = new RSSFeed();
		// initialize the RSSItem object - we will use this as a crutch to grab the
		// info from the channel
		// because the channel and items have very similar entries..
		_item = new RSSItem();
	}

	public void endDocument()
	throws SAXException
	{
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
	throws SAXException
	{
		depth++;
		if (localName.equals("channel"))
		{
			currentState = 0;
			return;
		}
		if (localName.equals("image"))
		{
			// record our feed data - we temporarily stored it in the item :)
			_feed.setTitle(_item.getTitle());
			_feed.setPubDate(_item.getPubDate());
		}
		if (localName.equals("item"))
		{
			// create a new item
			_item = new RSSItem();
			return;
		}
		if (localName.equals("title"))
		{
			currentState = RSS_TITLE;
			return;
		}
		if (localName.equals("description"))
		{
			currentState = RSS_DESCRIPTION;
			return;
		}
		if (localName.equals("link"))
		{
			currentState = RSS_LINK;
			return;
		}
		if (localName.equals("category"))
		{
			currentState = RSS_CATEGORY;
			return;
		}
		if (localName.equals("pubDate"))
		{
			currentState = RSS_PUBDATE;
			return;
		}
		currentState = 0;
	}

	public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException
	{
		depth--;
		if (localName.equals("item"))
		{
			// add our item to the list!
			_feed.addItem(_item);
		}
	}

	String sss;
	
	public void characters(char ch[], int start, int length)
	{
		String content = new String(ch, start, length);
		
		switch (currentState)
		{
		case RSS_TITLE:
			_item.setTitle(content);
			currentState = 0;
			break;
		case RSS_LINK:
			_item.setLink(content);
			currentState = 0;
			break;
		case RSS_DESCRIPTION:
			_item.setDescription(content);
			currentState = 0;
			break;
		case RSS_CATEGORY:
			_item.setCategory(content);
			currentState = 0;
			break;
		case RSS_PUBDATE:
			_item.setPubDate(content);
			currentState = 0;
			break;
		}
	}
}
