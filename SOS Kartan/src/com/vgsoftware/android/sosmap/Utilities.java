package com.vgsoftware.android.sosmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.vgsoftware.android.vglib.GoogleAnalytics;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.util.Log;

public class Utilities
{
	private static final int TIMEOUT = 1000 * 60 * 2;

	private static GoogleAnalytics _googleAnalytics = null;

	private Utilities()
	{
	}

	public static String getContent(String url)
	{
		try
		{
			System.out.println("url: " + url);
			HttpClient hc = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			get.addHeader("Referer", "http://div.dn.se/dn/sos/soslive.php");
			HttpResponse rp = hc.execute(get);

			if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String content = getString(rp.getEntity().getContent());
				return content;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace(System.err);
		}
		return null;
	}

	public static String getString(InputStream stream)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader buffer = new BufferedReader(reader, 8192);

			String line;
			while ((line = buffer.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			stream.close();
		}
		catch (UnsupportedCharsetException uce)
		{
			Log.e("SL Real Time", uce.getMessage());
		}
		catch (IOException io)
		{
			Log.e("SL Real Time", io.getMessage());
		}
		return sb.toString();
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

	public synchronized static GoogleAnalytics getGoogleAnalytics(Context context)
	{
		if (_googleAnalytics == null)
		{
			_googleAnalytics = new GoogleAnalytics(context, context.getString(R.string.GoogleAnalyticsTrackerId));
		}
		return _googleAnalytics;
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

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2)
	{
		if (provider1 == null)
		{
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public static boolean isDebug(Context context)
	{

		try
		{
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.vgsoftware.android.sosmap", 0);
			int flags = packageInfo.applicationInfo.flags;
			return (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		}
		catch (NameNotFoundException e)
		{
		}
		return false;
	}

	public static int getVersion(Context context)
	{
		try
		{
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}
		catch (NameNotFoundException ex)
		{
		}
		return -1;
	}

	public static String stringFormat(String input, Object... params)
	{
		String output = input;
		int itemIndex = 0;
		for (Object item : params)
		{
			output = output.replace("{" + (itemIndex++) + "}", item.toString());
		}
		return output;
	}

	public static boolean isAppOnForeground(Context context)
	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
		{
			return false;
		}
		final String packageName = context.getPackageName();
		for (RunningAppProcessInfo appProcess : appProcesses)
		{
			if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName))
			{
				return true;
			}
		}
		return false;
	}
}
