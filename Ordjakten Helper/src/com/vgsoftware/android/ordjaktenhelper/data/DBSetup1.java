package com.vgsoftware.android.ordjaktenhelper.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.vgsoftware.android.ordjaktenhelper.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBSetup1
{
	private static String[] createTables =
	{
		"CREATE TABLE Words" +
			"(" +
			"Word VARCHAR(200)" +
			");"
	};

	public static void Create(SQLiteDatabase db, Context context)
	{
		try
		{
			for (String table : createTables)
			{
				db.execSQL(table);
			}
			InputStreamReader streamReader = new InputStreamReader(context.getResources().openRawResource(R.raw.data));
			BufferedReader reader = new BufferedReader(streamReader);
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				db.execSQL("INSERT INTO Words VALUES('" + line + "');");
			}
			reader.close();
			streamReader.close();
		}
		catch (Exception ex)
		{
		}
	}
}
