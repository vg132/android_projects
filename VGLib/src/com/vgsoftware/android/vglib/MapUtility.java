package com.vgsoftware.android.vglib;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import android.location.Location;

public class MapUtility
{
	private MapUtility()
	{
	}

	public static double getLongitude(final GeoPoint point)
	{
		if (point != null)
		{
			return point.getLongitudeE6() / 1E6;
		}
		return 0;
	}

	public static double getLongitude(int longitudeE6)
	{
		return longitudeE6 / 1E6;
	}

	public static double getLatitude(GeoPoint point)
	{
		if (point != null)
		{
			return point.getLatitudeE6() / 1E6;
		}
		return 0;
	}

	public static double getLatitude(int latitudeE6)
	{
		return latitudeE6 / 1E6;
	}

	public static GeoPoint getPoint(double latitude, double longitude)
	{
		return new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
	}
	
	public static GeoPoint getPoint(Location location)
	{
		return MapUtility.getPoint(location.getLatitude(),location.getLongitude());
	}

	public static double getDistance(GeoPoint point, GeoPoint point2)
	{
		if (point != null && point2 != null)
		{
			return getDistance(getLatitude(point), getLongitude(point), getLatitude(point2), getLongitude(point2));
		}
		return 0;
	}

	public static double getDistance(double latitude1, double longitude1, double latitude2, double longitude2)
	{
		double earthRadius = 6378.7;
		double distanceLatitude = Math.toRadians(latitude2 - latitude1);
		double distanceLongitude = Math.toRadians(longitude2 - longitude1);
		double a = Math.sin(distanceLatitude / 2) * Math.sin(distanceLatitude / 2) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.sin(distanceLongitude / 2) * Math.sin(distanceLongitude / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return earthRadius * c;
	}

	public static int metersToRadius(float meters, final MapView map, double latitude)
	{
		return (int) (map.getProjection().metersToEquatorPixels(meters) * (1 / Math.cos(Math.toRadians(latitude))));
	}
	
	public static int metersToRadius(float meters, final MapView map, final GeoPoint point)
	{
		return MapUtility.metersToRadius(meters,map, MapUtility.getLatitude(point));
	}
}
