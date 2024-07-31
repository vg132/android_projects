package com.vgsoftware.android.vglib;

public class NumberUtility
{
	public static Integer tryParseInt(String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (NumberFormatException nfe)
		{
			return null;
		}
	}
}
