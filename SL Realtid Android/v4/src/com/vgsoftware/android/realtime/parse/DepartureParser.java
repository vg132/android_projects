package com.vgsoftware.android.realtime.parse;

import java.net.URL;
import java.util.Locale;

import android.os.AsyncTask;

import com.vgsoftware.android.realtime.CacheRepository;
import com.vgsoftware.android.realtime.Constants;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.SLRealtidApplication;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.model.ISite;
import com.vgsoftware.android.vglib.HttpUtility;
import com.vgsoftware.android.vglib.StringUtility;

public class DepartureParser
{
	private static final String DpsServiceUrl = "http://api.sl.se/api2/realtimedepartures.xml?key=&timewindow=60&siteid=";
	private IDepartureParsedListener _listener = null;
	private IDepartureParserError _errorListener = null;

	public void setOnDepartureParsedListener(IDepartureParsedListener listener)
	{
		_listener = listener;
	}

	public void setOnDepartueParserError(IDepartureParserError listener)
	{
		_errorListener = listener;
	}

	private void onDepartureParsed(DepartureParsedResponse response)
	{
		if (_listener != null)
		{
			_listener.departuresParsed(response);
		}
	}

	private void onDepartueParserError()
	{
		if (_errorListener != null)
		{
			_errorListener.departureParserError();
		}
	}

	public void parseDeparturesAsync(ISite site)
	{
		Tracking.sendEvent(SLRealtidApplication.getAppContext(), Tracking.CATEGORY_NETWORK, "DepartureParser", site.getName());
		DpsParserTask dpsParserTask = new DpsParserTask();
		dpsParserTask.execute(site);
	}

	public DepartureParserResult parseDepartures(final ISite site)
	{
		final DepartureParserResult result = new DepartureParserResult();

		DepartureParserResult dpsResult = dpsParser(site);
		result.set(Constants.TRANSPORTATION_TYPE_TRAIN, dpsResult.get(Constants.TRANSPORTATION_TYPE_TRAIN));
		result.set(Constants.TRANSPORTATION_TYPE_BUS, dpsResult.get(Constants.TRANSPORTATION_TYPE_BUS));
		result.set(Constants.TRANSPORTATION_TYPE_TRAM, dpsResult.get(Constants.TRANSPORTATION_TYPE_TRAM));
		result.set(Constants.TRANSPORTATION_TYPE_METRO, dpsResult.get(Constants.TRANSPORTATION_TYPE_METRO));

		return result;
	}

	private DepartureParserResult dpsParser(ISite site)
	{
		String cacheKey = String.format(Locale.getDefault(), "__Dps_Parser_Cache_%d__", site.getSiteId());
		String content = (String) CacheRepository.getInstance().get(cacheKey);
		if (StringUtility.isNullOrEmpty(content))
		{
			try
			{
				content = HttpUtility.getData(new URL(DepartureParser.DpsServiceUrl + site.getSiteId()));
				CacheRepository.getInstance().put(cacheKey, content, 60);
			}
			catch (Exception ex)
			{
				LogManager.error("Unable to download departures", ex);
			}
		}
		if (!StringUtility.isNullOrEmpty(content))
		{
			DepartureParserResult result = DpsParser.parse(content);
			for (int transportationType : result.keys())
			{
				onDepartureParsed(new DepartureParsedResponse(transportationType, result.get(transportationType)));
			}
			return result;
		}
		return null;
	}

	private class DpsParserTask extends AsyncTask<ISite, Void, Void>
	{
		@Override
		protected Void doInBackground(ISite... params)
		{
			if (params != null && params.length > 0 && params[0] != null)
			{
				if (dpsParser(params[0]) == null)
				{
					onDepartueParserError();
				}
			}
			return null;
		}
	}
}
