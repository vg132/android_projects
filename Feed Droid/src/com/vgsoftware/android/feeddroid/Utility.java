package com.vgsoftware.android.feeddroid;

import java.text.DateFormat;
import java.util.Date;

import android.content.Context;

public class Utility
{
	private static Context _context;
	private static Utility _instance;

	private Utility(Context context)
	{
		_context = context;
	}

	public static synchronized void Init(Context context)
	{
		_instance = new Utility(context);
	}

	public static Utility getInstance()
	{
		if (_instance == null)
		{
			return new Utility(null);
		}
		return _instance;
	}

	public Context getContext()
	{
		return _context;
	}

	public static String removeHtml(String input)
	{
		if (input != null)
		{
			return input.replaceAll("<(.|\n)*?>", "");
		}
		return "";
	}

	public String formatDateTime(Date date)
	{
		String dateString = "";
		if (date != null && getContext() != null)
		{
			DateFormat df = android.text.format.DateFormat.getMediumDateFormat(getContext());
			dateString = df.format(date);
			df = android.text.format.DateFormat.getTimeFormat(getContext());
			dateString += " - " + df.format(date);
		}
		return dateString;
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
}
