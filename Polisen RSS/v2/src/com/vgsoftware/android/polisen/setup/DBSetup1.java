package com.vgsoftware.android.polisen.setup;

import com.vgsoftware.android.polisen.Log;

import android.database.sqlite.SQLiteDatabase;

public class DBSetup1
{
	private static String[] dropTables=
	{
		"DROP TABLE Feed",
		"DROP TABLE Region",
		"DROP TABLE Category",
		"DROP TABLE FeedItem"
	};
	
	private static String[] createTables =
	{
		"CREATE TABLE Feed"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"Name VARCHAR(30),"+
			"Url VARCHAR(255),"+
			"CategoryId INT,"+
			"Active BIT"+
		");",

		"CREATE TABLE Category"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"Name VARCHAR(30),"+
			"Active BIT"+
		");",

		"CREATE TABLE FeedItem"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"FeedId INT,"+
			"Title VARCHAR(30),"+
			"Description VARCHAR(255),"+
			"Url VARCHAR(255),"+
			"Read BIT,"+
			"Date DATETIME,"+
			"Added DATETIME,"+
			"Deleted BIT"+
		");"
	};
	
	private static String[] categories=
	{
		"INSERT INTO Category(Id, Name, Active) VALUES(0,'Alla län',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(1,'Blekinge',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(2,'Dalarna',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(3,'Gotlands län',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(4,'Gävleborg',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(5,'Halland',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(6,'Jämtland',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(7,'Jönköpings län',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(8,'Kalmar län',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(9,'Kronoberg',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(10,'Norrbotten',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(11,'Såne',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(12,'Stockholms län',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(13,'Södermanland',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(14,'Uppsala län',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(15,'Värmland',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(16,'Västerbotten',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(17,'Västernorrland',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(18,'Västmanland',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(19,'Västra Götaland',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(20,'Örebro län',0);",
		"INSERT INTO Category(Id, Name, Active) VALUES(21,'Östergötland',0);"
	};
	
	private static String[] feeds=
	{
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(0,0,'Nyheter','http://www.polisen.se/sv/Aktuellt/RSS/Nyheter-RSS-lank/',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(1,0,'Händelser','http://www.polisen.se/sv/Aktuellt/RSS/Handelser-RSS-lank/',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(2,0,'Pressmeddelanden','http://www.polisen.se/sv/Aktuellt/RSS/Pressmeddelande-RSS-lank/',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(3,0,'Trafikövervakning','http://www.polisen.se/sv/Aktuellt/RSS/Trafikovervakning-RSS-lank/',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(4,0,'Publikationer','http://www.polisen.se/sv/Aktuellt/RSS/Publikationer-RSS-lank/',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(5,1,'Nyheter','http://www.polisen.se/Blekinge/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Blekinge/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(6,1,'Händelser','http://www.polisen.se/Blekinge/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Blekinge/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(7,1,'Pressmeddelanden','http://www.polisen.se/Blekinge/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Blekinge/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(8,2,'Nyheter','http://www.polisen.se/Dalarna/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Dalarna/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(9,2,'Händelser','http://www.polisen.se/Dalarna/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Dalarna/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(10,2,'Pressmeddelanden','http://www.polisen.se/Dalarna/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Dalarna/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(11,3,'Nyheter','http://www.polisen.se/Gotlands_lan/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Gotland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(12,3,'Händelser','http://www.polisen.se/Gotlands_lan/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Gotland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(13,3,'Pressmeddelanden','http://www.polisen.se/Gotlands_lan/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Gotland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(14,4,'Nyheter','http://www.polisen.se/Gavleborg/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Gavleborg/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(15,4,'Händelser','http://www.polisen.se/Gavleborg/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Gavleborg/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(16,4,'Pressmeddelanden','http://www.polisen.se/Gavleborg/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Gavleborg/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(17,5,'Nyheter','http://www.polisen.se/Halland/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Halland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(18,5,'Händelser','http://www.polisen.se/Halland/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Halland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(19,5,'Pressmeddelanden','http://www.polisen.se/Halland/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Halland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(20,6,'Nyheter','http://www.polisen.se/Jamtland/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Jamtland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(21,6,'Händelser','http://www.polisen.se/Jamtland/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Jamtland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(22,6,'Pressmeddelanden','http://www.polisen.se/Jamtland/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Jamtland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(23,7,'Nyheter','http://www.polisen.se/Jonkopings_lan/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Jonkopings-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(24,7,'Händelser','http://www.polisen.se/Jonkopings_lan/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Jonkoping/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(25,7,'Pressmeddelanden','http://www.polisen.se/Jonkopings_lan/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Jonkopings-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(26,8,'Nyheter','http://www.polisen.se/Kalmar_lan/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Kalmar-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(27,8,'Händelser','http://www.polisen.se/Kalmar_lan/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Kalmar-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(28,8,'Pressmeddelanden','http://www.polisen.se/Kalmar_lan/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Kalmar-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(29,9,'Nyheter','http://www.polisen.se/Kronoberg/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Kronoberg/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(30,9,'Händelser','http://www.polisen.se/Kronoberg/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Kronoberg/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(31,9,'Pressmeddelanden','http://www.polisen.se/Kronoberg/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Kronoberg/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(32,10,'Nyheter','http://www.polisen.se/Norrbotten/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Norrbotten/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(33,10,'Händelser','http://www.polisen.se/Norrbotten/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Norrbotten/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(34,10,'Pressmeddelanden','http://www.polisen.se/Norrbotten/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Norrbotten/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(35,11,'Nyheter','http://www.polisen.se/Skane/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Skane/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(36,11,'Händelser','http://www.polisen.se/Skane/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Skane/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(37,11,'Pressmeddelanden','http://www.polisen.se/Skane/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Skane/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(38,12,'Nyheter','http://www.polisen.se/Stockholms_lan/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Stockholms-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(39,12,'Händelser','http://www.polisen.se/Stockholms_lan/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Stockholms-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(40,12,'Pressmeddelanden','http://www.polisen.se/Stockholms_lan/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Stockholms-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(41,13,'Nyheter','http://www.polisen.se/Sodermanland/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Sodermanland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(42,13,'Händelser','http://www.polisen.se/Sodermanland/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Sodermanland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(43,13,'Pressmeddelanden','http://www.polisen.se/Sodermanland/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Sodermanland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(44,14,'Nyheter','http://www.polisen.se/Uppsala_lan/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Uppsala-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(45,14,'Händelser','http://www.polisen.se/Uppsala_lan/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Uppsala-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(46,14,'Pressmeddelanden','http://www.polisen.se/Uppsala_lan/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Uppsala-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(47,15,'Nyheter','http://www.polisen.se/Varmland/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Varmland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(48,15,'Händelser','http://www.polisen.se/Varmland/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Varmland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(49,15,'Pressmeddelanden','http://www.polisen.se/Varmland/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Varmland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(50,16,'Nyheter','http://www.polisen.se/Vasterbotten/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Vasterbotten/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(51,16,'Händelser','http://www.polisen.se/Vasterbotten/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Vasterbotten/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(52,16,'Pressmeddelanden','http://www.polisen.se/Vasterbotten/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Vasterbotten/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(53,17,'Nyheter','http://www.polisen.se/Vasternorrland/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Vasternorrland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(54,17,'Händelser','http://www.polisen.se/Vasternorrland/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Vasternorrland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(55,17,'Pressmeddelanden','http://www.polisen.se/Vasternorrland/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Vasternorrland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(56,18,'Nyheter','http://www.polisen.se/Vastmanland/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Vastmanland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(57,18,'Händelser','http://www.polisen.se/Vastmanland/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Vastmanland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(58,18,'Pressmeddelanden','http://www.polisen.se/Vastmanland/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Vastmanland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(59,19,'Nyheter','http://www.polisen.se/Vastra_Gotaland/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Vastra-Gotaland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(60,19,'Händelser','http://www.polisen.se/Vastra_Gotaland/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Vastra-Gotaland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(61,19,'Pressmeddelanden','http://www.polisen.se/Vastra_Gotaland/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Vastra-Gotaland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(62,20,'Nyheter','http://www.polisen.se/Orebro_lan/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Orebro-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(63,20,'Händelser','http://www.polisen.se/Orebro_lan/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Orebro-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(64,20,'Pressmeddelanden','http://www.polisen.se/Orebro_lan/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS---Orebro-lan/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(65,21,'Nyheter','http://www.polisen.se/Ostergotland/sv/Aktuellt/RSS/Lokal-RSS---Nyheter/Lokala-RSS-listor1/Nyheter-RSS---Ostergotland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(66,21,'Händelser','http://www.polisen.se/Ostergotland/sv/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Ostergotland/?feed=rss',1);",
		"INSERT INTO Feed(Id, CategoryId, Name, Url,Active) VALUES(67,21,'Pressmeddelanden','http://www.polisen.se/Ostergotland/sv/Aktuellt/RSS/Lokal-RSS---Pressmeddelanden/Lokala-RSS-listor/Press-RSS----Ostergotland/?feed=rss',1);"
	};
	
	public static void Create(SQLiteDatabase db)
	{
		try
		{
			for(String table : createTables)
			{
				db.execSQL(table);
			}
			for(String category : categories)
			{
				db.execSQL(category);
			}
			for(String feed : feeds)
			{
				db.execSQL(feed);
			}
		}
		catch(Exception ex)
		{
			Log.error("Faild to create database",ex);
		}
	}
	
	public static void Upgrade(SQLiteDatabase db)
	{
		try
		{
			for(String table : dropTables)
			{
				db.execSQL(table);
			}
			Create(db);
		}
		catch(Exception ex)
		{
			Log.error("Faild to delete old database",ex);
		}
	}
}
