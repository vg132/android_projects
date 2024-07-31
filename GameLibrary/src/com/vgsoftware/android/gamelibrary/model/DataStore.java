package com.vgsoftware.android.gamelibrary.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.vgsoftware.android.gamelibrary.GameLibraryApplication;
import com.vgsoftware.android.gamelibrary.LogManager;

import android.annotation.SuppressLint;

public class DataStore
{
	private static DataStore _instance = null;
	private List<IOnDataChanged> _onDataChangedListeners = null;

	private DataStore()
	{
		_onDataChangedListeners = new ArrayList<IOnDataChanged>();
	}

	public static DataStore getInstance()
	{
		if (_instance == null)
		{
			_instance = new DataStore();
		}
		return _instance;
	}

	public void registerOnDataChangedListener(IOnDataChanged listener)
	{
		if (!_onDataChangedListeners.contains(listener))
		{
			_onDataChangedListeners.add(listener);
		}
	}

	public void unregisterOnDataChangedListener(IOnDataChanged listener)
	{
		_onDataChangedListeners.remove(listener);
	}

	private void onDataChanged()
	{
		for (IOnDataChanged listener : _onDataChangedListeners)
		{
			listener.onDataChanged();
		}
	}

	private DatabaseHelper getHelper()
	{
		return DatabaseHelper.getHelper(GameLibraryApplication.getAppContext());
	}

	public void saveGame(Game game)
	{
		if (game.getAdded() == null)
		{
			game.setAdded(new Date(System.currentTimeMillis()));
		}
		getHelper().getGameDao().createOrUpdate(game);
		getHelper().getGamePlatformDao().delete(listGamePlatformByGameId(game.getId()));
		for (Platform platform : game.getPlatforms())
		{
			getHelper().getGamePlatformDao().createOrUpdate(new GamePlatform(game, platform));
		}
		onDataChanged();
	}

	public void deleteGame(Game game)
	{
		getHelper().getGamePlatformDao().delete(listGamePlatformByGameId(game.getId()));
		getHelper().getGameDao().delete(game);
		onDataChanged();
	}

	public List<GamePlatform> listGamePlatformByGameId(int id)
	{
		return getHelper().getGamePlatformDao().queryForEq(GamePlatform.FIELD_NAME_GAME_ID, id);
	}

	public List<GamePlatform> listGamePlatformByPlatformId(int id)
	{
		return getHelper().getGamePlatformDao().queryForEq(GamePlatform.FIELD_NAME_PLATFORM_ID, id);
	}

	public List<Platform> listUsedPlatforms()
	{
		try
		{
			QueryBuilder<GamePlatform, Integer> gamePlatformQb = getHelper().getGamePlatformDao().queryBuilder();
			gamePlatformQb.selectColumns(GamePlatform.FIELD_NAME_PLATFORM_ID);
			QueryBuilder<Platform, Integer> platformQb = getHelper().getPlatformDao().queryBuilder();
			platformQb.where().in(Platform.FIELD_NAME_ID, gamePlatformQb);
			platformQb.orderBy(Platform.FIELD_NAME_NAME, true);
			return platformQb.query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Platform>();
	}

	public List<Game> listGames()
	{
		return getHelper().getGameDao().queryForAll();
	}

	public List<Game> listGames(Platform platform)
	{
		try
		{
			QueryBuilder<GamePlatform, Integer> gamePlatformQb = getHelper().getGamePlatformDao().queryBuilder();
			gamePlatformQb.selectColumns(GamePlatform.FIELD_NAME_GAME_ID);
			gamePlatformQb.where().eq(GamePlatform.FIELD_NAME_PLATFORM_ID, platform.getId());
			QueryBuilder<Game, Integer> gameQb = getHelper().getGameDao().queryBuilder();
			gameQb.where().in(Game.FIELD_NAME_ID, gamePlatformQb);
			gameQb.orderBy(Game.FIELD_NAME_TITLE, true);
			return gameQb.query();
		}
		catch (SQLException ex)
		{
			LogManager.error("Unable to list games based on platform", ex);
		}
		return new ArrayList<Game>();
	}

	public List<Genre> listGenres()
	{
		return getHelper().getGenreDao().queryForAll();
	}

	public void saveGenre(Genre genre, boolean notifyDataChanged)
	{
		getHelper().getGenreDao().createOrUpdate(genre);
		if (notifyDataChanged)
		{
			onDataChanged();
		}
	}

	@SuppressLint("UseSparseArrays")
	public List<Platform> listPlatforms()
	{
		final List<Platform> platforms = getHelper().getPlatformDao().queryForAll();
		final GenericRawResults<Object[]> result = getHelper().getGamePlatformDao()
				.queryRaw("SELECT " + GamePlatform.FIELD_NAME_PLATFORM_ID + ", COUNT(" + GamePlatform.FIELD_NAME_PLATFORM_ID + ") FROM GamePlatform GROUP BY " + GamePlatform.FIELD_NAME_PLATFORM_ID,
						new DataType[] { DataType.INTEGER, DataType.INTEGER });
		final HashMap<Integer, Integer> usedPlatforms = new HashMap<Integer, Integer>();
		for (Object[] resultArray : result)
		{
			usedPlatforms.put((Integer) resultArray[0], (Integer) resultArray[1]);
		}
		Collections.sort(platforms, new Comparator<Platform>()
		{
			@Override
			public int compare(Platform lhs, Platform rhs)
			{
				if (usedPlatforms.containsKey(lhs.getId()) && usedPlatforms.containsKey(rhs.getId()))
				{
					return usedPlatforms.get(rhs.getId()) - usedPlatforms.get(lhs.getId());
				}
				else if (usedPlatforms.containsKey(lhs.getId()))
				{
					return -1;
				}
				else if (usedPlatforms.containsKey(rhs.getId()))
				{
					return 1;
				}
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		return platforms;
	}

	public void savePlatform(Platform platform, boolean notifyDataChanged)
	{
		getHelper().getPlatformDao().createOrUpdate(platform);
		if (notifyDataChanged)
		{
			onDataChanged();
		}
	}

	public Platform loadPlatform(String name)
	{
		List<Platform> platforms = getHelper().getPlatformDao().queryForEq("name", name);
		if (platforms != null && platforms.size() > 0)
		{
			return platforms.get(0);
		}
		return null;
	}
}
