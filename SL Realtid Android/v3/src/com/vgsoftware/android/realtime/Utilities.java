package com.vgsoftware.android.realtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

public class Utilities
{
	private static final int TIMEOUT = 1000 * 60 * 2;

	private Utilities()
	{
	}

	public static String getContent(String url)
	{
		try
		{
			HttpClient hc = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse rp = hc.execute(get);

			if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String content = getString(rp.getEntity().getContent());
				return content;
			}
		}
		catch (Exception ex)
		{
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

	// public synchronized static GoogleAnalytics getGoogleAnalytics(Context context)
	// {
	// return GoogleAnalytics.getInstance(context);
	// }

	// public synchronized static void trackEvent(Context context, String category, String action, String label, int value)
	// {
	// Utilities.getGoogleAnalytics(context).trackEvent(category, action, label, value);
	// Utilities.getGoogleAnalytics(context).dispatch();
	// }

	// public synchronized static void trackPageView(Context context, String folder, String item)
	// {
	// if (Utilities.isDebug(context))
	// {
	// LogManager log = new LogManager(context, "Google Analytics");
	// log.debug("/debug/" + folder + "/" + item);
	// }
	// else
	// {
	// Utilities.getGoogleAnalytics(context).trackPageView("/" + folder + "/" + item);
	// Utilities.getGoogleAnalytics(context).dispatch();
	// }
	// }

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
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.vgsoftware.android.realtime", 0);
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

	public static int getTransportationTypeDrawable(int transportationTypeId)
	{
		return Utilities.getTransportationTypeDrawable(transportationTypeId, "-1");
	}

	public static int getTransportationTypeDrawable(int transportationTypeId, String lineId)
	{
		switch (transportationTypeId)
		{
			case 1:
			case 6:
				return R.drawable.train;
			case 2:
				return R.drawable.tram;
			case 3:
				if (lineId.equals("10") || lineId.equals("11"))
				{
					return R.drawable.subway_blue;
				}
				else if (lineId.equals("13") || lineId.equals("14"))
				{
					return R.drawable.subway_red;
				}
				else if (lineId.equals("17") || lineId.equals("18") || lineId.equals("19"))
				{
					return R.drawable.subway_green;
				}
				return R.drawable.subway;
			case 4:
				return R.drawable.tram_2;
			case 5:
				return R.drawable.bus;
		}
		return 0;
	}

	public static String capitalize(String str, char... delimiters)
	{
		int delimLen = delimiters == null ? -1 : delimiters.length;
		if (TextUtils.isEmpty(str) || delimLen == 0)
		{
			return str;
		}
		char[] buffer = str.toCharArray();
		boolean capitalizeNext = true;
		for (int i = 0; i < buffer.length; i++)
		{
			char ch = buffer[i];
			if (isDelimiter(ch, delimiters))
			{
				capitalizeNext = true;
			}
			else if (capitalizeNext)
			{
				buffer[i] = Character.toTitleCase(ch);
				capitalizeNext = false;
			}
		}
		return new String(buffer);
	}

	private static boolean isDelimiter(char ch, char[] delimiters)
	{
		if (delimiters == null)
		{
			return Character.isWhitespace(ch);
		}
		for (char delimiter : delimiters)
		{
			if (ch == delimiter)
			{
				return true;
			}
		}
		return false;
	}
}
