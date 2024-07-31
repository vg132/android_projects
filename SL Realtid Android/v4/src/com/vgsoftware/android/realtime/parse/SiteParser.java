package com.vgsoftware.android.realtime.parse;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Xml;

import com.vgsoftware.android.realtime.CacheRepository;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.SLRealtidApplication;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.model.ISite;
import com.vgsoftware.android.realtime.model.Site;
import com.vgsoftware.android.vglib.HttpUtility;
import com.vgsoftware.android.vglib.StringUtility;
import com.vgsoftware.android.vglib.XMLUtility;

public class SiteParser
{
	private static final String SiteServiceUrl = "http://api.sl.se/api2/typeahead.xml?key=&maxresults=50&searchstring=";
	private ISiteParsedListener _listener = null;

	public void setOnSiteParsedListener(ISiteParsedListener listener)
	{
		_listener = listener;
	}

	private void onSiteParsed(List<ISite> sites)
	{
		if (_listener != null)
		{
			_listener.sitesParsed(sites);
		}
	}

	public void siteSearchAsync(String query)
	{
		SiteParserTask task = new SiteParserTask();
		task.execute(query);
	}

	public synchronized List<ISite> siteSearch(final String query)
	{
		final String searchQuery = query.toLowerCase(Locale.getDefault());
		Tracking.sendEvent(SLRealtidApplication.getAppContext(), Tracking.CATEGORY_NETWORK, "SiteParser", searchQuery);
		String cacheKey = String.format(Locale.getDefault(), "__Site_Parser_Cache_%s__", searchQuery);
		String content = (String) CacheRepository.getInstance().get(cacheKey);
		if (StringUtility.isNullOrEmpty(content))
		{
			try
			{
				content = HttpUtility.getData(new URL(SiteParser.SiteServiceUrl + URLEncoder.encode(searchQuery, "UTF-8")));
				CacheRepository.getInstance().put(cacheKey, content);
			}
			catch (IOException e)
			{
				LogManager.warn("Unable to parse site data", e);
			}
		}
		if (!StringUtility.isNullOrEmpty(content))
		{
			return parse(content);
		}
		return new ArrayList<ISite>(0);
	}

	private class SiteParserTask extends AsyncTask<String, Void, List<ISite>>
	{
		@Override
		protected List<ISite> doInBackground(String... params)
		{
			if (params != null && params.length > 0)
			{
				return siteSearch(params[0]);
			}
			return new ArrayList<ISite>();
		}

		@Override
		protected void onPostExecute(List<ISite> result)
		{
			onSiteParsed(result);
			super.onPostExecute(result);
		}
	}

	private List<ISite> parse(String content)
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(IOUtils.toInputStream(content), null);
			parser.nextTag();
			return parseSites(parser);
		}
		catch (Exception e)
		{
		}
		return new ArrayList<ISite>();
	}

	private List<ISite> parseSites(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		List<ISite> sites = new ArrayList<ISite>();
		String tagName = "";
		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			tagName = parser.getName();
			switch (parser.getEventType())
			{
				case XmlPullParser.START_TAG:
					if (tagName.equals("Site"))
					{
						ISite site = readSite(parser);
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

	private ISite readSite(XmlPullParser parser) throws XmlPullParserException, IOException
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
				site.setArea(area);
			}
			else
			{
				XMLUtility.skip(parser);
			}
		}
		return site;
	}
}
