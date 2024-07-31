package com.vgsoftware.android.gamelibrary.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.vgsoftware.android.gamelibrary.R;
import com.vgsoftware.android.gamelibrary.model.setup.DBSetup_100;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	public static final String DATABASE_NAME = "gamelibrary.db";
	public static final int DATABASE_VERSION = 100;

	private RuntimeExceptionDao<Game, Integer> _gameDao = null;
	private RuntimeExceptionDao<Genre, Integer> _genreDao = null;
	private RuntimeExceptionDao<Platform, Integer> _platformDao = null;
	private RuntimeExceptionDao<GamePlatform, Integer> _gamePlatformDao = null;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	public static DatabaseHelper getHelper(Context context)
	{
		return OpenHelperManager.getHelper(context, DatabaseHelper.class);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
	{
		DBSetup_100.create(this, database, connectionSource);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		DBSetup_100.create(this, database, connectionSource);
	}

	public RuntimeExceptionDao<Game, Integer> getGameDao()
	{
		if (_gameDao == null)
		{
			_gameDao = getRuntimeExceptionDao(Game.class);
		}
		return _gameDao;
	}

	public RuntimeExceptionDao<Genre, Integer> getGenreDao()
	{
		if (_genreDao == null)
		{
			_genreDao = getRuntimeExceptionDao(Genre.class);
		}
		return _genreDao;
	}

	public RuntimeExceptionDao<Platform, Integer> getPlatformDao()
	{
		if (_platformDao == null)
		{
			_platformDao = getRuntimeExceptionDao(Platform.class);
		}
		return _platformDao;
	}

	public RuntimeExceptionDao<GamePlatform, Integer> getGamePlatformDao()
	{
		if (_gamePlatformDao == null)
		{
			_gamePlatformDao = getRuntimeExceptionDao(GamePlatform.class);
		}
		return _gamePlatformDao;
	}

	@Override
	public void close()
	{
		super.close();
		_gameDao = null;
		_genreDao = null;
		_platformDao = null;
		_gamePlatformDao = null;
	}
}
