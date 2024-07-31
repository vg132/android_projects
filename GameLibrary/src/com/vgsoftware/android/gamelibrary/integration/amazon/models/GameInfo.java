package com.vgsoftware.android.gamelibrary.integration.amazon.models;

import android.os.Parcel;
import android.os.Parcelable;

public class GameInfo implements Parcelable
{
	public String Title;
	public String Publisher;
	public int Score;
	public String Platform;
	public String OriginalTitle;

	public GameInfo()
	{
	}

	public GameInfo(Parcel parcel)
	{
		Title = parcel.readString();
		Publisher = parcel.readString();
		Score = parcel.readInt();
		Platform = parcel.readString();
		OriginalTitle = parcel.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(Title);
		dest.writeString(Publisher);
		dest.writeInt(Score);
		dest.writeString(Platform);
		dest.writeString(OriginalTitle);
	}

	public static final Parcelable.Creator<GameInfo> CREATOR = new Parcelable.Creator<GameInfo>()
	{
		public GameInfo createFromParcel(Parcel in)
		{
			return new GameInfo(in);
		}

		public GameInfo[] newArray(int size)
		{
			return new GameInfo[size];
		}
	};

}
