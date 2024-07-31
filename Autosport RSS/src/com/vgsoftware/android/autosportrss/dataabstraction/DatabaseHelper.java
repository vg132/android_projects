package com.vgsoftware.android.autosportrss.dataabstraction;

import com.vgsoftware.android.autosportrss.setup.DBSetup1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "autosport.db";
	public static final int DATABASE_VERSION = 3;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		DBSetup1.Create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if(oldVersion==1)
		{
			DBSetup1.Upgrade(db);
		}
		else if(oldVersion==2)
		{
			DBSetup1.UpgradeFrom2(db);
		}
		else
		{
			onCreate(db);
		}
	}
}
