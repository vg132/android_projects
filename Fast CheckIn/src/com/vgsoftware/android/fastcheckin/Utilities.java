package com.vgsoftware.android.fastcheckin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Debug;

public class Utilities
{
	private static final int TIMEOUT = 1000 * 60 * 2;

	private Utilities()
	{	
	}
	
	public static String getVersion(Context context)
	{
		try
		{
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return pi.versionName;
		}
		catch (NameNotFoundException e)
		{
			LogManager.warn("Failed to get version info.",e);
			return "";
		}
	}
	
	public static boolean isDebug()
	{
		return Debug.isDebuggerConnected();
	}

	public static double getDistance(Location location1, Location location2)
	{
		return getDistance(location1.getLatitude(),location1.getLongitude(),location2.getLatitude(),location2.getLongitude());
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
		return Math.abs(earthRadius * c);
	}
	
	/**
	 * Determines whether one Location reading is better than the current Location
	 * fix
	 * 
	 * @param location
	 *          The new Location that you want to evaluate
	 * @param currentBestLocation
	 *          The current Location fix, to which you want to compare the new one
	 */
	public static boolean isBetterLocation(Location location, Location currentBestLocation)
	{
		if (currentBestLocation == null)
		{
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TIMEOUT;
		boolean isSignificantlyOlder = timeDelta < -TIMEOUT;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the
		// new location
		// because the user has likely moved
		if (isSignificantlyNewer)
		{
			return true;
			// If the new location is more than two minutes older, it must be worse
		}
		else if (isSignificantlyOlder)
		{
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate)
		{
			return true;
		}
		else if (isNewer && !isLessAccurate)
		{
			return true;
		}
		else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider)
		{
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2)
	{
		if (provider1 == null)
		{
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
