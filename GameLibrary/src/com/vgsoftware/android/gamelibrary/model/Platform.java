package com.vgsoftware.android.gamelibrary.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;

@DatabaseTable(tableName = "Platform")
public class Platform implements Parcelable
{
	public final static String FIELD_NAME_ID = "id";
	public final static String FIELD_NAME_NAME = "name";
	public final static String FIELD_NAME_ABBREVIATION = "abbreviation";

	@DatabaseField(generatedId = false, uniqueIndex = true, id = true, columnName = Platform.FIELD_NAME_ID)
	private int _id;
	@DatabaseField(canBeNull = false, columnName = Platform.FIELD_NAME_NAME)
	private String _name;
	@DatabaseField(canBeNull = true, columnName = Platform.FIELD_NAME_ABBREVIATION)
	private String _abbreviation;

	public Platform()
	{
	}

	public Platform(com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Platform platform)
	{
		setId(platform.id);
		setName(platform.name);
		setAbbreviation(platform.abbreviation);
	}

	public Platform(Parcel parcel)
	{
		_id = parcel.readInt();
		_name = parcel.readString();
		_abbreviation = parcel.readString();
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		_id = id;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public String getAbbreviation()
	{
		return _abbreviation;
	}

	public void setAbbreviation(String abbreviation)
	{
		_abbreviation = abbreviation;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(_id);
		dest.writeString(_name);
		dest.writeString(_abbreviation);
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
