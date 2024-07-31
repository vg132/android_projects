package com.vgsoftware.android.realtime.setup;

import android.database.sqlite.SQLiteDatabase;

public class DBSetup9 extends BaseDBSetup
{
	protected static String[] newData =
	{
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Uppsala centralstation', 6086, 59.858577, 17.646167, 1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Knivsta', 6091, 59.725709, 17.786736, 1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Arlanda C', 9511, 59.649758, 17.929194, 1,'','');"
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
