package com.vgsoftware.android.trafik.dataabstraction;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vgsoftware.android.trafik.Log;
import com.vgsoftware.android.trafik.setup.DBSetup;
import com.vgsoftware.android.trafik.R;

import android.content.Context;
import java.sql.SQLException;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	public static final String DATABASE_NAME = "trafiken.db";
	public static final int DATABASE_VERSION = 10;

	private RuntimeExceptionDao<Feed, Integer> _feedDao = null;
	private RuntimeExceptionDao<FeedItem, Integer> _feedItemDao = null;
	private RuntimeExceptionDao<Camera, Integer> _cameraDao = null;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTableIfNotExists(connectionSource, Feed.class);
			TableUtils.createTableIfNotExists(connectionSource, FeedItem.class);
			TableUtils.createTableIfNotExists(connectionSource, Camera.class);

			DBSetup.createFeeds(this);
			DBSetup.createCameras(this);
		}
		catch (SQLException ex)
		{
			Log.error("Unable to create database", ex);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		database.execSQL("DROP TABLE IF EXISTS Feed");
		database.execSQL("DROP TABLE IF EXISTS Region");
		database.execSQL("DROP TABLE IF EXISTS Category");
		database.execSQL("DROP TABLE IF EXISTS FeedItem");
		onCreate(database, connectionSource);
	}

	public RuntimeExceptionDao<Feed, Integer> getFeedDao()
	{
		if (_feedDao == null)
		{
			_feedDao = getRuntimeExceptionDao(Feed.class);
		}
		return _feedDao;
	}

	public RuntimeExceptionDao<FeedItem, Integer> getFeedItemDao()
	{
		if (_feedItemDao == null)
		{
			_feedItemDao = getRuntimeExceptionDao(FeedItem.class);
		}
		return _feedItemDao;
	}

	public RuntimeExceptionDao<Camera, Integer> getCameraDao()
	{
		if (_cameraDao == null)
		{
			_cameraDao = getRuntimeExceptionDao(Camera.class);
		}
		return _cameraDao;
	}

	public static DatabaseHelper getHelper(Context context)
	{
		return OpenHelperManager.getHelper(context, DatabaseHelper.class);
	}

	@Override
	public void close()
	{
		super.close();
		_feedDao = null;
		_feedItemDao = null;
		_cameraDao = null;
	}
}
