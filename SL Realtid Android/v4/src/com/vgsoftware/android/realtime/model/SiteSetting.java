package com.vgsoftware.android.realtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.vgsoftware.android.realtime.Constants;

public class SiteSetting implements Parcelable
{
	@DatabaseField(index = true, unique = true, id = true)
	private int siteId;
	@DatabaseField
	private int selectedTab = Constants.TRANSPORTATION_TYPE_TRAIN;
	@DatabaseField
	private long lastSearch = 0;

	public SiteSetting()
	{
	}

	public SiteSetting(int siteId, int selectedTab)
	{
		this.siteId = siteId;
		this.selectedTab = selectedTab;
	}

	public SiteSetting(Parcel parcel)
	{
		siteId = parcel.readInt();
		selectedTab = parcel.readInt();
		lastSearch = parcel.readLong();
	}

	public int getSiteId()
	{
		return siteId;
	}

	public void setSiteId(int siteId)
	{
		this.siteId = siteId;
	}

	public int getSelectedTab()
	{
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab)
	{
		this.selectedTab = selectedTab;
	}

	public long getLastSearch()
	{
		return lastSearch;
	}

	public void setLastSearch(long lastSearch)
	{
		this.lastSearch = lastSearch;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(siteId);
		dest.writeInt(selectedTab);
		dest.writeLong(lastSearch);
	}

	public static final Parcelable.Creator<SiteSetting> CREATOR = new Parcelable.Creator<SiteSetting>()
	{
		public SiteSetting createFromParcel(Parcel in)
		{
			return new SiteSetting(in);
		}

		public SiteSetting[] newArray(int size)
		{
			return new SiteSetting[size];
		}
	};
}
