package com.vgsoftware.android.ordjaktenhelper.data;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "ordfinnaren.db";
	public static final int DATABASE_VERSION = 1;
	private static Context _context = null;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		_context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		DBSetup1.Create(db, _context);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		DBSetup1.Create(db, _context);
	}

	public boolean doesDatabaseExist()
	{
		File dbFile = _context.getDatabasePath(DATABASE_NAME);
		return dbFile.exists();
	}
}
