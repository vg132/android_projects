package com.vgsoftware.android.realtime.ui.controls;

import com.vgsoftware.android.realtime.dataabstraction.Station;
import com.vgsoftware.android.realtime.dataabstraction.TransportationType;

public interface OnSearchListener
{
	public abstract void onSearch(TransportationType transportationType, String stationName);
	public abstract void onSearch(TransportationType transportationType, Station station);
}
