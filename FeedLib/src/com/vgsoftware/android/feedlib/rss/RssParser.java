package com.vgsoftware.android.feedlib.rss;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.text.TextUtils;

import com.vgsoftware.android.feedlib.Log;
import com.vgsoftware.android.vglib.DateUtility;
import com.vgsoftware.android.vglib.XMLUtility;

public class RssParser
{
	public static Feed parse(String feedUrl)
	{

		try
		{
			Element element = XMLUtility.getRootElement(new URL(feedUrl));
			if (element != null)
			{
				return parse(element);
			}
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Feed parse(Element root)
	{
		Log.verbose("Start parse of RSS feed");
		Feed feed = new Feed();
		feed.setTitle(XMLUtility.getValue(root.getChildNodes(), "title"));
		Log.verbose("Title: " + feed.getTitle());
		feed.setDescription(XMLUtility.getValue(root.getChildNodes(), "description"));
		Log.verbose("Description: " + feed.getDescription());
		feed.setDate(DateUtility.parse(XMLUtility.getValue(root.getChildNodes(), "pubDate")));
		Log.verbose("Publish date: " + feed.getDate());
		NodeList nodeList = root.getElementsByTagName("item");
		Log.verbose("Found '" + nodeList.getLength() + "' items.");
		if (nodeList.getLength() > 0)
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				FeedItem feedItem = parseItemElement((Element) nodeList.item(i));
				if (feedItem != null && feedItem.getLink() != null && !TextUtils.isEmpty(feedItem.getLink()))
				{
					feed.addItem(feedItem);
				}
			}
		}
		return feed;
	}

	public static boolean canHandle(Element root)
	{
		if (root.getNodeName() != null)
		{
			String nodeName = root.getNodeName();
			if (nodeName.equals("rss") || nodeName.equals("rdf:RDF"))
			{
				return true;
			}
			Log.verbose("Document of type: '" + nodeName + "' not handled by rss parser");
		}
		return false;
	}

	private static FeedItem parseItemElement(Element itemElement)
	{
		Log.verbose("Parse feed item");
		FeedItem item = new FeedItem();
		item.setTitle(XMLUtility.getValue(itemElement, "title"));
		Log.verbose("Title: " + item.getTitle());
		item.setDescription(XMLUtility.getValue(itemElement, "description"));
		Log.verbose("Description: " + item.getDescription());
		item.setLink(XMLUtility.getValue(itemElement, "link"));
		Log.verbose("Link: " + item.getLink());
		item.setDate(DateUtility.parse(XMLUtility.getValue(itemElement, "pubDate")));
		Log.verbose("Publish date: " + item.getDate());

		return item;
	}
}
