package com.vgsoftware.android.realtime.setup;

import android.database.sqlite.SQLiteDatabase;

public class DBSetup7 extends BaseDBSetup
{
	public static void upgrade(SQLiteDatabase db)
	{
		try
		{
			db.execSQL(dropDonationTable);
			db.execSQL(createDonationTable);
			db.execSQL(dropTransportationTypeTable);
			db.execSQL(createTransportationTypeTable);
			insertData(db,transportationTypeData);
			db.execSQL(dropStationTable);
			db.execSQL(createStationTable);
			insertData(db,stationData);
		}
		catch (Exception ex)
		{
			DBSetup7.create(db);
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

			insertData(db,transportationTypeData);
			insertData(db,stationData);
		}
		catch (Exception ex)
		{
		}
	}
}
