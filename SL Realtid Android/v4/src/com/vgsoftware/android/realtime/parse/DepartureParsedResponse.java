package com.vgsoftware.android.realtime.parse;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.vgsoftware.android.realtime.model.Departure;

public class DepartureParsedResponse implements Parcelable
{
	private List<Departure> _departures;
	private int _transporationType;

	public DepartureParsedResponse()
	{
	}

	public DepartureParsedResponse(Parcel in)
	{
		in.readList(_departures,Departure.class.getClassLoader());
		_transporationType = in.readInt();
	}

	public DepartureParsedResponse(int transportationType, List<Departure> departures)
	{
		_transporationType = transportationType;
		_departures = departures;
	}

	public List<Departure> getDepartures()
	{
		return _departures;
	}

	public void setDepartures(List<Departure> departures)
	{
		_departures = departures;
	}

	public int getTransportationType()
	{
		return _transporationType;
	}

	public void setTransportationType(int transportationType)
	{
		_transporationType = transportationType;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeList(_departures);
		dest.writeInt(_transporationType);
	}
	
	public static final Parcelable.Creator<DepartureParsedResponse> CREATOR = new Parcelable.Creator<DepartureParsedResponse>()
	{
		public DepartureParsedResponse createFromParcel(Parcel in)
		{
			return new DepartureParsedResponse(in);
		}

		public DepartureParsedResponse[] newArray(int size)
		{
			return new DepartureParsedResponse[size];
		}
	};
}
