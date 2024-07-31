package com.vgsoftware.android.realtime.search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.Settings;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.ISite;
import com.vgsoftware.android.realtime.model.Site;
import com.vgsoftware.android.realtime.model.SiteSetting;
import com.vgsoftware.android.realtime.model.Station;
import com.vgsoftware.android.realtime.parse.SiteParser;
import com.vgsoftware.android.vglib.StringUtility;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class StationContentProvider extends ContentProvider
{
	private SiteParser _siteParser = null;
	private Settings _settings = null;
	private DataStore _dataStore = null;

	public static String AUTHORITY = "com.vgsoftware.android.realtime.search.StationContentProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/search");

	// MIME types used for searching words or looking up a single definition
	public static final String STATIONS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.vgsoftware.android.realtime";
	public static final String STATION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.vgsoftware.android.realtime";

	// UriMatcher stuff
	private static final int SEARCH_STATIONS = 0;
	private static final int GET_STATION = 1;
	private static final int SEARCH_SUGGEST = 2;
	private static final UriMatcher sURIMatcher = buildUriMatcher();

	public StationContentProvider()
	{
	}

	public static Uri BuildViewUri(ISite site)
	{
		return Uri.parse(CONTENT_URI + "/" + site.getSiteId() + "|" + Uri.encode(site.getName()) + "|" + Uri.encode(site.getArea()));
	}

	private static UriMatcher buildUriMatcher()
	{
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		// to get definitions...
		matcher.addURI(AUTHORITY, "search", SEARCH_STATIONS);
		matcher.addURI(AUTHORITY, "search/*", GET_STATION);
		// to get suggestions...
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

		return matcher;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (sURIMatcher.match(uri))
		{
			case SEARCH_STATIONS:
				return STATIONS_MIME_TYPE;
			case GET_STATION:
				return STATION_MIME_TYPE;
			case SEARCH_SUGGEST:
				return SearchManager.SUGGEST_MIME_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URL " + uri);
		}
	}

	@Override
	public boolean onCreate()
	{
		_siteParser = new SiteParser();
		_settings = new Settings(getContext());
		_dataStore = new DataStore(getContext());

		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		switch (sURIMatcher.match(uri))
		{
			case SEARCH_SUGGEST:
				if (selectionArgs == null)
				{
					throw new IllegalArgumentException("selectionArgs must be provided for the Uri: " + uri);
				}
				return getSuggestions(selectionArgs[0]);
			case SEARCH_STATIONS:
				if (selectionArgs == null)
				{
					throw new IllegalArgumentException("selectionArgs must be provided for the Uri: " + uri);
				}
				return search(selectionArgs[0]);
			case GET_STATION:
				return getStation(uri);
			default:
				throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}

	private Cursor search(String query)
	{
		MatrixCursor cursor = new MatrixCursor(new String[] {
				BaseColumns._ID,
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_TEXT_2,
				SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
		});
		for (ISite site : filterResult(querySites(query)))
		{
			String id = site.getSiteId() + "|" + site.getName() + "|" + site.getArea();
			cursor.addRow(new Object[] { site.getSiteId(), site.getName(), site.getArea(), id });
		}
		return cursor;
	}

	private Cursor getStation(Uri uri)
	{
		MatrixCursor cursor = new MatrixCursor(new String[] {
				BaseColumns._ID,
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_TEXT_2
		});
		String[] parts = uri.getLastPathSegment().split("\\|");

		if (parts.length > 0)
		{
			Object[] rowData = new Object[3];
			rowData[0] = parts[0];
			if (parts.length > 1)
			{
				rowData[1] = parts[1];
			}
			if (parts.length > 2)
			{
				rowData[2] = parts[2];
			}
			cursor.addRow(rowData);
		}
		return cursor;
	}

	private Cursor getSuggestions(final String query)
	{
		final MatrixCursor cursor = new MatrixCursor(new String[] { BaseColumns._ID,
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_TEXT_2,
				SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID });

		for (ISite site : filterResult(querySites(query)))
		{
			cursor.addRow(new Object[] { site.getSiteId(),
					site.getName(),
					site.getArea(),
					site.getSiteId() + "|" + site.getName() + "|" + site.getArea()
			});
		}
		return cursor;
	}

	private List<? extends ISite> querySites(final String query)
	{
		final String searchQuery = query.trim();
		if (StringUtility.isNullOrEmpty(searchQuery) || searchQuery.length() < 2)
		{
			int counter = 0;
			List<Site> sites = new ArrayList<Site>();
			for (SiteSetting siteSetting : _dataStore.listSiteSettingByLastSearch())
			{
				Site site = _dataStore.loadSite(siteSetting.getSiteId());
				if (site != null)
				{
					sites.add(site);
				}
				counter++;
				if (counter >= 10)
				{
					break;
				}
			}
			return sites;
		}
		List<? extends ISite> sites = _dataStore.listSiteByQuery(searchQuery);
		if (sites == null || sites.size() == 0)
		{
			sites = _siteParser.siteSearch(searchQuery);
			final List<? extends ISite> tempSites = sites;
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					_dataStore.deleteSitesByQuery(searchQuery);
					for (ISite site : tempSites)
					{
						Site newSite = new Site(site);
						newSite.setValidTo(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
						newSite.setQuery(searchQuery);
						_dataStore.saveSite(newSite);
					}
				}
			}).start();
		}
		else
		{
			LogManager.debug("Sites found in database. Query: %s", searchQuery);
		}
		return sites;
	}

	private List<? extends ISite> filterResult(final List<? extends ISite> sites)
	{
		if (_settings != null &&
				(!_settings.getShowBus() || !_settings.getShowSubway() || !_settings.getShowTrain() || !_settings.getShowTram()))
		{
			List<ISite> filteredSites = new ArrayList<ISite>();
			for (ISite site : sites)
			{
				if (includeSite(site))
				{
					filteredSites.add(site);
				}
			}
			return filteredSites;
		}
		return sites;
	}

	private boolean includeSite(ISite site)
	{
		Station station = _dataStore.getStation(site.getSiteId());
		if (station == null)
		{
			return _settings.getShowBus();
		}
		return (_settings.getShowTram() && station.getHasTram()) ||
				(_settings.getShowTrain() && station.getHasTrain()) ||
				(_settings.getShowSubway() && station.getHasSubway()) ||
				(_settings.getShowBus() && station.getHasBus());
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		throw new UnsupportedOperationException();
	}
}
