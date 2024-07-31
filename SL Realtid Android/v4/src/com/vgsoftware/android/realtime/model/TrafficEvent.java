package com.vgsoftware.android.realtime.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrafficEvent implements Parcelable
{
	private String _message = null;
	private String _line = null;

	public TrafficEvent()
	{
	}

	public TrafficEvent(Parcel in)
	{
		_message = in.readString();
		_line = in.readString();
	}

	public String getMessage()
	{
		return _message;
	}

	public void setMessage(String message)
	{
		_message = message;
	}

	public String getLine()
	{
		return _line;
	}

	public void setLine(String line)
	{
		_line = line;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(_message);
		dest.writeString(_line);
	}

	public static final Parcelable.Creator<TrafficEvent> CREATOR = new Parcelable.Creator<TrafficEvent>()
	{
		public TrafficEvent createFromParcel(Parcel in)
		{
			return new TrafficEvent(in);
		}

		public TrafficEvent[] newArray(int size)
		{
			return new TrafficEvent[size];
		}
	};
}
