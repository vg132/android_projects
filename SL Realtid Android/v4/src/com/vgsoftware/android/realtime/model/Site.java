package com.vgsoftware.android.realtime.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;

@DatabaseTable(tableName = "SiteCache")
public class Site implements ISite
{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int siteId;
	@DatabaseField
	private String name = null;
	@DatabaseField
	private String area = null;
	@DatabaseField(index = true)
	private String query = null;
	@DatabaseField
	private long validTo = 0;

	public Site()
	{
	}

	public Site(ISite site)
	{
		this.area = site.getArea();
		this.name = site.getName();
		this.siteId = site.getSiteId();
	}

	public Site(Parcel in)
	{
		name = in.readString();
		area = in.readString();
		siteId = in.readInt();
		query = in.readString();
		validTo = in.readLong();
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
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public long getValidTo()
	{
		return validTo;
	}

	public void setValidTo(long validTo)
	{
		this.validTo = validTo;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(name);
		dest.writeString(area);
		dest.writeInt(siteId);
		dest.writeString(query);
		dest.writeLong(validTo);
	}

	public static final Parcelable.Creator<Site> CREATOR = new Parcelable.Creator<Site>()
	{
		public Site createFromParcel(Parcel in)
		{
			return new Site(in);
		}

		public Site[] newArray(int size)
		{
			return new Site[size];
		}
	};
}