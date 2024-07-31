package com.vgsoftware.android.polisen.setup;

import com.vgsoftware.android.polisen.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.Region;

public class DBSetup5
{
	public static void createRegions(DatabaseHelper helper)
	{
		helper.getRegionDao().create(new Region(0, "Alla län", true));
		helper.getRegionDao().create(new Region(1, "Blekinge", true));
		helper.getRegionDao().create(new Region(2, "Dalarna", true));
		helper.getRegionDao().create(new Region(3, "Gotlands län", true));
		helper.getRegionDao().create(new Region(4, "Gävleborg", true));
		helper.getRegionDao().create(new Region(5, "Halland", true));
		helper.getRegionDao().create(new Region(6, "Jämtland", true));
		helper.getRegionDao().create(new Region(7, "Jönköpings län", true));
		helper.getRegionDao().create(new Region(8, "Kalmar län", true));
		helper.getRegionDao().create(new Region(9, "Kronoberg", true));
		helper.getRegionDao().create(new Region(10, "Norrbotten", true));
		helper.getRegionDao().create(new Region(11, "Skåne", true));
		helper.getRegionDao().create(new Region(12, "Stockholms län", true));
		helper.getRegionDao().create(new Region(13, "Södermanland", true));
		helper.getRegionDao().create(new Region(14, "Uppsala län", true));
		helper.getRegionDao().create(new Region(15, "Värmland", true));
		helper.getRegionDao().create(new Region(16, "Västerbotten", true));
		helper.getRegionDao().create(new Region(17, "Västernorrland", true));
		helper.getRegionDao().create(new Region(18, "Västmanland", true));
		helper.getRegionDao().create(new Region(19, "Västra Götaland", true));
		helper.getRegionDao().create(new Region(20, "Örebro län", true));
		helper.getRegionDao().create(new Region(21, "Östergötland", true));
	}

	@SuppressWarnings("deprecation")
	public static void createFeeds(DatabaseHelper helper)
	{
		helper.getFeedDao().create(new Feed(0, "Nyheter", "http://www.polisen.se/rss-nyheter", true));
		helper.getFeedDao().create(new Feed(1, "Händelser", "http://www.polisen.se/rss-handelser", true));
		helper.getFeedDao().create(new Feed(2, "Trafik", "http://www.polisen.se/rss-trafikovervakning", true));
	}
}
