package com.vgsoftware.android.realtime.parse;

import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;

import com.vgsoftware.android.realtime.dataabstraction.Station;
import com.vgsoftware.android.realtime.ui.SLRealTime;
import com.vgsoftware.android.vglib.HttpUtility;

public class RealTimeParser extends ParserBase
{
	private static final String DpsServiceUrl = "http://api.sl.se/api2/realtimedepartures.xml?key=&timewindow=60&siteid=";

	private Station _station;

	public RealTimeParser(Context context, Handler handler, Station station)
	{
		_handler = handler;
		_station = station;
	}

	public void run()
	{
		try
		{
			_data = new ArrayList<Departure>();
			String content = HttpUtility.getData(new URL(RealTimeParser.DpsServiceUrl + _station.getSiteId()));
			_data = DpsParser.parse(content, _station.getTransportationTypeId());
		}
		catch (Exception ex)
		{
			_exception = ex;
		}
		_handler.sendEmptyMessage(SLRealTime.HANDLER_MESSAGE_SL_REAL_TIME);
	}
}
