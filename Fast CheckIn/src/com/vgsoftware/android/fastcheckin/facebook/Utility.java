package com.vgsoftware.android.fastcheckin.facebook;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;
import com.vgsoftware.android.fastcheckin.LogManager;
import com.vgsoftware.android.fastcheckin.dataabstraction.Place;

import android.location.Location;
import android.os.Bundle;

public class Utility
{
	public static boolean checkIn(Facebook facebook, Place place, Location location, String message)
	{
		if (place != null && location != null)
		{
			try
			{
				Bundle params = new Bundle();
				params.putString("place", place.getId());
				params.putString("message", message != null ? message : "");
				JSONObject jo = new JSONObject();

				jo.put("latitude", location.getLatitude());
				jo.put("longitude", location.getLongitude());
				params.putString("coordinates", jo.toString());
				facebook.request("me/checkins", params, "POST");
				return true;
			}
			catch (IOException ex)
			{
				LogManager.error("Unable to check-in to Facebook.", ex);
			}
			catch (JSONException ex)
			{
				LogManager.error("Unable to parse geo position for check-in.", ex);
			}
		}
		return false;
	}
}
