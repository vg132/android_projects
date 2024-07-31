package com.vgsoftware.android.gamelibrary.integration.giantbomb.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Platform implements Parcelable
{
	public Platform()
	{
	}

	public Platform(Parcel in)
	{
		id = in.readInt();
		name = in.readString();
		abbreviation = in.readString();
	}

	public int id;
	public String api_detail_url;
	public String name;
	public String site_detail_url;
	public String abbreviation;

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeString(api_detail_url);
		dest.writeString(name);
		dest.writeString(site_detail_url);
		dest.writeString(abbreviation);
	}

	public static final Parcelable.Creator<Platform> CREATOR = new Parcelable.Creator<Platform>()
	{
		public Platform createFromParcel(Parcel in)
		{
			return new Platform(in);
		}

		public Platform[] newArray(int size)
		{
			return new Platform[size];
		}
	};
}
