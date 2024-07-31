package com.vgsoftware.android.realtime.setup;

import android.database.sqlite.SQLiteDatabase;

public class DBSetup6 extends BaseDBSetup
{
	private static String updateSavedTable=	"ALTER TABLE Saved ADD TransportationTypeId INT;";
	
	private static String updateSavedData="UPDATE Saved SET TransportationTypeId=(SELECT Station.TransportationTypeId FROM Station WHERE Station.Id=Saved.StationId);";
	
	public static void upgrade(SQLiteDatabase db)
	{
		try
		{
			db.execSQL(updateSavedTable);
			db.execSQL(updateSavedData);
		}
		catch(Exception ex)
		{
			DBSetup7.create(db);
		}
	}
}
