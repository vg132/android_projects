package com.vgsoftware.android.realtime.parse;

import android.os.Handler;

import com.vgsoftware.android.realtime.ui.SLRealTime;

public class TrafficSituationParser extends ParserBase
{
	private Handler _handler = null;

	public TrafficSituationParser(Handler handler)
	{
		_handler = handler;
	}

	public void run()
	{
		try
		{
			TrafficStatusParser parser = new TrafficStatusParser();
			_data = parser.parseTrafficStatus();
		}
		catch (Exception ex)
		{
			_exception = ex;
		}
		_handler.sendEmptyMessage(SLRealTime.HANDLER_MESSAGE_TRAFFIC_SITUATION);
	}
}
