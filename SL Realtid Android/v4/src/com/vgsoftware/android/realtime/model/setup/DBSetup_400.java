package com.vgsoftware.android.realtime.model.setup;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.model.DatabaseHelper;
import com.vgsoftware.android.realtime.model.Donation;
import com.vgsoftware.android.realtime.model.Favorite;
import com.vgsoftware.android.realtime.model.Station;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBSetup_400
{
	protected static String dropTransportationTypeTable = "DROP TABLE IF EXISTS TransportationType;";
	protected static String dropSavedTable = "DROP TABLE IF EXISTS Saved;";
	protected static String dropDonationTable = "DROP TABLE IF EXISTS Donation;";
	protected static String dropStationTable = "DROP TABLE IF EXISTS Station;";

	public static void create(DatabaseHelper helper, SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTableIfNotExists(connectionSource, Donation.class);
			TableUtils.createTableIfNotExists(connectionSource, Station.class);
			TableUtils.createTableIfNotExists(connectionSource, Favorite.class);

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
			db.execSQL(dropTransportationTypeTable);

			TableUtils.createTableIfNotExists(connectionSource, Donation.class);
			TableUtils.createTableIfNotExists(connectionSource, Station.class);
			TableUtils.createTableIfNotExists(connectionSource, Favorite.class);

			DatabaseSetup.createStations(helper);

			Cursor cursor = db.rawQuery("SELECT StationId FROM Saved", null);
			while (cursor.moveToNext())
			{
				Cursor stationCursor = db.rawQuery("SELECT Name, AreaName, SiteId FROM Station WHERE Id = " + cursor.getInt(0), null);
				if (stationCursor.moveToFirst())
				{
					Favorite favorite = new Favorite();
					favorite.setName(stationCursor.getString(0));
					favorite.setArea(stationCursor.getString(1));
					favorite.setSiteId(stationCursor.getInt(2));
					helper.getFavoriteDao().create(favorite);
				}
			}

			db.execSQL(dropStationTable);
			db.execSQL(dropSavedTable);
		}
		catch (Exception ex)
		{
			LogManager.error("Unable to upgrade database", ex);
			DBSetup_400.create(helper, db, connectionSource);
		}
	}
}
