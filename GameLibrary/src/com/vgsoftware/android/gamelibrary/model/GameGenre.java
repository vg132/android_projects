package com.vgsoftware.android.gamelibrary.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "GameGenre")
public class GameGenre
{
	public static final String FIELD_NAME_ID = "id";
	public static final String FIELD_NAME_GAME_ID = "gameId";
	public static final String FIELD_NAME_GENRE_ID = "genreId";

	@DatabaseField(id = true, useGetSet = true, columnName = GameGenre.FIELD_NAME_ID)
	private int id;
	@DatabaseField(foreign = true, columnName = GameGenre.FIELD_NAME_GAME_ID)
	private Game _game;
	@DatabaseField(foreign = true, columnName = GameGenre.FIELD_NAME_GENRE_ID)
	private Genre _genre;

	public GameGenre()
	{
	}

	public GameGenre(Game game, Genre genre)
	{
		_game = game;
		_genre = genre;
	}

	public int getId()
	{
		if (_game != null && _genre != null)
		{
			return (_game.getId() + "-" + _genre.getId()).hashCode();
		}
		return 0;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}
