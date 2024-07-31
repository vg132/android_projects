package com.vgsoftware.android.autosportrss.setup;

import com.vgsoftware.android.autosportrss.Log;

import android.database.sqlite.SQLiteDatabase;

public class DBSetup1
{
	private static String[] dropTables=
	{
		"DROP TABLE RssFeed"
	};
	
	private static String[] dropTablesFor2 = 
	{
		"DROP TABLE FeedItem",
		"DROP TABLE Feed",
		"DROP TABLE Category"
	};
	
	private static String[] createTables =
	{
		"CREATE TABLE Feed"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"Name VARCHAR(30),"+
			"Url VARCHAR(255),"+
			"Active BIT"+
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
			"CategoryId INT,"+
			"Read BIT,"+
			"Date DATETIME,"+
			"Added DATETIME,"+
			"Deleted BIT"+
		");"
	};
	
	private static String[] feeds=
	{
		"INSERT INTO Feed(Name, Url, Active) VALUES('All News','http://www.autosport.com/rss/allnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Formula One','http://www.autosport.com/rss/f1news.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('MotoGP','http://www.autosport.com/rss/motogpnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('IndyCar','http://www.autosport.com/rss/irlnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('GP2 Series','http://www.autosport.com/rss/gp2news.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('F3','http://www.autosport.com/rss/f3news.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Renault World','http://www.autosport.com/rss/wsrnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('WTCC','http://www.autosport.com/rss/wtccnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('NASCAR','http://www.autosport.com/rss/nascarnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('DTM','http://www.autosport.com/rss/dtmnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('BTCC','http://www.autosport.com/rss/btccnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('V8 Supercar','http://www.autosport.com/rss/v8news.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('WRC','http://www.autosport.com/rss/wrcnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Le Mans 24 hours','http://www.autosport.com/rss/lemansnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('ALMS','http://www.autosport.com/rss/almsnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Le Mans Series','http://www.autosport.com/rss/lmsnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('FIA GT','http://www.autosport.com/rss/fiagtnews.xml',0);",
		"INSERT INTO Feed(Name, Url, Active) VALUES('Features','http://www.autosport.com/rss/features.xml',0);"
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
			Log.error("Faild to create database",ex);
		}
	}

	public static void UpgradeFrom2(SQLiteDatabase db)
	{
		try
		{
			for(String table : dropTablesFor2)
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
