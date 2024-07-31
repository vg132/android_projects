package com.vgsoftware.android.gamelibrary.model.setup;

import java.sql.SQLException;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vgsoftware.android.gamelibrary.LogManager;
import com.vgsoftware.android.gamelibrary.model.DatabaseHelper;
import com.vgsoftware.android.gamelibrary.model.Game;
import com.vgsoftware.android.gamelibrary.model.GamePlatform;
import com.vgsoftware.android.gamelibrary.model.Genre;
import com.vgsoftware.android.gamelibrary.model.Platform;

public class DBSetup_100
{
	public static void create(DatabaseHelper helper, SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTableIfNotExists(connectionSource, Genre.class);
			TableUtils.createTableIfNotExists(connectionSource, Platform.class);
			TableUtils.createTableIfNotExists(connectionSource, Game.class);
			TableUtils.createTableIfNotExists(connectionSource, GamePlatform.class);
		}
		catch (SQLException ex)
		{
			LogManager.error("Unable to create database", ex);
		}
	}
}
