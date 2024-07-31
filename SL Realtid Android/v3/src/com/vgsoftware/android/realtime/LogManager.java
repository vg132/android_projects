package com.vgsoftware.android.realtime;

import android.content.Context;

import com.vgsoftware.android.vglib.logging.AbstractLogManager;

public class LogManager extends AbstractLogManager
{
	public LogManager(Context context)
	{
		super("SL Realtid");
		setEnabled(Utilities.isDebug(context));
	}

	public LogManager(Context context, String tag)
	{
		super(tag);
		setEnabled(Utilities.isDebug(context));
	}
}
