package com.vgsoftware.android.realtime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "realtime.db";
	public static final int DATABASE_VERSION = 1;

	public static final String[] TABLES = { "favorites" };

	public static final String[] CREATE_TABLE_SQL =
	{
		"CREATE TABLE favorites"+
		"("+
			"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"transportationTypeId INTEGER,"+
			"stationId INTEGER"+
		")"
	};

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// create the tables
		int length = CREATE_TABLE_SQL.length;
		for (int i = 0; i < length; i++)
		{
			db.execSQL(CREATE_TABLE_SQL[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		for (String tableName : TABLES)
		{
			db.execSQL("DROP TABLE IF EXISTS " + tableName);
		}
		onCreate(db);
	}
}
