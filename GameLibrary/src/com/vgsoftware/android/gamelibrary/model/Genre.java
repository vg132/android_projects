package com.vgsoftware.android.gamelibrary.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;

@DatabaseTable(tableName = "Genre")
public class Genre implements Parcelable
{
	public final static String FIELD_NAME_ID = "id";
	public final static String FIELD_NAME_NAME = "name";

	@DatabaseField(generatedId = false, uniqueIndex = true, id = true, columnName = Genre.FIELD_NAME_ID)
	private int _id;
	@DatabaseField(canBeNull = false, columnName = Genre.FIELD_NAME_NAME)
	private String _name;

	public Genre()
	{
	}

	public Genre(com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Genre genre)
	{
		setId(genre.id);
		setName(genre.name);
	}

	public Genre(Parcel in)
	{
		_id = in.readInt();
		_name = in.readString();
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
