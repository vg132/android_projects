package com.vgsoftware.android.justcount.dataabstraction;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vgsoftware.android.justcount.Log;
import com.vgsoftware.android.justcount.R;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	public static final String DATABASE_NAME = "justcount.db";
	public static final int DATABASE_VERSION = 1;

	private RuntimeExceptionDao<Count, Integer> _countDao = null;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTableIfNotExists(connectionSource, Count.class);
		}
		catch (SQLException ex)
		{
			Log.error("Unable to create database", ex);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		onCreate(database, connectionSource);
	}

	public RuntimeExceptionDao<Count, Integer> getCountDao()
	{
		if (_countDao == null)
		{
			_countDao = getRuntimeExceptionDao(Count.class);
		}
		return _countDao;
	}

	@Override
	public void close()
	{
		super.close();
		_countDao = null;
	}
}
