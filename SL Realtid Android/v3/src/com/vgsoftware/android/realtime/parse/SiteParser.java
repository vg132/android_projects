package com.vgsoftware.android.realtime.parse;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.vgsoftware.android.vglib.HttpUtility;
import com.vgsoftware.android.vglib.StringUtility;
import com.vgsoftware.android.vglib.XMLUtility;

public class SiteParser
{
	private static final String SiteServiceUrl = "http://api.sl.se/api2/typeahead.xml?key=&maxresults=50&searchstring=";

	public synchronized List<Site> siteSearch(final String query)
	{
		final String searchQuery = query.toLowerCase(Locale.getDefault());
		try
		{
			String content = HttpUtility.getData(new URL(SiteParser.SiteServiceUrl + URLEncoder.encode(searchQuery, "UTF-8")));
			if (!StringUtility.isNullOrEmpty(content))
			{
				return parse(content);
			}
		}
		catch (IOException e)
		{
		}
		return new ArrayList<Site>(0);
	}

	private List<Site> parse(String content)
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(new StringReader(content));
			parser.nextTag();
			return parseSites(parser);
		}
		catch (Exception e)
		{
		}
		return new ArrayList<Site>();
	}

	private List<Site> parseSites(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		List<Site> sites = new ArrayList<Site>();
		String tagName = "";
		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			tagName = parser.getName();
			switch (parser.getEventType())
			{
				case XmlPullParser.START_TAG:
					if (tagName.equals("Site"))
					{
						Site site = readSite(parser);
						if (site != null)
						{
							sites.add(site);
						}
					}
					break;
			}
		}
		return sites;
	}

	private Site readSite(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, null, "Site");
		Site site = new Site();
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG)
			{
				continue;
			}
			String tagName = parser.getName();
			if (tagName.equals("SiteId"))
			{
				site.setSiteId(XMLUtility.readInteger(parser, tagName));
			}
			else if (tagName.equals("Name"))
			{
				String area = "";
				String name = XMLUtility.readText(parser, tagName);
				if (name.length() <= 4 && name.toUpperCase(Locale.getDefault()).equals(name))
				{
					return null;
				}
				int startIndex = name.lastIndexOf('(');
				if (startIndex >= 0)
				{
					area = name.substring(startIndex).trim().replace("(", "").replace(")", "");
					name = name.substring(0, startIndex).trim();
				}
				site.setName(name);
				site.setAreaName(area);
			}
			else
			{
				XMLUtility.skip(parser);
			}
		}
		return site;
	}
}
