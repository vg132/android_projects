package com.vgsoftware.android.realtime.parse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Xml;

import com.vgsoftware.android.realtime.CacheRepository;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.model.TrafficEvent;
import com.vgsoftware.android.realtime.model.TrafficStatus;
import com.vgsoftware.android.vglib.HttpUtility;
import com.vgsoftware.android.vglib.StringUtility;
import com.vgsoftware.android.vglib.XMLUtility;

public class TrafficStatusParser
{
	private static final String TrafficServiceUrl = "http://api.sl.se/api2/trafficsituation.xml?key=";
	private static final String CacheKey = "__Traffic_Status_Parser_Cahce__";
	private ITrafficStatusParsedListener _listener = null;

	public void setOnTrafficStatusParsedListener(ITrafficStatusParsedListener listener)
	{
		_listener = listener;
	}

	private void onTrafficStatusParsed(List<TrafficStatus> trafficStatus)
	{
		if (_listener != null)
		{
			_listener.trafficStatusParsed(trafficStatus);
		}
	}

	public void parseTrafficStatusAsync()
	{
		TrafficStatusTask task = new TrafficStatusTask();
		task.execute();
	}

	public synchronized List<TrafficStatus> parseTrafficStatus()
	{
		List<TrafficStatus> trafficStatus = new ArrayList<TrafficStatus>();
		String content = (String) CacheRepository.getInstance().get(TrafficStatusParser.CacheKey);
		if (StringUtility.isNullOrEmpty(content))
		{
			try
			{
				content = HttpUtility.getData(new URL(TrafficStatusParser.TrafficServiceUrl));
				CacheRepository.getInstance().put(TrafficStatusParser.CacheKey, content, 60);
			}
			catch (Exception e)
			{
				LogManager.warn("Unable to parse trffic status", e);
			}
		}
		if (!StringUtility.isNullOrEmpty(content))
		{
			return parse(content);
		}
		return trafficStatus;
	}

	private class TrafficStatusTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				List<TrafficStatus> trafficStatus = parseTrafficStatus();
				onTrafficStatusParsed(trafficStatus);
			}
			catch (Exception ex)
			{
				LogManager.error("Unable to parse traffic status", ex);
			}
			return null;
		}
	}

	private List<TrafficStatus> parse(String content)
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(IOUtils.toInputStream(content), null);
			parser.nextTag();
			return parseTrafficStauts(parser);
		}
		catch (Exception e)
		{
		}
		return new ArrayList<TrafficStatus>();
	}

	private List<TrafficStatus> parseTrafficStauts(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		List<TrafficStatus> trafficStatus = new ArrayList<TrafficStatus>();
		TrafficStatus status = null;
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
						status = new TrafficStatus();
						status.setName(parser.getAttributeValue(null, "Name"));
						status.setStatus(parser.getAttributeValue(null, "StatusIcon"));
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
