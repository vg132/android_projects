package com.vgsoftware.android.feeddroid.setup;

import com.vgsoftware.android.feeddroid.LogManager;

import android.database.sqlite.SQLiteDatabase;

public class DBSetup1
{
	private static String[] createTables =
	{
		"CREATE TABLE Feed"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"Name VARCHAR(30),"+
			"Url VARCHAR(255),"+
			"Active BIT,"+
			"CategoryId INT"+
		");",

		"CREATE TABLE Category"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"Name VARCHAR(30)"+
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
	
	private static String[] feeds=
	{
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 1','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=52',1);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 2','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=13',1);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 3','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=17',1);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 4','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=6',1);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 5','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=56',1);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 6','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=2',1);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 7','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=3',1);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 8','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=55',1);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Sweclockers 9','http://www.sweclockers.com/forum/external.php?type=RSS2&forumids=54',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('Autosport F1','http://www.autosport.com/rss/f1news.xml',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('Kotaku','http://www.kotaku.com/index.xml',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('DN - Topnyheter','http://www.dn.se/toppnyheter-rss',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('DN - Ekonomi','http://www.dn.se/ekonomi-rss',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('DN - Sport','http://www.dn.se/sport-rss',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('DN - Debatt','http://www.dn.se/debatt-rss',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('DN - Ledare','http://www.dn.se/ledare-rss',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('DN - Kultur','http://www.dn.se/kultur-rss',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('CNN','http://rss.cnn.com/rss/edition.rss',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('Engadget','http://www.engadget.com/rss.xml',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('Apple','http://images.apple.com/main/rss/hotnews/hotnews.rss',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('Slashdot','http://rss.slashdot.org/Slashdot/slashdot',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('BBC','http://feeds.bbci.co.uk/news/rss.xml',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('Giantbomb','http://feeds.feedburner.com/GiantbombAll',1);",
		//"INSERT INTO Feed(Name, Url, Active) VALUES('Giantbomb 2','feed://feeds.feedburner.com/GiantbombAll',1);"
	};
	
	public static void Create(SQLiteDatabase db)
	{
		try
		{
			for(String table : createTables)
			{
				db.execSQL(table);
			}
			for(String feed : feeds)
			{
				db.execSQL(feed);
			}
		}
		catch(Exception ex)
		{
			LogManager.error("Faild to create database",ex);
		}
	}
}
