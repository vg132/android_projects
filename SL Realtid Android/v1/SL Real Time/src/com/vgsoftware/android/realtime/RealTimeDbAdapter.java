package com.vgsoftware.android.realtime;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RealTimeDbAdapter
{
	private static final String LOG_TAG="SL Real Time - RealTimeDbAdapter";
	
	private SQLiteDatabase _db;
	private Context _context;
	private DatabaseHelper _databaseHelper;
	
	public RealTimeDbAdapter(Context context)
	{
		_context=context;
	}
	
	private RealTimeDbAdapter open()
	throws SQLException
	{
		_databaseHelper=new DatabaseHelper(_context);
		_db=_databaseHelper.getWritableDatabase();
		return this;
	}
	
	private void close()
	{
		try
		{
			_databaseHelper.close();
		}
		catch(Exception ex)
		{
		}
	}

	public ArrayList<Favorite> getFavorites()
	{
		ArrayList<Favorite> list=new ArrayList<Favorite>();
		try
		{
			this.open();
			Cursor cursor=_db.rawQuery("SELECT id, transportationTypeId, stationId FROM favorites",null);
			if(cursor.moveToFirst())
			{
				do
				{
					list.add(new Favorite(cursor.getInt(0),cursor.getInt(2),cursor.getInt(1)));
				}while(cursor.moveToNext());
			}
		}
		catch(Exception ex)
		{
			Log.e(RealTimeDbAdapter.LOG_TAG,"Unable to get favorites",ex);
		}
		finally
		{
			this.close();
		}
		return list;
	}
	
	public void deleteFavorite(int id)
	{
		if(id>=0)
		{
			try
			{
				this.open();
				_db.execSQL("DELETE FROM favorites WHERE Id="+id+";");
			}
			catch(Exception ex)
			{
				Log.e(RealTimeDbAdapter.LOG_TAG,"Unable to delete favorite",ex);
			}
			finally
			{
				this.close();
			}
		}
	}
	
	public Favorite loadFavorite(int id)
	{
		if(id>=0)
		{
			try
			{
				this.open();
				Cursor cursor=_db.rawQuery("SELECT id, transportationTypeId, stationId FROM favorites WHERE id="+id,null);
				if (cursor.moveToFirst())
				{
					return new Favorite(cursor.getInt(0),cursor.getInt(2), cursor.getInt(1));
				}
			}
			catch(Exception ex)
			{
				Log.e(RealTimeDbAdapter.LOG_TAG,"Unable to load favorite",ex);
			}
			finally
			{
				this.close();
			}
		}
		return null;
	}
	
	public void saveFavorite(Favorite station)
	{
		if(station!=null)
		{
			try
			{
				this.open();
				_db.execSQL("INSERT INTO favorites(transportationTypeId,stationId) VALUES("+station.getTransportationTypeId()+","+station.getStationId()+");");
			}
			catch(Exception ex)
			{
				Log.e(RealTimeDbAdapter.LOG_TAG,"Unable to save favorite",ex);
			}
			finally
			{
				this.close();
			}
		}
	}
}
