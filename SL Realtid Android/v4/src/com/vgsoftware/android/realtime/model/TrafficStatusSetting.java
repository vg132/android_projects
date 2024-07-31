package com.vgsoftware.android.realtime.model;

import com.j256.ormlite.field.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class TrafficStatusSetting implements Parcelable
{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false, unique = true, index = true)
	private int widgetId;
	@DatabaseField(canBeNull = false)
	private boolean showBus;
	@DatabaseField(canBeNull = false)
	private boolean showBoat;
	@DatabaseField(canBeNull = false)
	private boolean showTrain;
	@DatabaseField(canBeNull = false)
	private boolean showSubway;
	@DatabaseField(canBeNull = false)
	private boolean showTram;
	@DatabaseField(canBeNull = false)
	private boolean showTram2;
	@DatabaseField(canBeNull = false)
	private long nextExecution = 0;

	public TrafficStatusSetting()
	{
	}

	public TrafficStatusSetting(Parcel in)
	{
		id = in.readInt();
		widgetId = in.readInt();
		showBus = in.readByte() == 1;
		showBoat = in.readByte() == 1;
		showTrain = in.readByte() == 1;
		showSubway = in.readByte() == 1;
		showTram = in.readByte() == 1;
		showTram2 = in.readByte() == 1;
		nextExecution = in.readLong();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getWidgetId()
	{
		return widgetId;
	}

	public void setWidgetId(int widgetId)
	{
		this.widgetId = widgetId;
	}

	public boolean isShowBus()
	{
		return showBus;
	}

	public void setShowBus(boolean showBus)
	{
		this.showBus = showBus;
	}

	public boolean isShowBoat()
	{
		return showBoat;
	}

	public void setShowBoat(boolean showBoat)
	{
		this.showBoat = showBoat;
	}

	public boolean isShowTrain()
	{
		return showTrain;
	}

	public void setShowTrain(boolean showTrain)
	{
		this.showTrain = showTrain;
	}

	public boolean isShowSubway()
	{
		return showSubway;
	}

	public void setShowSubway(boolean showSubway)
	{
		this.showSubway = showSubway;
	}

	public boolean isShowTram()
	{
		return showTram;
	}

	public void setShowTram(boolean showTram)
	{
		this.showTram = showTram;
	}

	public boolean isShowTram2()
	{
		return showTram2;
	}

	public void setShowTram2(boolean showTram2)
	{
		this.showTram2 = showTram2;
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
		dest.writeByte((byte) (showBus ? 1 : 0));
		dest.writeByte((byte) (showBoat ? 1 : 0));
		dest.writeByte((byte) (showTrain ? 1 : 0));
		dest.writeByte((byte) (showSubway ? 1 : 0));
		dest.writeByte((byte) (showTram ? 1 : 0));
		dest.writeByte((byte) (showTram2 ? 1 : 0));
		dest.writeLong(nextExecution);
	}

	public static final Parcelable.Creator<TrafficStatusSetting> CREATOR = new Parcelable.Creator<TrafficStatusSetting>()
	{
		public TrafficStatusSetting createFromParcel(Parcel in)
		{
			return new TrafficStatusSetting(in);
		}

		public TrafficStatusSetting[] newArray(int size)
		{
			return new TrafficStatusSetting[size];
		}
	};
}
