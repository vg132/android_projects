package com.vgsoftware.android.gamelibrary.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;

@DatabaseTable(tableName = "Game")
public class Game implements Parcelable
{
	public final static String FIELD_NAME_ID = "id";
	public final static String FIELD_NAME_EXTERNAL_ID = "externalId";
	public final static String FIELD_NAME_TITLE = "title";
	public final static String FIELD_NAME_EAN = "ean";
	public final static String FIELD_NAME_DEVELOPER = "developer";
	public final static String FIELD_NAME_PUBLISHER = "publisher";
	public final static String FIELD_NAME_RATING = "rating";
	public final static String FIELD_NAME_COMMENT = "comment";
	public final static String FIELD_NAME_ADDED = "added";

	@DatabaseField(generatedId = true, columnName = Game.FIELD_NAME_ID)
	private int _id;
	@DatabaseField(canBeNull = true, columnName = Game.FIELD_NAME_EXTERNAL_ID)
	private String _externalId;
	@DatabaseField(canBeNull = false, columnName = Game.FIELD_NAME_TITLE)
	private String _title;
	@DatabaseField(canBeNull = true, columnName = Game.FIELD_NAME_EAN)
	private String _ean;
	@DatabaseField(canBeNull = true, columnName = Game.FIELD_NAME_DEVELOPER)
	private String _developer;
	@DatabaseField(canBeNull = true, columnName = Game.FIELD_NAME_PUBLISHER)
	private String _publisher;
	@DatabaseField(canBeNull = true, columnName = Game.FIELD_NAME_RATING)
	private int _rating;
	@DatabaseField(canBeNull = true, columnName = Game.FIELD_NAME_COMMENT)
	private String _comment;
	@DatabaseField(canBeNull = false, columnName = Game.FIELD_NAME_ADDED)
	private Date _added;
	private List<Platform> _platforms;
	private List<Genre> _genres;

	public Game()
	{
		_platforms = new ArrayList<Platform>();
		_genres = new ArrayList<Genre>();
	}

	public Game(Parcel in)
	{
		_id = in.readInt();
		_externalId = in.readString();
		_title = in.readString();
		_ean = in.readString();
		_developer = in.readString();
		_publisher = in.readString();
		_rating = in.readInt();
		_comment = in.readString();
		_added = new Date(in.readLong());
		_platforms = new ArrayList<Platform>();
		in.readList(_platforms, Platform.class.getClassLoader());
		_genres = new ArrayList<Genre>();
		in.readList(_genres, Genre.class.getClassLoader());
	}

	public int getId()
	{
		return _id;
	}

	public String getExternalId()
	{
		return _externalId;
	}

	public void setExternalId(String externalId)
	{
		this._externalId = externalId;
	}

	public String getTitle()
	{
		return _title;
	}

	public void setTitle(String title)
	{
		this._title = title;
	}

	public String getEAN()
	{
		return _ean;
	}

	public void setEAN(String ean)
	{
		this._ean = ean;
	}

	public String getDeveloper()
	{
		return _developer;
	}

	public void setDeveloper(String developer)
	{
		this._developer = developer;
	}

	public String getPublisher()
	{
		return _publisher;
	}

	public void setPublisher(String publisher)
	{
		_publisher = publisher;
	}

	public int getRating()
	{
		return _rating;
	}

	public void setRating(int score)
	{
		this._rating = score;
	}

	public String getComment()
	{
		return _comment;
	}

	public void setComment(String comment)
	{
		this._comment = comment;
	}

	public Date getAdded()
	{
		return _added;
	}

	public void setAdded(Date added)
	{
		_added = added;
	}

	public List<Platform> getPlatforms()
	{
		return _platforms;
	}

	public void setPlatforms(List<Platform> platforms)
	{
		_platforms = platforms;
	}

	public List<Genre> getGenres()
	{
		return _genres;
	}

	public void setGenres(List<Genre> genres)
	{
		_genres = genres;
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
		dest.writeString(_externalId);
		dest.writeString(_title);
		dest.writeString(_ean);
		dest.writeString(_developer);
		dest.writeString(_publisher);
		dest.writeInt(_rating);
		dest.writeString(_comment);
		dest.writeLong(_added.getTime());
		dest.writeList(_platforms);
		dest.writeList(_genres);
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
