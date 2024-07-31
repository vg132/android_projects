package com.vgsoftware.android.realtime;

import android.os.Handler;
import android.app.Activity;
import java.util.ArrayList;

public class RealTimeData extends Thread
{
	private Handler _handler;
	private int _selectedIndex;
	private Activity _activity;
	private String _stationName;
	
	ArrayList<Departure> _departures=new ArrayList<Departure>();
	
	public RealTimeData(Activity activity, Handler handler, int selectedIndex, String stationName)
	{
		_activity=activity;
		_handler=handler;
		_selectedIndex=selectedIndex;
		_stationName=stationName;
	}
	
	public void run()
	{
		switch(_selectedIndex)
		{
			case 0:
				_departures = Parser.getRealTimeInformationCommuterTrain(getUrl());
				break;
			case 1:
				_departures = Parser.getRealTimeInformationCommuterTrain(getUrl());
				break;
			case 2:
				_departures = Parser.getRealTimeInformationSubway(getUrl());
				break;
			case 3:
				_departures = Parser.getRealTimeInformationSubway(getUrl());
				break;
			case 4:
				_departures = Parser.getRealTimeInformationSubway(getUrl());
				break;
		}
		_handler.sendEmptyMessage(0);
	}
	
	private String getUrl()
	{
		String url = null;
		int stationId = 0;
		switch(_selectedIndex)
		{
			case 0:
				stationId=getStationId(R.array.CommuterTrainName,R.array.CommuterTrainId);
				if(stationId>=0)
				{
					url=_activity.getResources().getString(R.string.CommuterTrainUrl) + stationId;
				}
				break;
			case 1:
				stationId=getStationId(R.array.RoslagsbanaName,R.array.RoslagsbananId);
				if(stationId>=0)
				{
					url=_activity.getResources().getString(R.string.RoslagsbanaUrl) + stationId;
				}
				break;
			case 2:
				stationId=getStationId(R.array.GreenLineName,R.array.GreenLineId);
				if(stationId>=0)
				{
					url=_activity.getResources().getString(R.string.SubwayUrl) + stationId;	
				}
				break;
			case 3:
				stationId=getStationId(R.array.RedLineName,R.array.RedLineId);
				if(stationId>=0)
				{
					url=_activity.getResources().getString(R.string.SubwayUrl) + stationId;	
				}
				break;
			case 4:
				stationId=getStationId(R.array.BlueLineName,R.array.BlueLineId);
				if(stationId>=0)
				{
					url=_activity.getResources().getString(R.string.SubwayUrl) + stationId;	
				}
				break;
			default:
				break;
		}
		GoogleAnalytics.getInstance().trackEvent("SL","LoadData","Station",stationId);
		return url;
	}

	private int getStationId(int nameResourceArray, int idResourceArray)
	{
		int ids[] = _activity.getResources().getIntArray(idResourceArray);
		String stations[] = _activity.getResources().getStringArray(nameResourceArray);
		for(int i=0;i<stations.length;i++)
		{
			if(stations[i].equalsIgnoreCase(_stationName))
			{
				return ids[i];
			}
		}
		return -1;
	}
	
	public ArrayList<Departure> getDepartures()
	{
		return _departures;
	}
}
