package com.vgsoftware.android.feeddroid.dataabstraction;

import com.vgsoftware.android.feeddroid.setup.DBSetup1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "feeddroid22.db";
	public static final int DATABASE_VERSION = 1;

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
		onCreate(db);
	}
}
