package com.vgsoftware.android.othersideoftheworld;

import com.google.android.maps.GeoPoint;

public class MapUtility
{
	private MapUtility()
	{
	}
	
	public static double getLongitude(GeoPoint point)
	{
		if(point!=null)
		{
			return point.getLongitudeE6()/1E6;
		}
		return 0;
	}
	
	public static double getLongitude(int longitudeE6)
	{
		return longitudeE6/1E6;
	}
	
	public static double getLatitude(GeoPoint point)
	{
		if(point!=null)
		{
			return point.getLatitudeE6()/1E6;
		}
		return 0;
	}
	
	public static double getLatitude(int latitudeE6)
	{
		return latitudeE6/1E6;
	}
	
	public static GeoPoint getPoint(double latitude, double longitude)
	{
		return new GeoPoint(Math.round((float)(latitude*1E6)),Math.round((float)(longitude*1E6)));
	}

	public static double getDistance(GeoPoint point, GeoPoint point2)
	{
		if(point!=null && point2!=null)
		{
			return getDistance(getLatitude(point),getLongitude(point),getLatitude(point2),getLongitude(point2));
		}
		return 0;
	}
	
	public static double getDistance(double latitude1, double longitude1, double latitude2, double longitude2)
	{
		double earthRadius = 6378.7;
		double distanceLatitude = Math.toRadians(latitude2-latitude1);
		double distanceLongitude = Math.toRadians(longitude2-longitude1); 
		double a = Math.sin(distanceLatitude/2) * Math.sin(distanceLatitude/2) +
		        Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * 
		        Math.sin(distanceLongitude/2) * Math.sin(distanceLongitude/2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return earthRadius * c;
	}
}
