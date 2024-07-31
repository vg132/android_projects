package com.vgsoftware.android.gamelibrary.integration.giantbomb.models;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable
{
	public Game()
	{
	}

	public Game(Parcel in)
	{
		id = in.readInt();
		image = in.readParcelable(Image.class.getClassLoader());
		name = in.readString();
		platforms = new ArrayList<Platform>();
		in.readList(platforms, Platform.class.getClassLoader());
	}

	public int id;
	public Image image;
	public String name;
	public List<Platform> platforms;

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeParcelable(image, Parcelable.CONTENTS_FILE_DESCRIPTOR);
		dest.writeString(name);
		dest.writeList(platforms);
	}

	public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>()
	{
		public Game createFromParcel(Parcel in)
		{
			return new Game(in);
		}

		public Game[] newArray(int size)
		{
			return new Game[size];
		}
	};
}
