package com.vgsoftware.android.realtime.model;

import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.realtime.LogManager;

import android.os.Parcel;
import android.os.Parcelable;

public class TrafficStatus implements Parcelable
{
	private String _name = null;
	private String _status = null;
	private List<TrafficEvent> _trafficEvent = null;

	public TrafficStatus()
	{
		_trafficEvent = new ArrayList<TrafficEvent>();
	}

	public TrafficStatus(Parcel in)
	{
		try
		{
			_name = in.readString();
			_status = in.readString();
			in.readList(_trafficEvent, TrafficEvent.class.getClassLoader());
		}
		catch (Exception ex)
		{
			LogManager.warn("Unable to unpack traffic status", ex);
		}
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}
	
	public String getStatus()
	{
		return _status;
	}
	
	public void setStatus(String status)
	{
		_status = status;
	}

	public List<TrafficEvent> getTrafficEvents()
	{
		return _trafficEvent;
	}

	public void setTrafficEvents(List<TrafficEvent> trafficEvent)
	{
		_trafficEvent = trafficEvent;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(_name);
		dest.writeString(_status);
		dest.writeList(_trafficEvent);
	}

	public static final Parcelable.Creator<TrafficStatus> CREATOR = new Parcelable.Creator<TrafficStatus>()
	{
		public TrafficStatus createFromParcel(Parcel in)
		{
			return new TrafficStatus(in);
		}

		public TrafficStatus[] newArray(int size)
		{
			return new TrafficStatus[size];
		}
	};
}
