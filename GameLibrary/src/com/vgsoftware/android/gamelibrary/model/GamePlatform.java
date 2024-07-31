package com.vgsoftware.android.gamelibrary.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "GamePlatform")
public class GamePlatform
{
	public final static String FIELD_NAME_ID = "id";
	public final static String FIELD_NAME_GAME_ID = "gameId";
	public final static String FIELD_NAME_PLATFORM_ID = "platformId";

	@DatabaseField(id = true, useGetSet = true, columnName = GamePlatform.FIELD_NAME_ID)
	private int id;
	@DatabaseField(foreign = true, columnName = GamePlatform.FIELD_NAME_GAME_ID)
	private Game _game;
	@DatabaseField(foreign = true, columnName = GamePlatform.FIELD_NAME_PLATFORM_ID)
	private Platform _platform;

	public GamePlatform()
	{
	}

	public GamePlatform(Game game, Platform platform)
	{
		_game = game;
		_platform = platform;
	}

	public int getId()
	{
		if (_game != null && _platform != null)
		{
			return (_game.getId() + "-" + _platform.getId()).hashCode();
		}
		return 0;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}
