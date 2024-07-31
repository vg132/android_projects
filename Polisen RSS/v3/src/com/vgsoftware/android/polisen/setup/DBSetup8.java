package com.vgsoftware.android.polisen.setup;

import com.vgsoftware.android.polisen.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.Region;

public class DBSetup8
{
	public static void createRegions(DatabaseHelper helper)
	{
		helper.getRegionDao().createIfNotExists(new Region(0, "Alla län", true));
		helper.getRegionDao().createIfNotExists(new Region(1, "Blekinge", true));
		helper.getRegionDao().createIfNotExists(new Region(2, "Dalarna", true));
		helper.getRegionDao().createIfNotExists(new Region(3, "Gotlands län", true));
		helper.getRegionDao().createIfNotExists(new Region(4, "Gävleborg", true));
		helper.getRegionDao().createIfNotExists(new Region(5, "Halland", true));
		helper.getRegionDao().createIfNotExists(new Region(6, "Jämtland", true));
		helper.getRegionDao().createIfNotExists(new Region(7, "Jönköpings län", true));
		helper.getRegionDao().createIfNotExists(new Region(8, "Kalmar län", true));
		helper.getRegionDao().createIfNotExists(new Region(9, "Kronoberg", true));
		helper.getRegionDao().createIfNotExists(new Region(10, "Norrbotten", true));
		helper.getRegionDao().createIfNotExists(new Region(11, "Skåne", true));
		helper.getRegionDao().createIfNotExists(new Region(12, "Stockholms län", true));
		helper.getRegionDao().createIfNotExists(new Region(13, "Södermanland", true));
		helper.getRegionDao().createIfNotExists(new Region(14, "Uppsala län", true));
		helper.getRegionDao().createIfNotExists(new Region(15, "Värmland", true));
		helper.getRegionDao().createIfNotExists(new Region(16, "Västerbotten", true));
		helper.getRegionDao().createIfNotExists(new Region(17, "Västernorrland", true));
		helper.getRegionDao().createIfNotExists(new Region(18, "Västmanland", true));
		helper.getRegionDao().createIfNotExists(new Region(19, "Västra Götaland", true));
		helper.getRegionDao().createIfNotExists(new Region(20, "Örebro län", true));
		helper.getRegionDao().createIfNotExists(new Region(21, "Östergötland", true));
	}

	public static void createFeeds(DatabaseHelper helper)
	{
		helper.getFeedDao().createOrUpdate(new Feed(0, "Nyheter", "https://polisen.se/Aktuellt/Nyheter/Nyheter-hela-landet/?feed=rss", true, "news"));
		helper.getFeedDao().createOrUpdate(new Feed(1, "Händelser", "https://polisen.se/Aktuellt/Handelser/Handelser-i-hela-landet/?feed=rss", true, "event"));
		helper.getFeedDao().createOrUpdate(new Feed(2, "Trafik", "https://polisen.se/Aktuellt/Trafikovervakning/Trafikovervakning-hela-landet/?feed=rss", true, "traffic"));
		helper.getFeedDao().createOrUpdate(new Feed(3, "Press", "https://polisen.se/Aktuellt/Pressmeddelanden/Pressmeddelanden-hela-landet/?feed=rss", true, "press"));
		helper.getFeedDao().createOrUpdate(new Feed(4, "Publikationer", "https://polisen.se/Aktuellt/Rapporter-och-publikationer/Senast-publicerade-rapporter-och-publikationer/?feed=rss", true, "publications"));
	}
}
