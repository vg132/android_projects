package com.vgsoftware.android.realtime.setup;

import android.database.sqlite.SQLiteDatabase;

public class DBSetup10 extends BaseDBSetup
{
	protected static String[] newData =
	{
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Johannesfred', 3612, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Norra Ulvsunda', 3614, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Karlsbodavägen', 3685, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bällsta bro', 3680, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sundbybergs centrum', 9325, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Solna Business Park', 5119, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Solna centrum', 9305, 0, 0, 4,'','');",
	};

	public static void upgrade(SQLiteDatabase db)
	{
		try
		{
			insertData(db, newData);
		}
		catch (Exception ex)
		{
			DBSetup9.create(db);
		}
	}

	public static void create(SQLiteDatabase db)
	{
		try
		{
			db.execSQL(dropDonationTable);
			db.execSQL(dropSavedTable);
			db.execSQL(dropStationTable);
			db.execSQL(dropTransportationTypeTable);

			db.execSQL(createDonationTable);
			db.execSQL(createTransportationTypeTable);
			db.execSQL(createStationTable);
			db.execSQL(createSavedTable);

			insertData(db, transportationTypeData);
			insertData(db, stationData);
		}
		catch (Exception ex)
		{
		}
	}

}
