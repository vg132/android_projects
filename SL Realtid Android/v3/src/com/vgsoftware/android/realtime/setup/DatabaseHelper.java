package com.vgsoftware.android.realtime.setup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "realtime.db";
	public static final int DATABASE_VERSION = 10;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		DBSetup9.create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (oldVersion == 3)
		{
			DBSetup6.upgrade(db);
			DBSetup7.upgrade(db);
			DBSetup8.upgrade(db);
			DBSetup9.upgrade(db);
			DBSetup10.upgrade(db);
		}
		else if (oldVersion == 6)
		{
			DBSetup7.upgrade(db);
			DBSetup8.upgrade(db);
			DBSetup9.upgrade(db);
			DBSetup10.upgrade(db);
		}
		else if (oldVersion == 7)
		{
			DBSetup8.upgrade(db);
			DBSetup9.upgrade(db);
			DBSetup10.upgrade(db);
		}
		else if (oldVersion == 8)
		{
			DBSetup9.upgrade(db);
			DBSetup10.upgrade(db);
		}
		else if(oldVersion==9)
		{
			DBSetup10.upgrade(db);
		}
		else
		{
			DBSetup10.create(db);
		}
	}
}
