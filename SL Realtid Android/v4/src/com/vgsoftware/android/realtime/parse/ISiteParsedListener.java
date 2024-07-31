package com.vgsoftware.android.realtime.parse;

import java.util.List;

import com.vgsoftware.android.realtime.model.ISite;

public interface ISiteParsedListener
{
	public void sitesParsed(List<ISite> sites);
}
