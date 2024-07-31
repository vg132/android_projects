package com.vgsoftware.android.feedlib;

import java.util.Date;
import java.util.List;

public interface IFeed
{
	String getTitle();
	String getDescription();
	Date getDate();
	IFeedItem getFeedItem(int pos);
	List<IFeedItem> getFeedItems();
}
