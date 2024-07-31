package com.vgsoftware.android.isitfriday;

public class TimeSpan
{
	private static final int MILLIES_IN_SEC = 1000;
	private static final int SECS_IN_MINUTE = 60;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;

	private int mMillies;
	private int mSeconds;
	private int mMinutes;
	private int mHours;
	private int mDays;

	public TimeSpan(long totalMillies)
	{
		mMillies = (int)(totalMillies % MILLIES_IN_SEC);
		totalMillies /= MILLIES_IN_SEC;
		mSeconds = (int)(totalMillies % SECS_IN_MINUTE);
		totalMillies /= SECS_IN_MINUTE;
		mMinutes = (int)(totalMillies % MINUTES_IN_HOUR);
		totalMillies /= MINUTES_IN_HOUR;
		mHours = (int)(totalMillies % HOURS_IN_DAY);
		totalMillies /= HOURS_IN_DAY;
		mDays = (int)totalMillies;
	}

	public int getDays()
	{
		return mDays;
	}

	public int getHours()
	{
		return mHours;
	}

	public int getMinutes()
	{
		return mMinutes;
	}

	public int getSeconds()
	{
		return mSeconds;
	}

	public int getMillies()
	{
		return mMillies;
	}
}
