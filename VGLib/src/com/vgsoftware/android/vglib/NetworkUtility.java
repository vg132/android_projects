package com.vgsoftware.android.vglib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtility
{
	public static boolean isOnline(final Context context)
	{
		final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected())
		{
			return true;
		}
		return false;
	}
}
