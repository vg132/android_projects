package com.vgsoftware.android.realtime.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Departure implements Parcelable
{
	private String _destination;
	private Date _timeTabledDateTime;
	private Date _expectedDateTime;
	private String _line;
	private String _stopAreaName;
	private String _displayTime;
	private int _stopAreaNumber;
	private int _direction;
	private int _transportationType;

	private String _groupOfLine;
	private int _groupOfLineId;
	private String _platformMessage;
	private String _stopPointDesignation;
	private int _stopPointNumber;
	private String _secondaryDestinationName;

	public Departure()
	{
	}

	public Departure(Parcel in)
	{
		_destination = in.readString();
		long date = in.readLong();
		if (date > 0)
		{
			_timeTabledDateTime = new Date(date);
		}
		date = in.readLong();
		if (date > 0)
		{
			_expectedDateTime = new Date(date);
		}
		_line = in.readString();
		_stopAreaName = in.readString();
		_displayTime = in.readString();
		_stopAreaNumber = in.readInt();
		_direction = in.readInt();
		_transportationType = in.readInt();
		_groupOfLineId = in.readInt();
		_groupOfLine = in.readString();
		_platformMessage = in.readString();
		_stopPointNumber = in.readInt();
		_stopPointDesignation = in.readString();
		_secondaryDestinationName = in.readString();
	}

	public String getDestination()
	{
		return _destination;
	}

	public void setDestination(String destination)
	{
		_destination = destination;
	}

	public Date getTimeTabledDateTime()
	{
		return _timeTabledDateTime;
	}

	public void setTimeTabledDateTime(Date time)
	{
		_timeTabledDateTime = time;
	}

	public Date getExpectedDateTime()
	{
		return _expectedDateTime;
	}

	public void setExpectedDateTime(Date expectedTime)
	{
		_expectedDateTime = expectedTime;
	}

	public String getLine()
	{
		return _line;
	}

	public void setLine(String line)
	{
		_line = line;
	}

	public void setStopAreaName(String stopAreaName)
	{
		_stopAreaName = stopAreaName;
	}

	public String getStopAreaName()
	{
		return _stopAreaName;
	}

	public void setDisplayTime(String displayTime)
	{
		_displayTime = displayTime;
	}

	public String getDisplayTime()
	{
		return _displayTime;
	}

	public int getStopAreaNumber()
	{
		return _stopAreaNumber;
	}

	public void setStopAreaNumber(int stopAreaNumber)
	{
		_stopAreaNumber = stopAreaNumber;
	}

	public int getDirection()
	{
		return _direction;
	}

	public void setDirection(int direction)
	{
		_direction = direction;
	}

	public int getTransportationType()
	{
		return _transportationType;
	}

	public void setTransportationType(int transportationType)
	{
		_transportationType = transportationType;
	}

	public String getGroupOfLine()
	{
		return _groupOfLine;
	}

	public void setGroupOfLine(String groupOfLine)
	{
		_groupOfLine = groupOfLine;
	}

	public int getGroupOfLineId()
	{
		return _groupOfLineId;
	}

	public void setGroupOfLineId(int groupOfLineId)
	{
		_groupOfLineId = groupOfLineId;
	}

	public String getPlatformMessage()
	{
		return _platformMessage;
	}

	public void setPlatformMessage(String platformMessage)
	{
		_platformMessage = platformMessage;
	}

	public String getStopPointDesignation()
	{
		return _stopPointDesignation;
	}

	public void setStopPointDesignation(String stopPointDesignation)
	{
		_stopPointDesignation = stopPointDesignation;
	}

	public int getStopPointNumber()
	{
		return _stopPointNumber;
	}

	public void setStopPointNumber(int stopPointNumber)
	{
		_stopPointNumber = stopPointNumber;
	}

	public String getSecondaryDestinationName()
	{
		return _secondaryDestinationName;
	}

	public void setSecondaryDestinationName(String secondaryDestinationName)
	{
		_secondaryDestinationName = secondaryDestinationName;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(_destination);
		dest.writeLong(_timeTabledDateTime != null ? _timeTabledDateTime.getTime() : 0);
		dest.writeLong(_expectedDateTime != null ? _expectedDateTime.getTime() : 0);
		dest.writeString(_line);
		dest.writeString(_stopAreaName);
		dest.writeString(_displayTime);
		dest.writeInt(_stopAreaNumber);
		dest.writeInt(_direction);
		dest.writeInt(_transportationType);
		dest.writeInt(_groupOfLineId);
		dest.writeString(_groupOfLine);
		dest.writeString(_platformMessage);
		dest.writeInt(_stopPointNumber);
		dest.writeString(_stopPointDesignation);
		dest.writeString(_secondaryDestinationName);
	}

	public static final Parcelable.Creator<Departure> CREATOR = new Parcelable.Creator<Departure>()
	{
		public Departure createFromParcel(Parcel in)
		{
			return new Departure(in);
		}

		public Departure[] newArray(int size)
		{
			return new Departure[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}
}
