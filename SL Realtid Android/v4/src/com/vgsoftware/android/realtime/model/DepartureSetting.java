package com.vgsoftware.android.realtime.model;

import com.j256.ormlite.field.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class DepartureSetting implements Parcelable
{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false, index = true)
	private int widgetId;
	@DatabaseField(canBeNull = false)
	private int siteId;
	@DatabaseField(canBeNull = false)
	private boolean autoUpdate;
	@DatabaseField(canBeNull = false)
	private int transportationType;
	@DatabaseField(canBeNull = false)
	private long nextExecution = 0;

	public DepartureSetting()
	{

	}

	public DepartureSetting(Parcel in)
	{
		id = in.readInt();
		widgetId = in.readInt();
		siteId = in.readInt();
		autoUpdate = in.readByte() == 1;
		transportationType = in.readInt();
		nextExecution = in.readLong();
	}

	public int getId()
	{
		return id;
	}

	public int getWidgetId()
	{
		return widgetId;
	}

	public void setWidgetId(int widgetId)
	{
		this.widgetId = widgetId;
	}

	public int getSiteId()
	{
		return siteId;
	}

	public void setSiteId(int siteId)
	{
		this.siteId = siteId;
	}

	public boolean isAutoUpdate()
	{
		return autoUpdate;
	}

	public void setAutoUpdate(boolean autoUpdate)
	{
		this.autoUpdate = autoUpdate;
	}

	public int getTransportationType()
	{
		return transportationType;
	}

	public void setTransportationType(int transportationType)
	{
		this.transportationType = transportationType;
	}

	public long getNextExecution()
	{
		return nextExecution;
	}

	public void setNextExecution(long nextExecution)
	{
		this.nextExecution = nextExecution;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeInt(widgetId);
		dest.writeInt(siteId);
		dest.writeByte((byte) (autoUpdate ? 1 : 0));
		dest.writeInt(transportationType);
		dest.writeLong(nextExecution);
	}

	public static final Parcelable.Creator<DepartureSetting> CREATOR = new Parcelable.Creator<DepartureSetting>()
	{
		public DepartureSetting createFromParcel(Parcel in)
		{
			return new DepartureSetting(in);
		}

		public DepartureSetting[] newArray(int size)
		{
			return new DepartureSetting[size];
		}
	};
}
