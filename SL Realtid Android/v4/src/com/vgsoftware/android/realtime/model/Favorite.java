package com.vgsoftware.android.realtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

public class Favorite implements ISite
{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false)
	private int siteId;
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField(canBeNull = true)
	private String areaName;
	@DatabaseField(canBeNull = true)
	private String serializedSiteFilter; //Unused old field.

	public Favorite()
	{
	}

	public Favorite(Parcel parcel)
	{
		id = parcel.readInt();
		name = parcel.readString();
		siteId = parcel.readInt();
		areaName = parcel.readString();
		serializedSiteFilter = parcel.readString();
	}

	public Favorite(int siteId, String name)
	{
		this.siteId = siteId;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public int getSiteId()
	{
		return siteId;
	}

	public void setSiteId(int siteId)
	{
		this.siteId = siteId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getArea()
	{
		return areaName;
	}

	public void setArea(String areaName)
	{
		this.areaName = areaName;
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
		dest.writeString(name);
		dest.writeInt(siteId);
		dest.writeString(areaName);
		dest.writeString(serializedSiteFilter);
	}

	public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>()
	{
		public Favorite createFromParcel(Parcel in)
		{
			return new Favorite(in);
		}

		public Favorite[] newArray(int size)
		{
			return new Favorite[size];
		}
	};

	public String toString()
	{
		return name;
	}
}