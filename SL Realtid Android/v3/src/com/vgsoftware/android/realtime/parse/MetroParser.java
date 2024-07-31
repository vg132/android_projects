package com.vgsoftware.android.realtime.parse;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vgsoftware.android.vglib.StringUtility;
import com.vgsoftware.android.vglib.XMLUtility;

import android.util.Xml;

public class MetroParser
{
	private static final Pattern MetroRegexPattern = Pattern.compile("(\\d\\d)(.*?)(\\d+:\\d+|\\d+|$)");

	public static List<Departure> parse(String content)
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(new StringReader(content));
			parser.nextTag();
			return parseDepartures(parser);
		}
		catch (Exception e)
		{
		}
		return new ArrayList<Departure>();
	}

	private static List<Departure> parseDepartures(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		List<Departure> result = new ArrayList<Departure>();
		String tagName = "";

		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			tagName = parser.getName();
			switch (parser.getEventType())
			{
				case XmlPullParser.START_TAG:
				{
					if (tagName.equals("DisplayRow1") || tagName.equals("DisplayRow2"))
					{
						String displayText = XMLUtility.readText(parser, tagName).replace("min.", "min,");
						for (String row : displayText.split(","))
						{
							readDeparture(fixDeparture(row), result);
						}
					}
					break;
				}
			}
		}
		return result;
	}

	private static void readDeparture(final String text, final List<Departure> result)
	{
		Matcher matcher = MetroParser.MetroRegexPattern.matcher(text);
		while (matcher.find())
		{
			Departure departure = new Departure();
			departure.setLine(matcher.group(1).trim());
			departure.setDestination(matcher.group(2).trim());
			String displayTime = matcher.group(3).trim();
			if (StringUtility.isNullOrEmpty(displayTime))
			{
				displayTime = "0";
			}
			departure.setTimeTabledDateTime(getDate(displayTime));
			departure.setDisplayTime(displayTime + (displayTime.contains(":") ? "" : " min"));
			if (!StringUtility.isNullOrEmpty(departure.getLine()) && !StringUtility.isNullOrEmpty(departure.getDestination())
					&& !StringUtility.isNullOrEmpty(departure.getDisplayTime()) && departure.getDestination().length() > 3)
			{
				result.add(departure);
			}
		}
	}

	private static Date getDate(final String displayTime)
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));
		if (displayTime.contains(":"))
		{
			String[] timeParts = displayTime.split(":");
			if (timeParts.length == 2)
			{
				try
				{
					int hour = Integer.parseInt(timeParts[0]);
					int minutes = Integer.parseInt(timeParts[1]);
					if (hour < calendar.get(Calendar.HOUR_OF_DAY))
					{
						calendar.add(Calendar.DAY_OF_MONTH, 1);
					}
					calendar.set(Calendar.HOUR_OF_DAY, hour);
					calendar.set(Calendar.MINUTE, minutes);
				}
				catch (Exception ex)
				{
				}
			}
		}
		else
		{
			calendar.add(Calendar.MINUTE, Integer.parseInt(displayTime));
		}
		return calendar.getTime();
	}

	private static String fixDeparture(final String departure)
	{
		String fixedDeparture = departure.trim().replace("min.", "min");
		for (int i = 0; i < 10; i++)
		{
			fixedDeparture = fixedDeparture.replace("  ", " ");
		}
		return fixedDeparture;
	}
}
