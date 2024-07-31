package com.vgsoftware.android.realtime.ui.fragments;

import java.util.List;

import com.vgsoftware.android.realtime.model.Departure;

public interface IDepartureInformationChangedListener
{
	public void departureInformationUpdated(int transportationType, List<Departure> departures);
}
