package com.vgsoftware.android.realtime.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.setup.DBSetup_400;
import com.vgsoftware.android.realtime.model.setup.DBSetup_401;
import com.vgsoftware.android.realtime.model.setup.DBSetup_402;
import com.vgsoftware.android.realtime.model.setup.DBSetup_403;
import com.vgsoftware.android.realtime.model.setup.DBSetup_404;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	public static final String DATABASE_NAME = "realtime.db";
	public static final int DATABASE_VERSION = 404;

	private RuntimeExceptionDao<Donation, Integer> _donationDao = null;
	private RuntimeExceptionDao<Station, Integer> _stationDao = null;
	private RuntimeExceptionDao<Favorite, Integer> _favoriteDao = null;
	private RuntimeExceptionDao<TrafficStatusSetting, Integer> _trafficStatusSettingDao = null;
	private RuntimeExceptionDao<DepartureSetting, Integer> _departureSettingDao = null;
	private RuntimeExceptionDao<Site, Integer> _siteDao = null;
	private RuntimeExceptionDao<SiteSetting, Integer> _siteSettingDao = null;
	private RuntimeExceptionDao<SiteFilter, Integer> _siteFilterDao = null;

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
		DBSetup_404.create(this, database, connectionSource);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		if (oldVersion < 400)
		{
			DBSetup_400.upgrade(this, database, connectionSource);
			DBSetup_401.upgrade(this, database, connectionSource);
			DBSetup_402.upgrade(this, database, connectionSource);
			DBSetup_403.upgrade(this, database, connectionSource);
			DBSetup_404.upgrade(this, database, connectionSource);
		}
		else if (oldVersion == 400)
		{
			DBSetup_401.upgrade(this, database, connectionSource);
			DBSetup_402.upgrade(this, database, connectionSource);
			DBSetup_403.upgrade(this, database, connectionSource);
			DBSetup_404.upgrade(this, database, connectionSource);
		}
		else if (oldVersion == 401)
		{
			DBSetup_402.upgrade(this, database, connectionSource);
			DBSetup_403.upgrade(this, database, connectionSource);
			DBSetup_404.upgrade(this, database, connectionSource);
		}
		else if (oldVersion == 402)
		{
			DBSetup_403.upgrade(this, database, connectionSource);
			DBSetup_404.upgrade(this, database, connectionSource);
		}
		else if (oldVersion == 403)
		{
			DBSetup_404.upgrade(this, database, connectionSource);
		}
		else
		{
			DBSetup_404.create(this, database, connectionSource);
		}
	}

	public RuntimeExceptionDao<Donation, Integer> getDonationDao()
	{
		if (_donationDao == null)
		{
			_donationDao = getRuntimeExceptionDao(Donation.class);
		}
		return _donationDao;
	}

	public RuntimeExceptionDao<Station, Integer> getStationDao()
	{
		if (_stationDao == null)
		{
			_stationDao = getRuntimeExceptionDao(Station.class);
		}
		return _stationDao;
	}

	public RuntimeExceptionDao<Favorite, Integer> getFavoriteDao()
	{
		if (_favoriteDao == null)
		{
			_favoriteDao = getRuntimeExceptionDao(Favorite.class);
		}
		return _favoriteDao;
	}

	public RuntimeExceptionDao<TrafficStatusSetting, Integer> getTrafficStatusSettingDao()
	{
		if (_trafficStatusSettingDao == null)
		{
			_trafficStatusSettingDao = getRuntimeExceptionDao(TrafficStatusSetting.class);
		}
		return _trafficStatusSettingDao;
	}

	public RuntimeExceptionDao<DepartureSetting, Integer> getDepartureSettingDao()
	{
		if (_departureSettingDao == null)
		{
			_departureSettingDao = getRuntimeExceptionDao(DepartureSetting.class);
		}
		return _departureSettingDao;
	}

	public RuntimeExceptionDao<Site, Integer> getSiteDao()
	{
		if (_siteDao == null)
		{
			_siteDao = getRuntimeExceptionDao(Site.class);
		}
		return _siteDao;
	}

	public RuntimeExceptionDao<SiteSetting, Integer> getSiteSettingDao()
	{
		if (_siteSettingDao == null)
		{
			_siteSettingDao = getRuntimeExceptionDao(SiteSetting.class);
		}
		return _siteSettingDao;
	}

	public RuntimeExceptionDao<SiteFilter, Integer> getSiteFilterDao()
	{
		if (_siteFilterDao == null)
		{
			_siteFilterDao = getRuntimeExceptionDao(SiteFilter.class);
		}
		return _siteFilterDao;
	}

	@Override
	public void close()
	{
		super.close();
		_donationDao = null;
		_stationDao = null;
		_favoriteDao = null;
		_trafficStatusSettingDao = null;
		_departureSettingDao = null;
		_siteDao = null;
		_siteSettingDao = null;
		_siteFilterDao = null;
	}
}
