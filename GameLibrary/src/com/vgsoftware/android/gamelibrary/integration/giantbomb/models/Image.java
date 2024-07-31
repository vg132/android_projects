package com.vgsoftware.android.gamelibrary.integration.giantbomb.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable
{
	public Image()
	{
	}

	public Image(Parcel in)
	{
		icon_url = in.readString();
		medium_url = in.readString();
		screen_url = in.readString();
		small_url = in.readString();
		super_url = in.readString();
		thumb_url = in.readString();
		tiny_url = in.readString();
	}

	public String icon_url;
	public String medium_url;
	public String screen_url;
	public String small_url;
	public String super_url;
	public String thumb_url;
	public String tiny_url;

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(icon_url);
		dest.writeString(medium_url);
		dest.writeString(screen_url);
		dest.writeString(small_url);
		dest.writeString(super_url);
		dest.writeString(thumb_url);
		dest.writeString(tiny_url);
	}

	public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>()
	{
		public Image createFromParcel(Parcel in)
		{
			return new Image(in);
		}

		public Image[] newArray(int size)
		{
			return new Image[size];
		}
	};
}
