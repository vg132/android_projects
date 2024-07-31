package com.vgsoftware.android.vglib;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

public class DateUtility
{
	private static final SimpleDateFormat[] DateFormats = { new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH), new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z", Locale.ENGLISH), new SimpleDateFormat("EEE, d MMM yy HH:mm z", Locale.ENGLISH), new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH), new SimpleDateFormat("EEE, d MMM yyyy HH:mm z", Locale.ENGLISH), new SimpleDateFormat("d MMM yy HH:mm z", Locale.ENGLISH), new SimpleDateFormat("d MMM yy HH:mm:ss z", Locale.ENGLISH), new SimpleDateFormat("d MMM yyyy HH:mm z", Locale.ENGLISH), new SimpleDateFormat("d MMM yyyy HH:mm:ss z", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.ENGLISH),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH), new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z", Locale.ENGLISH), new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH), new SimpleDateFormat("EEE MMM  d kk:mm:ss zzz yyyy", Locale.ENGLISH), new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH), new SimpleDateFormat("-yy-MM", Locale.ENGLISH), new SimpleDateFormat("-yyMM", Locale.ENGLISH), new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM", Locale.ENGLISH), new SimpleDateFormat("yyyy-D", Locale.ENGLISH), new SimpleDateFormat("-yyMM", Locale.ENGLISH), new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH),
			new SimpleDateFormat("yyMMdd", Locale.ENGLISH), new SimpleDateFormat("yyyy", Locale.ENGLISH), new SimpleDateFormat("yyD", Locale.ENGLISH),

			new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()), new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z", Locale.getDefault()), new SimpleDateFormat("EEE, d MMM yy HH:mm z", Locale.getDefault()), new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.getDefault()), new SimpleDateFormat("EEE, d MMM yyyy HH:mm z", Locale.getDefault()), new SimpleDateFormat("d MMM yy HH:mm z", Locale.getDefault()), new SimpleDateFormat("d MMM yy HH:mm:ss z", Locale.getDefault()), new SimpleDateFormat("d MMM yyyy HH:mm z", Locale.getDefault()), new SimpleDateFormat("d MMM yyyy HH:mm:ss z", Locale.getDefault()), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault()),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z", Locale.getDefault()), new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.getDefault()), new SimpleDateFormat("EEE MMM  d kk:mm:ss zzz yyyy", Locale.getDefault()), new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.getDefault()), new SimpleDateFormat("-yy-MM", Locale.getDefault()), new SimpleDateFormat("-yyMM", Locale.getDefault()), new SimpleDateFormat("yy-MM-dd", Locale.getDefault()), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()), new SimpleDateFormat("yyyy-MM", Locale.getDefault()), new SimpleDateFormat("yyyy-D", Locale.getDefault()),
			new SimpleDateFormat("-yyMM", Locale.getDefault()), new SimpleDateFormat("yyyyMMdd", Locale.getDefault()), new SimpleDateFormat("yyMMdd", Locale.getDefault()), new SimpleDateFormat("yyyy", Locale.getDefault()), new SimpleDateFormat("yyD", Locale.getDefault()) };

	public static Date parse(final String dateString)
	{
		Date date = null;
		SimpleDateFormat dateFormatter = null;
		for (int i = 0; i < DateFormats.length; i++)
		{
			try
			{
				dateFormatter = DateFormats[i];
				date = dateFormatter.parse(dateString);
				break;
			}
			catch (Exception ex)
			{
			}
		}
		if (date == null)
		{
			Log.w("VGLib", "Unable to parse date: '" + dateString + "'");
		}
		return date;
	}

	public static DateDiff dateDiff(final Date startDate, final Date endDate)
	{
		long _hours = 0;
		long _minutes = 0;
		long _seconds = 0;

		long _milliseconds = Math.abs(startDate.getTime() - endDate.getTime());
		long _days = _milliseconds / (24 * 60 * 60 * 1000);
		_milliseconds -= _days * (24 * 60 * 60 * 1000);
		if (_milliseconds > 0)
		{
			_hours = _milliseconds / (60 * 60 * 1000);
			_milliseconds -= _hours * (60 * 60 * 1000);
		}
		if (_milliseconds > 0)
		{
			_minutes = _milliseconds / (60 * 1000);
			_milliseconds -= _minutes * (60 * 1000);
		}
		if (_milliseconds > 0)
		{
			_seconds = _milliseconds / 1000;
			_milliseconds -= _seconds * 1000;
		}
		return new DateDiff(_days, _hours, _minutes, _seconds, _milliseconds);
	}

	public static String formatDate(Date date, Context context)
	{
		Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
		return dateFormat.format(date);
	}

	public static String formatTime(Date date, Context context)
	{
		Format timeFormat = android.text.format.DateFormat.getTimeFormat(context);
		return timeFormat.format(date);
	}
}
