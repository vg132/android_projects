package com.vgsoftware.android.babysleep.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class Sleep implements Parcelable
{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false)
	private Date startDate;
	@DatabaseField(canBeNull = true)
	private Date endDate;

	public Sleep()
	{
	}

	public Sleep(Parcel parcel)
	{
		id = parcel.readInt();
		startDate = new Date(parcel.readLong());
		endDate = new Date(parcel.readLong());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeLong(startDate.getTime());
		dest.writeLong(endDate.getTime());
	}
	
	public static final Parcelable.Creator<Sleep> CREATOR = new Parcelable.Creator<Sleep>()
	{
		public Sleep createFromParcel(Parcel in)
		{
			return new Sleep(in);
		}

		public Sleep[] newArray(int size)
		{
			return new Sleep[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}
}
