package com.vgsoftware.android.realtime.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.vgsoftware.android.vglib.MapUtility;

@DatabaseTable(tableName = "StationV2")
public class Station implements ISite
{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int siteId;
	@DatabaseField
	private String name;
	@DatabaseField
	private String alias;
	@DatabaseField
	private double latitude;
	@DatabaseField
	private double longitude;
	@DatabaseField
	private boolean hasTrain;
	@DatabaseField
	private boolean hasSubway;
	@DatabaseField
	private boolean hasTram;
	@DatabaseField
	private boolean hasBus;

	public Station()
	{
	}

	public Station(Parcel parcel)
	{
		id = parcel.readInt();
		siteId = parcel.readInt();
		name = parcel.readString();
		alias = parcel.readString();
		latitude = parcel.readDouble();
		longitude = parcel.readDouble();
		hasTrain = parcel.readByte() == 1;
		hasSubway = parcel.readByte() == 1;
		hasTram = parcel.readByte() == 1;
		hasBus = parcel.readByte() == 1;
	}

	public Station(String name, int siteId, double latitude, double longitude, String alias, boolean hasTrain, boolean hasSubway, boolean hasTram, boolean hasBus)
	{
		this.name = name;
		this.siteId = siteId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.alias = alias;
		this.hasTrain = hasTrain;
		this.hasSubway = hasSubway;
		this.hasTram = hasTram;
		this.hasBus = hasBus;
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
		return "";
	}

	public void setArea(String name)
	{
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public boolean getHasBus()
	{
		return hasBus;
	}

	public boolean getHasTrain()
	{
		return hasTrain;
	}

	public boolean getHasSubway()
	{
		return hasSubway;
	}

	public boolean getHasTram()
	{
		return hasTram;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	public double getDistanceInMeters(Location location)
	{
		return MapUtility.getDistance(location.getLatitude(), location.getLongitude(), this.getLatitude(), this.getLongitude()) * 1000;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeInt(siteId);
		dest.writeString(name);
		dest.writeString(alias);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeByte((byte) (hasTrain ? 1 : 0));
		dest.writeByte((byte) (hasSubway ? 1 : 0));
		dest.writeByte((byte) (hasTram ? 1 : 0));
		dest.writeByte((byte) (hasBus ? 1 : 0));
	}

	public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>()
	{
		public Station createFromParcel(Parcel in)
		{
			return new Station(in);
		}

		public Station[] newArray(int size)
		{
			return new Station[size];
		}
	};

	public String toString()
	{
		return name;
	}
}
