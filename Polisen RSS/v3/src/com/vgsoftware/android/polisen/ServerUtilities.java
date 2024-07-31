package com.vgsoftware.android.polisen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.android.gcm.GCMRegistrar;
import com.vgsoftware.android.vglib.HttpUtility;

import android.content.Context;

public class ServerUtilities
{
	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();

	public static boolean register(final Context context, final String regId)
	{
		String serverUrl = "http://polisenrss.appspot.com/registration";
		Map<String, String> params = new HashMap<String, String>();
		params.put("action", "register");
		params.put("deviceId", regId);
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		for (int i = 1; i <= MAX_ATTEMPTS; i++)
		{
			try
			{
				HttpUtility.postData(serverUrl, params);
				GCMRegistrar.setRegisteredOnServer(context, true);
				return true;
			}
			catch (IOException e)
			{
				if (i == MAX_ATTEMPTS)
				{
					break;
				}
				try
				{
					Thread.sleep(backoff);
				}
				catch (InterruptedException e1)
				{
					Thread.currentThread().interrupt();
					return false;
				}
				backoff *= 2;
			}
		}
		return false;
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static void unregister(final Context context, final String regId)
	{
		String serverUrl = "http://polisenrss.appspot.com/registration";
		Map<String, String> params = new HashMap<String, String>();
		params.put("action", "unregister");
		params.put("deviceId", regId);
		try
		{
			HttpUtility.postData(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
		}
		catch (IOException ex)
		{
			Log.error("Unable to unregister device.", ex);
		}
	}
}
