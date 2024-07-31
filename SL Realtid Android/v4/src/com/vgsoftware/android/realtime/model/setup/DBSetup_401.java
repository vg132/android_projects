package com.vgsoftware.android.realtime.model.setup;

import java.sql.SQLException;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.model.DatabaseHelper;
import com.vgsoftware.android.realtime.model.DepartureSetting;
import com.vgsoftware.android.realtime.model.Donation;
import com.vgsoftware.android.realtime.model.Favorite;
import com.vgsoftware.android.realtime.model.Station;
import com.vgsoftware.android.realtime.model.TrafficStatusSetting;

public class DBSetup_401
{
	public static void create(DatabaseHelper helper, SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTableIfNotExists(connectionSource, Donation.class);
			TableUtils.createTableIfNotExists(connectionSource, Station.class);
			TableUtils.createTableIfNotExists(connectionSource, Favorite.class);
			TableUtils.createTableIfNotExists(connectionSource, TrafficStatusSetting.class);
			TableUtils.createTableIfNotExists(connectionSource, DepartureSetting.class);

			DatabaseSetup.createStations(helper);
		}
		catch (SQLException ex)
		{
			LogManager.error("Unable to create database", ex);
		}
	}

	public static void upgrade(DatabaseHelper helper, SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTableIfNotExists(connectionSource, TrafficStatusSetting.class);
			TableUtils.createTableIfNotExists(connectionSource, DepartureSetting.class);
		}
		catch (Exception ex)
		{
			LogManager.error("Unable to upgrade database", ex);
			DBSetup_401.create(helper, db, connectionSource);
		}
	}
}
