package com.vgsoftware.android.feedlib.atom;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.text.TextUtils;

import com.vgsoftware.android.feedlib.rss.Feed;
import com.vgsoftware.android.feedlib.rss.FeedItem;
import com.vgsoftware.android.vglib.DateUtility;
import com.vgsoftware.android.vglib.XMLUtility;

public class AtomParser
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
		catch (MalformedURLException ex)
		{
		}
		return null;
	}

	public static Feed parse(Element root)
	{
		Feed feed = new Feed();
		feed.setTitle(XMLUtility.getValue(root.getChildNodes(), "title"));
		feed.setDescription(XMLUtility.getValue(root.getChildNodes(), "subtitle"));
		feed.setDate(DateUtility.parse(XMLUtility.getValue(root.getChildNodes(), "updated")));
		NodeList nodeList = root.getElementsByTagName("entry");
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
		if (root.getNodeName() != null && root.getNodeName().equals("feed") && root.hasAttribute("xmlns") && root.getAttribute("xmlns").equals("http://www.w3.org/2005/Atom"))
		{
			return true;
		}
		return false;
	}

	private static FeedItem parseItemElement(Element element)
	{
		FeedItem item = new FeedItem();

		item.setTitle(XMLUtility.getValue(element, "title"));

		String description = XMLUtility.getValue(element, "summary");
		if (description == null || description.trim().equals(""))
		{
			description = XMLUtility.getValue(element, "content");
		}
		item.setDescription(description);

		item.setLink(getLink(element));
		item.setDate(DateUtility.parse(XMLUtility.getValue(element, "updated")));

		return item;
	}

	private static String getLink(Element element)
	{
		String fallbackLink = null;
		NodeList nodes = element.getElementsByTagName("link");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			if (nodes.item(i) instanceof Element)
			{
				Element link = (Element) nodes.item(i);
				if (link.hasAttribute("rel") && link.getAttribute("rel").equals("alternate") && link.hasAttribute("href"))
				{
					return link.getAttribute("href");
				}
				else if (!link.hasAttribute("rel") && link.hasAttribute("href"))
				{
					fallbackLink = link.getAttribute("href");
				}
			}
		}
		return fallbackLink;
	}
}
