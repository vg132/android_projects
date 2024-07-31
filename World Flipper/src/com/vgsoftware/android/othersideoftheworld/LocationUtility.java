package com.vgsoftware.android.othersideoftheworld;

import java.util.List;

import android.location.Address;
import android.text.TextUtils;

public class LocationUtility
{
	private LocationUtility()
	{
	}

	public static String getAddress(List<Address> addresses)
	{
		String address = "";
		if (addresses.size() > 0)
		{
			for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
			{
				if (!TextUtils.isEmpty(addresses.get(0).getAddressLine(i)))
				{
					address += addresses.get(0).getAddressLine(i) + "\n";
				}
			}
		}
		return address.trim();
	}
}
