package com.vgsoftware.android.gamelibrary.integration.giantbomb.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable
{
	public Genre()
	{
	}

	public Genre(Parcel in)
	{
		id = in.readInt();
		name = in.readString();
	}

	public int id;
	public String name;

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
	}

	public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>()
	{
		public Genre createFromParcel(Parcel in)
		{
			return new Genre(in);
		}

		public Genre[] newArray(int size)
		{
			return new Genre[size];
		}
	};
}
