package com.vgsoftware.android.polisen.dataabstraction;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vgsoftware.android.polisen.Log;
import com.vgsoftware.android.polisen.Preferences;
import com.vgsoftware.android.polisen.setup.DBSetup8;
import com.vgsoftware.android.polisen.ui.R;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	public static final String DATABASE_NAME = "polisen.db";
	public static final int DATABASE_VERSION = 8;

	private RuntimeExceptionDao<Region, Integer> _regionDao = null;
	private RuntimeExceptionDao<Feed, Integer> _feedDao = null;
	private RuntimeExceptionDao<FeedItem, Integer> _feedItemDao = null;
	private Context _context = null;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
		_context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTableIfNotExists(connectionSource, Region.class);
			TableUtils.createTableIfNotExists(connectionSource, Feed.class);
			TableUtils.createTableIfNotExists(connectionSource, FeedItem.class);

			DBSetup8.createRegions(this);
			DBSetup8.createFeeds(this);
		}
		catch (SQLException ex)
		{
			Log.error("Unable to create database", ex);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		if (oldVersion < 5)
		{
			database.execSQL("DROP TABLE IF EXISTS Feed");
			database.execSQL("DROP TABLE IF EXISTS Region");
			database.execSQL("DROP TABLE IF EXISTS Category");
			database.execSQL("DROP TABLE IF EXISTS FeedItem");
		}
		database.execSQL("DROP TABLE IF EXISTS Feed");
		onCreate(database, connectionSource);
	}

	public RuntimeExceptionDao<Region, Integer> getRegionDao()
	{
		if (_regionDao == null)
		{
			_regionDao = getRuntimeExceptionDao(Region.class);
		}
		return _regionDao;
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

	public static DatabaseHelper getHelper(Context context)
	{
		return OpenHelperManager.getHelper(context, DatabaseHelper.class);
	}

	public List<Feed> getActiveFeeds()
	{
		List<Feed> feeds = new ArrayList<Feed>();
		for (Feed feed : getFeedDao().queryForAll())
		{
			if (feed.isActive() || Preferences.getInstance(_context).getAllFeeds())
			{
				feeds.add(feed);
			}
		}
		return feeds;
	}

	@Override
	public void close()
	{
		super.close();
		_regionDao = null;
		_feedDao = null;
		_feedItemDao = null;
	}
}
