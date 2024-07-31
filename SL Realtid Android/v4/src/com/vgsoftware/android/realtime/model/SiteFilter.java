package com.vgsoftware.android.realtime.model;

import com.j256.ormlite.field.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class SiteFilter implements Parcelable
{
	@DatabaseField(generatedId = true, columnName = "id")
	private int _id;
	@DatabaseField(canBeNull = false, columnName = "siteId")
	private int _siteId;
	@DatabaseField(canBeNull = true, columnName = "destination")
	private String _destination;
	@DatabaseField(canBeNull = true, columnName = "line")
	private String _line;

	public SiteFilter()
	{
	}

	public SiteFilter(Parcel parcel)
	{
		_id = parcel.readInt();
		_siteId = parcel.readInt();
		_destination = parcel.readString();
		_line = parcel.readString();
	}

	public int getId()
	{
		return _id;
	}

	public int getSiteId()
	{
		return _siteId;
	}

	public void setSiteId(int siteId)
	{
		_siteId = siteId;
	}

	public String getDestination()
	{
		return _destination;
	}

	public void setDestination(String destination)
	{
		_destination = destination;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(_id);
		dest.writeInt(_siteId);
		dest.writeString(_destination);
		dest.writeString(_line);
	}

	public static final Parcelable.Creator<SiteFilter> CREATOR = new Parcelable.Creator<SiteFilter>()
	{
		public SiteFilter createFromParcel(Parcel in)
		{
			return new SiteFilter(in);
		}

		public SiteFilter[] newArray(int size)
		{
			return new SiteFilter[size];
		}
	};
}
