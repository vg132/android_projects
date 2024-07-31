package com.vgsoftware.android.realtime;

import android.location.Location;

public class Utilities
{
	private static final int TIMEOUT = 1000 * 60 * 2;

	private Utilities()
	{
	}

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

	// Checks whether two providers are the same
	private static boolean isSameProvider(String provider1, String provider2)
	{
		if (provider1 == null)
		{
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public static String getTransportationTypeName(int transportationType)
	{
		switch (transportationType)
		{
			case Constants.TRANSPORTATION_TYPE_BUS:
				return "Buss";
			case Constants.TRANSPORTATION_TYPE_METRO:
				return "Tunnelbana";
			case Constants.TRANSPORTATION_TYPE_TRAIN:
				return "Tåg";
			case Constants.TRANSPORTATION_TYPE_TRAM:
				return "Lokalbana";
			case Constants.TRANSPORTATION_TYPE_METRO_GREEN:
				return "Grön";
			case Constants.TRANSPORTATION_TYPE_METRO_RED:
				return "Röd";
			case Constants.TRANSPORTATION_TYPE_METRO_BLUE:
				return "Blå";
		}
		return "";
	}
}
