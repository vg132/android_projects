package com.vgsoftware.android.realtime.parse;

import java.util.List;

import android.os.Handler;

public abstract class ParserBase extends Thread
{
	protected Exception _exception = null;
	protected Handler _handler = null;
	protected List<?> _data = null;

	public Exception getException()
	{
		return _exception;
	}

	public List<?> getData()
	{
		return _data;
	}
}
