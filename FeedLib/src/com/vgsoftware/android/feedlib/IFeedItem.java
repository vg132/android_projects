package com.vgsoftware.android.feedlib;

import java.util.Date;

public interface IFeedItem
{
	String getTitle();
	String getDescription();
	String getLink();
	Date getDate();
}
