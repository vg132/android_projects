package com.vgsoftware.android.realtime.parse;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vgsoftware.android.vglib.HttpUtility;
import com.vgsoftware.android.vglib.StringUtility;
import com.vgsoftware.android.vglib.XMLUtility;

import android.util.Xml;

public class TrafficStatusParser
{
	private static final String TrafficServiceUrl = "http://api.sl.se/api2/trafficsituation.xml?key=";

	public synchronized List<TrafficSituation> parseTrafficStatus()
	{
		List<TrafficSituation> trafficStatus = new ArrayList<TrafficSituation>();
		try
		{
			String content = HttpUtility.getData(new URL(TrafficStatusParser.TrafficServiceUrl));
			if (!StringUtility.isNullOrEmpty(content))
			{
				return parse(content);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return trafficStatus;
	}

	private List<TrafficSituation> parse(String content)
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(new StringReader(content));			
			parser.nextTag();
			return parseTrafficStauts(parser);
		}
		catch (Exception e)
		{
		}
		return new ArrayList<TrafficSituation>();
	}

	private List<TrafficSituation> parseTrafficStauts(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		List<TrafficSituation> trafficStatus = new ArrayList<TrafficSituation>();
		TrafficSituation status = null;
		String tagName = "";
		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			tagName = parser.getName();
			switch (parser.getEventType())
			{
				case XmlPullParser.START_TAG:
				{
					if (tagName.equals("TrafficEvent"))
					{
						TrafficEvent event = readTrafficEvent(parser);
						if (event != null)
						{
							status.getTrafficEvents().add(event);
						}
					}
					else if (tagName.equals("TrafficType"))
					{
						status = new TrafficSituation();
						status.setName(parser.getAttributeValue(null, "Name"));
						trafficStatus.add(status);
					}
					break;
				}
			}
		}
		return trafficStatus;
	}

	private TrafficEvent readTrafficEvent(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, null, "TrafficEvent");
		TrafficEvent trafficEvent = new TrafficEvent();
		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			String tagName = parser.getName();
			switch (parser.getEventType())
			{
				case XmlPullParser.START_TAG:
				{
					if (tagName.equals("Message"))
					{
						trafficEvent.setMessage(XMLUtility.readText(parser, tagName));
					}
					else if (tagName.equals("TrafficLine"))
					{
						trafficEvent.setLine(XMLUtility.readText(parser, tagName));
					}
				}
				case XmlPullParser.END_TAG:
				{
					if (tagName.equals("TrafficEvent"))
					{
						return trafficEvent;
					}
				}
			}
		}
		return null;
	}
}
