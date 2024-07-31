package com.vgsoftware.android.realtime.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.vgsoftware.android.realtime.Constants;
import com.vgsoftware.android.realtime.LogManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

public class DataStore
{
	private Context _context = null;
	private List<IOnDataChanged> _onDataChangedListeners = new ArrayList<IOnDataChanged>();

	public DataStore(Context context)
	{
		_context = context;
	}

	public Donation getDonation(String productId)
	{
		List<Donation> donations = DatabaseHelper.getHelper(_context).getDonationDao().queryForEq("ProductId", productId);
		if (donations != null && donations.size() > 0)
		{
			return donations.get(0);
		}
		return null;
	}

	public List<Donation> listDonations()
	{
		return DatabaseHelper.getHelper(_context).getDonationDao().queryForAll();
	}

	public void saveDonation(Donation donation)
	{
		DatabaseHelper.getHelper(_context).getDonationDao().createOrUpdate(donation);
	}

	public void deleteDonation(Donation donation)
	{
		DatabaseHelper.getHelper(_context).getDonationDao().delete(donation);
	}

	public void saveFavorite(ISite site)
	{
		Favorite favorite = new Favorite(site.getSiteId(), site.getName());
		favorite.setArea(site.getArea());
		DatabaseHelper.getHelper(_context).getFavoriteDao().createOrUpdate(favorite);
		onDataChanged();
	}

	public List<Favorite> listFavorites()
	{
		QueryBuilder<Favorite, Integer> builder = DatabaseHelper.getHelper(_context).getFavoriteDao().queryBuilder();
		try
		{
			return builder.orderBy("name", true).query();
		}
		catch (SQLException ex)
		{
			LogManager.error("Unable to list favorites", ex);
		}
		return new ArrayList<Favorite>();
	}

	public void deleteFavorite(ISite site)
	{
		DatabaseHelper.getHelper(_context).getFavoriteDao().delete(getFavorite(site));
		onDataChanged();
	}

	public Favorite getFavorite(ISite site)
	{
		try
		{
			PreparedQuery<Favorite> query = DatabaseHelper.getHelper(_context).getFavoriteDao().queryBuilder().where().eq("siteId", site.getSiteId()).prepare();
			return DatabaseHelper.getHelper(_context).getFavoriteDao().queryForFirst(query);
		}
		catch (Exception ex)
		{
			LogManager.error("Unable to load favorite", ex);
		}
		return null;
	}

	public List<Station> listStations(final Location location, int distanceInMeters)
	{
		List<Station> stations = new ArrayList<Station>();
		try
		{
			for (Station station : DatabaseHelper.getHelper(_context).getStationDao().queryForAll())
			{
				if (station.getDistanceInMeters(location) <= distanceInMeters)
				{
					stations.add(station);
				}
			}
			Collections.sort(stations, new Comparator<Station>()
			{
				public int compare(Station station1, Station station2)
				{
					return (int) (station1.getDistanceInMeters(location) - station2.getDistanceInMeters(location));
				}
			});
		}
		catch (Exception ex)
		{
			LogManager.error("Unable to list stations by location", ex);
		}
		return stations;
	}

	@SuppressLint("UseSparseArrays")
	public List<Station> listStations()
	{
		try
		{
			QueryBuilder<Station, Integer> builder = DatabaseHelper.getHelper(_context).getStationDao().queryBuilder();
			Map<Integer, Station> tempStations = new HashMap<Integer, Station>();
			for (Station station : builder.query())
			{
				if (!tempStations.containsKey(station.getSiteId()))
				{
					tempStations.put(station.getSiteId(), station);
				}
			}

			List<Station> stations = new ArrayList<Station>(tempStations.values());
			Collections.sort(stations, new Comparator<Station>()
			{
				@Override
				public int compare(Station lhs, Station rhs)
				{
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			return stations;
		}
		catch (Exception ex)
		{
			LogManager.error("Unable to list stations", ex);
		}
		return new ArrayList<Station>(0);
	}

	public List<Station> listStations(int type)
	{
		try
		{
			QueryBuilder<Station, Integer> builder = DatabaseHelper.getHelper(_context).getStationDao().queryBuilder();
			switch (type)
			{
				case Constants.TRANSPORTATION_TYPE_TRAIN:
					builder.where().eq("hasTrain", true);
					break;
				case Constants.TRANSPORTATION_TYPE_METRO:
					builder.where().eq("hasSubway", true);
					break;
				case Constants.TRANSPORTATION_TYPE_TRAM:
					builder.where().eq("hasTram", true);
					break;
				case Constants.TRANSPORTATION_TYPE_BUS:
					builder.where().eq("hasBus", true);
					break;
			}
			return builder.query();
		}
		catch (Exception ex)
		{
			LogManager.error("Unable to list stations by transportation type", ex);
		}
		return new ArrayList<Station>(0);
	}

	public Station getStation(int siteId)
	{
		try
		{
			QueryBuilder<Station, Integer> builder = DatabaseHelper.getHelper(_context).getStationDao().queryBuilder();
			builder.where().eq("siteId", siteId);
			return builder.queryForFirst();
		}
		catch (Exception ex)
		{
			LogManager.error("Unable to get station: " + siteId, ex);
		}
		return null;
	}

	public TrafficStatusSetting getTrafficStatusSetting(int widgetId)
	{
		try
		{
			QueryBuilder<TrafficStatusSetting, Integer> builder = DatabaseHelper.getHelper(_context).getTrafficStatusSettingDao().queryBuilder();
			builder.where().eq("widgetId", widgetId);
			return builder.queryForFirst();
		}
		catch (Exception ex)
		{
			LogManager.error("Unable to get traffic status setting");
		}
		return null;
	}

	public List<TrafficStatusSetting> listTrafficStatusSettings()
	{
		return DatabaseHelper.getHelper(_context).getTrafficStatusSettingDao().queryForAll();
	}

	public void saveTrafficStatusSetting(TrafficStatusSetting trafficStatusSetting)
	{
		DatabaseHelper.getHelper(_context).getTrafficStatusSettingDao().createOrUpdate(trafficStatusSetting);
	}

	public void deleteTrafficStatusSetting(TrafficStatusSetting setting)
	{
		DatabaseHelper.getHelper(_context).getTrafficStatusSettingDao().delete(setting);
	}

	public List<DepartureSetting> listDepartureSettings()
	{
		return DatabaseHelper.getHelper(_context).getDepartureSettingDao().queryForAll();
	}

	public List<DepartureSetting> listDepartureSettingsByWidgetId(int widgetId)
	{
		return DatabaseHelper.getHelper(_context).getDepartureSettingDao().queryForEq("widgetId", widgetId);
	}

	public DepartureSetting getDepartureSettingByWidgetId(int widgetId)
	{
		List<DepartureSetting> settings = listDepartureSettingsByWidgetId(widgetId);
		return settings != null && settings.size() > 0 ? settings.get(0) : null;
	}

	public void saveDepartureSetting(DepartureSetting setting)
	{
		DatabaseHelper.getHelper(_context).getDepartureSettingDao().createOrUpdate(setting);
	}

	public void deleteDepartureSetting(DepartureSetting setting)
	{
		DatabaseHelper.getHelper(_context).getDepartureSettingDao().delete(setting);
	}

	public List<Site> listSiteByQuery(String query)
	{
		return listSiteByQuery(query, true);
	}

	public List<Site> listSiteByQuery(String query, boolean onlyValid)
	{
		try
		{
			if (onlyValid)
			{
				QueryBuilder<Site, Integer> builder = DatabaseHelper.getHelper(_context).getSiteDao().queryBuilder();
				return builder.where().gt("validTo", System.currentTimeMillis()).and().eq("query", query).query();
			}
			else
			{
				return DatabaseHelper.getHelper(_context).getSiteDao().queryForEq("query", query);
			}
		}
		catch (Exception ex)
		{
		}
		return new ArrayList<Site>();
	}

	public void deleteSitesByQuery(String query)
	{
		DatabaseHelper.getHelper(_context).getSiteDao().delete(listSiteByQuery(query, false));
	}

	public void saveSite(Site site)
	{
		DatabaseHelper.getHelper(_context).getSiteDao().createOrUpdate(site);
	}

	public Site loadSite(int siteId)
	{
		try
		{
			QueryBuilder<Site, Integer> builder = DatabaseHelper.getHelper(_context).getSiteDao().queryBuilder();
			builder.where().eq("siteId", siteId);
			return builder.queryForFirst();
		}
		catch (SQLException e)
		{
			LogManager.error("Unable to get site.", e);
		}
		return null;
	}

	public void deleteSite(Site site)
	{
		DatabaseHelper.getHelper(_context).getSiteDao().createOrUpdate(site);
	}

	public void saveSiteSetting(SiteSetting siteSetting)
	{
		DatabaseHelper.getHelper(_context).getSiteSettingDao().createOrUpdate(siteSetting);
	}

	public SiteSetting getSiteSetting(int siteId)
	{
		try
		{
			QueryBuilder<SiteSetting, Integer> builder = DatabaseHelper.getHelper(_context).getSiteSettingDao().queryBuilder();
			builder.where().eq("siteId", siteId);
			return builder.queryForFirst();
		}
		catch (SQLException e)
		{
			LogManager.error("Unable to get site settings.", e);
		}
		return null;
	}

	public List<SiteSetting> listSiteSettingByLastSearch()
	{
		try
		{
			QueryBuilder<SiteSetting, Integer> builder = DatabaseHelper.getHelper(_context).getSiteSettingDao().queryBuilder();
			builder.where().ne("lastSearch", 0);
			builder.orderBy("lastSearch", false);
			return builder.query();
		}
		catch (SQLException e)
		{
			LogManager.error("Unable to get site settings.", e);
		}
		return new ArrayList<SiteSetting>();
	}

	public List<Site> listSitesByLastSearch()
	{
		try
		{
			QueryBuilder<SiteSetting, Integer> builder = DatabaseHelper.getHelper(_context).getSiteSettingDao().queryBuilder();
			builder.where().ne("lastSearch", 0);
			builder.orderBy("lastSearch", false);
			List<Site> sites = new ArrayList<Site>();
			for (SiteSetting siteSetting : builder.query())
			{
				Site site = loadSite(siteSetting.getSiteId());
				if (site != null)
				{
					sites.add(site);
				}
			}
			return sites;
		}
		catch (SQLException e)
		{
			LogManager.error("Unable to get sites.", e);
		}
		return new ArrayList<Site>();
	}

	public SiteFilter getSiteFilter(ISite site)
	{
		return getSiteFilter(site.getSiteId());
	}

	public SiteFilter getSiteFilter(int siteId)
	{
		try
		{
			QueryBuilder<SiteFilter, Integer> builder= DatabaseHelper.getHelper(_context).getSiteFilterDao().queryBuilder();
			builder.where().eq("siteId", siteId);
			return builder.queryForFirst();
		}
		catch (SQLException e)
		{
			LogManager.error("Unable to get site filter.", e);
		}
		return null;
	}

	public void deleteSiteFilter(SiteFilter siteFilter)
	{
	}

	private void onDataChanged()
	{
		for (IOnDataChanged dataChanged : _onDataChangedListeners)
		{
			dataChanged.dataChanged();
		}
	}

	public void addOnDataChangedListener(IOnDataChanged onDataChanged)
	{
		_onDataChangedListeners.add(onDataChanged);
	}

	public void removeOnDataChangedListener(IOnDataChanged onDataChanged)
	{
		_onDataChangedListeners.remove(onDataChanged);
	}
}