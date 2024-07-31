package com.vgsoftware.android.polisen.setup;

import com.vgsoftware.android.polisen.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.Region;

public class DBSetup6
{
	public static void createRegions(DatabaseHelper helper)
	{
		helper.getRegionDao().createIfNotExists(new Region(0, "Alla l�n", true));
		helper.getRegionDao().createIfNotExists(new Region(1, "Blekinge", true));
		helper.getRegionDao().createIfNotExists(new Region(2, "Dalarna", true));
		helper.getRegionDao().createIfNotExists(new Region(3, "Gotlands l�n", true));
		helper.getRegionDao().createIfNotExists(new Region(4, "G�vleborg", true));
		helper.getRegionDao().createIfNotExists(new Region(5, "Halland", true));
		helper.getRegionDao().createIfNotExists(new Region(6, "J�mtland", true));
		helper.getRegionDao().createIfNotExists(new Region(7, "J�nk�pings l�n", true));
		helper.getRegionDao().createIfNotExists(new Region(8, "Kalmar l�n", true));
		helper.getRegionDao().createIfNotExists(new Region(9, "Kronoberg", true));
		helper.getRegionDao().createIfNotExists(new Region(10, "Norrbotten", true));
		helper.getRegionDao().createIfNotExists(new Region(11, "Sk�ne", true));
		helper.getRegionDao().createIfNotExists(new Region(12, "Stockholms l�n", true));
		helper.getRegionDao().createIfNotExists(new Region(13, "S�dermanland", true));
		helper.getRegionDao().createIfNotExists(new Region(14, "Uppsala l�n", true));
		helper.getRegionDao().createIfNotExists(new Region(15, "V�rmland", true));
		helper.getRegionDao().createIfNotExists(new Region(16, "V�sterbotten", true));
		helper.getRegionDao().createIfNotExists(new Region(17, "V�sternorrland", true));
		helper.getRegionDao().createIfNotExists(new Region(18, "V�stmanland", true));
		helper.getRegionDao().createIfNotExists(new Region(19, "V�stra G�taland", true));
		helper.getRegionDao().createIfNotExists(new Region(20, "�rebro l�n", true));
		helper.getRegionDao().createIfNotExists(new Region(21, "�sterg�tland", true));
	}

	public static void createFeeds(DatabaseHelper helper)
	{
		helper.getFeedDao().createOrUpdate(new Feed(0, "Nyheter", "http://www.polisen.se/rss-nyheter", true, "news"));
		helper.getFeedDao().createOrUpdate(new Feed(1, "H�ndelser", "http://www.polisen.se/rss-handelser", true, "event"));
		helper.getFeedDao().createOrUpdate(new Feed(2, "Trafik", "http://www.polisen.se/rss-trafikovervakning", true, "traffic"));
		helper.getFeedDao().createOrUpdate(new Feed(3, "Press", "http://www.polisen.se/Aktuellt/RSS/Pressmeddelande-RSS-lank/", true, "press"));
		helper.getFeedDao().createOrUpdate(new Feed(4, "Publikationer", "http://www.polisen.se/rss-publikationer", true, "publications"));
	}
}
