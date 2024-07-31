package com.vgsoftware.android.realtime.dataabstraction;

import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.parse.Site;
import com.vgsoftware.android.realtime.setup.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database
{
	private static final String STATION_TABLE_NAME = "Station";
	private static final String STATION_ID_COLUMN_NAME = "Id";
	private static final String STATION_SITE_ID_COLUMN_NAME = "SiteId";
	private static final String STATION_TRANSPORTATION_TYPE_ID_COLUMN_NAME = "TransportationTypeId";
	private static final String STATION_NAME_COLUMN_NAME = "Name";
	private static final String STATION_AREA_NAME_COLUMN_NAME = "AreaName";
	private static final String STATION_ALIAS_COLUMN_NAME = "Alias";
	private static final String STATION_LATITUDE_COLUMN_NAME = "Latitude";
	private static final String STATION_LONGITUDE_COLUMN_NAME = "Longitude";

	private static final String[] STATION_COLUMNS = { STATION_ID_COLUMN_NAME, STATION_SITE_ID_COLUMN_NAME, STATION_TRANSPORTATION_TYPE_ID_COLUMN_NAME, STATION_NAME_COLUMN_NAME, STATION_AREA_NAME_COLUMN_NAME, STATION_ALIAS_COLUMN_NAME, STATION_LATITUDE_COLUMN_NAME, STATION_LONGITUDE_COLUMN_NAME, };

	private static final String TRANSPORTATION_TYPE_TABLE_NAME = "TransportationType";
	private static final String TRANSPORTATION_TYPE_ID_COLUMN_NAME = "Id";
	private static final String TRANSPORTATION_TYPE_NAME_COLUMN_NAME = "Name";

	private static final String[] TRANSPORTATION_TYPE_COLUMNS = { TRANSPORTATION_TYPE_ID_COLUMN_NAME, TRANSPORTATION_TYPE_NAME_COLUMN_NAME, };

	private static final String SAVED_TABLE_NAME = "Saved";
	private static final String SAVED_ID_COLUMN_NAME = "Id";
	private static final String SAVED_STATION_ID_COLUMN_NAME = "StationId";
	private static final String SAVED_TRANSPORTATION_TYPE_ID_COLUMN_NAME = "TransportationTypeId";

	private static final String[] SAVED_COLUMNS = { SAVED_ID_COLUMN_NAME, SAVED_STATION_ID_COLUMN_NAME, SAVED_TRANSPORTATION_TYPE_ID_COLUMN_NAME, };

	private static final String DONATION_TABLE_NAME = "Donation";
	private static final String DONATION_ID_COLUMN_NAME = "Id";
	private static final String DONATION_PRODUCT_ID_COLUMN_NAME = "ProductId";
	private static final String DONATION_STATUS_COLUMN_NAME = "Status";

	private static final String[] DONATION_COLUMNS = { DONATION_ID_COLUMN_NAME, DONATION_PRODUCT_ID_COLUMN_NAME, DONATION_STATUS_COLUMN_NAME, };

	private SQLiteDatabase _db = null;
	private DatabaseHelper _databaseHelper = null;
	private static Database _instance = null;
	private static LogManager _log = null;

	private Database()
	{
	}

	public void init(Context context)
	{
		_log = new LogManager(context);
		_log.debug("init database and open writable database");
		_databaseHelper = new DatabaseHelper(context);
		_db = _databaseHelper.getWritableDatabase();
	}

	public synchronized static Database getInstance()
	{
		if (_instance == null)
		{
			_instance = new Database();
		}
		return _instance;
	}

	public void close()
	{
		_log.debug("Close database");
		_db.close();
	}

	public void saveFavorite(Favorite favorite)
	{
		_log.debug("Save favorite");
		ContentValues values = new ContentValues();
		if (favorite.getId() > 0)
		{
			values.put(Database.SAVED_ID_COLUMN_NAME, favorite.getId());
		}
		values.put(Database.SAVED_STATION_ID_COLUMN_NAME, favorite.getStationId());
		values.put(Database.SAVED_TRANSPORTATION_TYPE_ID_COLUMN_NAME, favorite.getTransportationTypeId());

		_db.replace(Database.SAVED_TABLE_NAME, null, values);
	}

	public synchronized Favorite loadFavorite(int id)
	{
		_log.debug("loadFavorite: " + id);
		Cursor cursor = _db.query(Database.SAVED_TABLE_NAME, Database.SAVED_COLUMNS, "Id=?", new String[] { Integer.toString(id) }, null, null, null);
		try
		{
			if (cursor.moveToFirst())
			{
				return new Favorite(cursor);
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	public synchronized List<Favorite> listFavorites()
	{
		_log.debug("listFavorites");
		List<Favorite> favorites = new ArrayList<Favorite>();
		Cursor cursor = _db.query(Database.SAVED_TABLE_NAME, Database.SAVED_COLUMNS, null, null, null, null, null);
		try
		{
			while (cursor.moveToNext())
			{
				favorites.add(new Favorite(cursor));
			}
			_log.debug(favorites.size() + " favorites found.");
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return favorites;
	}

	public synchronized void deleteFavorite(int id)
	{
		_log.debug("Delete favorite: " + id);
		_db.delete(Database.SAVED_TABLE_NAME, "id=?", new String[] { Integer.toString(id) });
	}

	public synchronized void saveDonation(Donation donation)
	{
		_log.debug("Save donation");
		ContentValues values = new ContentValues();
		if (donation.getId() > 0)
		{
			values.put(Database.SAVED_ID_COLUMN_NAME, donation.getId());
		}
		values.put(Database.DONATION_PRODUCT_ID_COLUMN_NAME, donation.getProductId());
		values.put(Database.DONATION_STATUS_COLUMN_NAME, donation.getStatus());

		_db.replace(Database.DONATION_TABLE_NAME, null, values);
	}

	public synchronized Donation loadDonation(String productId)
	{
		_log.debug("Load donation");
		Cursor cursor = _db.query(Database.DONATION_TABLE_NAME, Database.DONATION_COLUMNS, "productId = ?", new String[] { productId }, null, null, null);
		try
		{
			if (cursor.moveToFirst())
			{
				return new Donation(cursor);
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	public synchronized List<Donation> listDonation()
	{
		_log.debug("List donations");
		List<Donation> donations = new ArrayList<Donation>();
		Cursor cursor = _db.query(Database.DONATION_TABLE_NAME, Database.DONATION_COLUMNS, null, null, null, null, null);
		try
		{
			while (cursor.moveToNext())
			{
				donations.add(new Donation(cursor));
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return donations;
	}

	public synchronized void deleteDonation(int id)
	{
		_log.debug("Delete donation");
		_db.delete(Database.DONATION_TABLE_NAME, "id=?", new String[] { Integer.toString(id) });
	}

	public synchronized Station loadStation(int id)
	{
		_log.debug("Load station: " + id);
		Cursor cursor = _db.query(Database.STATION_TABLE_NAME, Database.STATION_COLUMNS, "id = ?", new String[] { Integer.toString(id) }, null, null, null);
		try
		{
			if (cursor.moveToFirst())
			{
				return new Station(cursor);
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	public synchronized List<Station> listStations(int typeId)
	{
		_log.debug("List stations by type: " + typeId);
		List<Station> stations = new ArrayList<Station>();
		Cursor cursor = _db.query(Database.STATION_TABLE_NAME, Database.STATION_COLUMNS, "transportationTypeId = ?", new String[] { Integer.toString(typeId) }, null, null, null);
		try
		{
			while (cursor.moveToNext())
			{
				stations.add(new Station(cursor));
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return stations;
	}

	public synchronized List<Station> listStations()
	{
		_log.debug("List stations");
		List<Station> stations = new ArrayList<Station>();
		Cursor cursor = _db.query(Database.STATION_TABLE_NAME, Database.STATION_COLUMNS, null, null, null, null, null);
		try
		{
			while (cursor.moveToNext())
			{
				stations.add(new Station(cursor));
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return stations;
	}

	public synchronized Station loadStationBySiteId(int siteId, int typeId)
	{
		_log.debug("Load stationBySiteId siteId: " + siteId + ", type: " + typeId);
		Cursor cursor = _db.query(Database.STATION_TABLE_NAME, Database.STATION_COLUMNS, "siteId = ? AND TransportationTypeId = ?", new String[] { Integer.toString(siteId), Integer.toString(typeId) }, null, null, null);
		try
		{
			if (cursor.moveToFirst())
			{
				return new Station(cursor);
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	public synchronized Station loadStation(int stationId, int typeId)
	{
		_log.debug("Load station: " + stationId + ", type: " + typeId);
		Cursor cursor = _db.query(Database.STATION_TABLE_NAME, Database.STATION_COLUMNS, "id = ? AND TransportationTypeId = ?", new String[] { Integer.toString(stationId), Integer.toString(typeId) }, null, null, null);
		try
		{
			if (cursor.moveToFirst())
			{
				return new Station(cursor);
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	public synchronized void saveSiteAsStation(Site site, int typeId)
	{
		_log.debug("Save site as station");
		ContentValues values = new ContentValues();

		values.put(Database.STATION_SITE_ID_COLUMN_NAME, site.getSiteId());
		values.put(Database.STATION_TRANSPORTATION_TYPE_ID_COLUMN_NAME, typeId);
		values.put(Database.STATION_NAME_COLUMN_NAME, site.getName());
		values.put(Database.STATION_AREA_NAME_COLUMN_NAME, site.getAreaName());

		_db.replace(Database.STATION_TABLE_NAME, null, values);
	}

	public synchronized List<Station> listStations(double latitude, double longitude, double distance)
	{
		_log.debug("List stations");
		ArrayList<Station> list = new ArrayList<Station>();
		for (Station station : listStations())
		{
			if (station.getDistance(latitude, longitude) < distance)
			{
				list.add(station);
			}
		}
		return list;
	}

	public synchronized Station loadStation(int typeId, String stationName)
	{
		_log.debug("loadStation: " + typeId + ", " + stationName);
		Cursor cursor = _db.query(Database.STATION_TABLE_NAME, Database.STATION_COLUMNS, Database.STATION_TRANSPORTATION_TYPE_ID_COLUMN_NAME + " = ? AND " + Database.STATION_NAME_COLUMN_NAME + " = ?", new String[] { Integer.toString(typeId), stationName }, null, null, null);
		try
		{
			if (cursor.moveToNext())
			{
				return new Station(cursor);
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	public synchronized List<Station> loadStation(String stationName)
	{
		_log.debug("loadStation: " + stationName);
		List<Station> stations = new ArrayList<Station>();
		Cursor cursor = _db.query(Database.STATION_TABLE_NAME, Database.STATION_COLUMNS, Database.STATION_TRANSPORTATION_TYPE_ID_COLUMN_NAME + "<> 5 AND " + Database.STATION_NAME_COLUMN_NAME + " = ?", new String[] { stationName }, null, null, null);
		try
		{
			while (cursor.moveToNext())
			{
				stations.add(new Station(cursor));
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return stations;
	}

	public synchronized TransportationType loadTransportationType(int id)
	{
		_log.debug("Load transportation type: " + id);
		Cursor cursor = _db.query(Database.TRANSPORTATION_TYPE_TABLE_NAME, Database.TRANSPORTATION_TYPE_COLUMNS, "id = ?", new String[] { Integer.toString(id) }, null, null, null);
		try
		{
			if (cursor.moveToNext())
			{
				return new TransportationType(cursor);
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	public synchronized List<TransportationType> listTransportationTypes()
	{
		_log.debug("listTransportationTypes");
		List<TransportationType> transportationTypes = new ArrayList<TransportationType>();
		Cursor cursor = _db.query(Database.TRANSPORTATION_TYPE_TABLE_NAME, Database.TRANSPORTATION_TYPE_COLUMNS, null, null, null, null, null);
		try
		{
			while (cursor.moveToNext())
			{
				transportationTypes.add(new TransportationType(cursor));
			}
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return transportationTypes;
	}
}
