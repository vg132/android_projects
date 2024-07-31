package com.vgsoftware.android.realtime.parse;

import java.util.List;

import com.vgsoftware.android.realtime.model.TrafficStatus;

public interface ITrafficStatusParsedListener
{
	public void trafficStatusParsed(List<TrafficStatus> trafficStatus);
}
