package com.vgsoftware.android.realtime.parse;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.vgsoftware.android.vglib.DateUtility;
import com.vgsoftware.android.vglib.XMLUtility;

public class DpsParser
{
	public static List<Departure> parse(String content, int transportationType)
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(new StringReader(content));
			parser.nextTag();
			return parseDepartures(parser, transportationType);
		}
		catch (Exception e)
		{
		}
		return new ArrayList<Departure>();
	}

	private static List<Departure> parseDepartures(XmlPullParser parser, int transportationType) throws XmlPullParserException, IOException
	{
		List<Departure> departures = new ArrayList<Departure>();
		String tagName = "";

		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			tagName = parser.getName();
			switch (parser.getEventType())
			{
				case XmlPullParser.START_TAG:
				{
					if ((tagName.equals("Bus") && transportationType == 5) ||
							(tagName.equals("Metro") && transportationType == 3) ||
							(tagName.equals("Train") && transportationType == 1) ||
							(tagName.equals("Tram") && (transportationType == 2 || transportationType == 4)))
					{
						Departure departure = readDeparture(parser);
						if (departure != null)
						{
							departures.add(departure);
						}
					}
					break;
				}
			}
		}
		return departures;
	}

	private static Departure readDeparture(final XmlPullParser parser) throws XmlPullParserException, IOException
	{
		String endTagName = parser.getName();
		Departure departure = new Departure();
		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			String tagName = parser.getName();
			switch (parser.getEventType())
			{
				case XmlPullParser.START_TAG:
				{
					if (tagName.equals("Destination"))
					{
						departure.setDestination(XMLUtility.readText(parser, tagName));
					}
					else if (tagName.equals("TimeTabledDateTime"))
					{
						departure.setTimeTabledDateTime(DateUtility.parse(XMLUtility.readText(parser, tagName)));
					}
					else if (tagName.equals("ExpectedDateTime"))
					{
						departure.setExpectedDateTime(DateUtility.parse(XMLUtility.readText(parser, tagName)));
					}
					else if (tagName.equals("LineNumber"))
					{
						departure.setLine(XMLUtility.readText(parser, tagName));
					}
					else if (tagName.equals("StopAreaName"))
					{
						departure.setStopAreaName(XMLUtility.readText(parser, tagName));
					}
					else if (tagName.equals("DisplayTime"))
					{
						departure.setDisplayTime(XMLUtility.readText(parser, tagName));
					}
					else if (tagName.equals("StopAreaNumber"))
					{
						departure.setStopAreaNumber(Integer.parseInt(XMLUtility.readText(parser, tagName)));
					}
					else if (tagName.equals("JourneyDirection"))
					{
						departure.setDirection(Integer.parseInt(XMLUtility.readText(parser, tagName)));
					}
				}
				case XmlPullParser.END_TAG:
				{
					if (tagName.equals(endTagName))
					{
						return departure;
					}
				}
			}
		}
		return null;
	}

}
