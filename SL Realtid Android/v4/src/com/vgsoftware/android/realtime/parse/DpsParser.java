package com.vgsoftware.android.realtime.parse;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.vgsoftware.android.realtime.Constants;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.model.Departure;
import com.vgsoftware.android.vglib.DateUtility;
import com.vgsoftware.android.vglib.XMLUtility;

public class DpsParser
{
	public static DepartureParserResult parse(String content)
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(IOUtils.toInputStream(content), null);
			parser.nextTag();
			return parseDepartures(parser);
		}
		catch (Exception e)
		{
			LogManager.error("Unable to parse dps xml", e);
		}
		return new DepartureParserResult();
	}

	private static DepartureParserResult parseDepartures(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		DepartureParserResult result = new DepartureParserResult();
		String tagName = "";

		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			tagName = parser.getName();
			switch (parser.getEventType())
			{
				case XmlPullParser.START_TAG:
				{
					Departure departure = null;
					if (tagName.equals("Bus"))
					{
						departure = readDeparture(parser);
						departure.setTransportationType(Constants.TRANSPORTATION_TYPE_BUS);
					}
					else if (tagName.equals("Metro"))
					{
						departure = readDeparture(parser);
						departure.setTransportationType(Constants.TRANSPORTATION_TYPE_METRO);
					}
					else if (tagName.equals("Train"))
					{
						departure = readDeparture(parser);
						departure.setTransportationType(Constants.TRANSPORTATION_TYPE_TRAIN);
					}
					else if (tagName.equals("Tram"))
					{
						departure = readDeparture(parser);
						departure.setTransportationType(Constants.TRANSPORTATION_TYPE_TRAM);
					}
					if (departure != null)
					{
						result.add(departure.getTransportationType(), departure);
					}
					break;
				}
			}
		}
		return result;
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
					else if (tagName.equals("GroupOfLine"))
					{
						departure.setGroupOfLine(XMLUtility.readText(parser, tagName));
					}
					else if (tagName.equals("GroupOfLineId"))
					{
						departure.setGroupOfLineId(Integer.parseInt(XMLUtility.readText(parser, tagName)));
					}
					else if (tagName.equals("PlatformMessage"))
					{
						departure.setPlatformMessage(XMLUtility.readText(parser, tagName));
					}
					else if (tagName.equals("StopPointDesignation"))
					{
						departure.setStopPointDesignation(XMLUtility.readText(parser, tagName));
					}
					else if (tagName.equals("StopPointNumber"))
					{
						departure.setStopPointNumber(Integer.parseInt(XMLUtility.readText(parser, tagName)));
					}
					else if (tagName.equals("SecondaryDestinationName"))
					{
						departure.setSecondaryDestinationName(XMLUtility.readText(parser, tagName));
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
