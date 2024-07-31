package com.vgsoftware.android.realtime.setup;

import android.database.sqlite.SQLiteDatabase;

public class DBSetup8 extends BaseDBSetup
{
	protected static String[] updateData = 
	{
		"UPDATE TransportationType SET Name = 'Lokalbana' WHERE Id = 2;",
		"UPDATE TransportationType SET Name = 'Spårvagn' WHERE Id = 4;",
		"DELETE FROM TransportationType WHERE Id = 6",
		"UPDATE Station SET TransportationTypeId = 2 WHERE TransportationTypeId = 4;",
		"UPDATE Station SET TransportationTypeId = 4 WHERE TransportationTypeId = 6;",
		"UPDATE Saved SET TransportationTypeId = 2 WHERE TransportationTypeId = 4;",
		"UPDATE Saved SET TransportationTypeId = 4 WHERE TransportationTypeId = 6;"
	};
	
	protected static String[] newData =
	{
		// Saltsjöbanan
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Slussen', 9192 ,59.31952810332031, 18.071951866149902,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Henriksdal', 9432 ,59.31230114475141, 18.108086585998535,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sickla', 9431 ,59.306847059062214, 18.121304512023926,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Nacka', 9430 ,59.30654037766777, 18.12997341156006,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Saltsjö-Järla', 9429 ,59.30686896477028, 18.149585723876953,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Lillängen', 9428 ,59.305182183964995, 18.161344528198242,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Storängen', 9427 ,59.30559841037957, 18.178038597106934,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Saltsjö-Duvnäs', 9426 ,59.300559537718414, 18.19859504699707,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Östervik', 9425 ,59.29510356884767, 18.23558807373047,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Fisksätra', 9424 ,59.29418319870373, 18.25657367706299,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Igelboda', 9423 ,59.289800142200335, 18.275842666625977,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tippen', 9443 ,59.2839786, 18.2772057,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tattby', 9442 ,59.27910311493099, 18.281936645507812,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Erstaviksbadet', 9441 ,59.2729858860848, 18.285112380981445,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Solsidan', 9440 ,59.27116585322382, 18.296356201171875,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Neglinge', 9422 ,59.288572785213915, 18.292150497436523,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ringvägen', 9421 ,59.28304913102033, 18.30219268798828,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Saltsjöbaden', 9420 ,59.27903734412124, 18.313007354736328,2,'','');",

		// Lidingöbanan
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ropsten', 9220 , 59.35732413640648, 18.10248613357544,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Torsvik', 9252, 59.3619171965557, 18.117785453796387,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Baggeby', 9251, 59.35689760642419, 18.133835792541504,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bodal', 9250, 59.353676585168934, 18.138502836227417,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Larsberg', 9249, 59.35048807467962, 18.14620614051819,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('AGA', 9248, 59.34663739870551, 18.155089616775513,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Skärsätra', 9219, 59.34336616898086, 18.170442581176758,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kottla', 9245, 59.34442743857396, 18.179798126220703,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Högberga', 9244, 59.34395151249717, 18.19308042526245,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Brevik', 9243, 59.34840963904922, 18.203551769256592,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Käppala', 9242, 59.35273592367938, 18.218003511428833,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Talludden', 9241, 59.355415646490954, 18.22254180908203,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gåshaga', 9240, 59.356935884974874, 18.22910785675049,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gåshaga Brygga', 9239, 59.35687573294728, 18.234118223190308,2,'','');",

		// Nockebybanan
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Nockeby',9120, 59.32864720819035, 17.91866898536682,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Nockeby torg',9121, 59.32899200028048, 17.928314208984375,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Olovslund',9122, 59.32791930228881, 17.93522357940674,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Höglandstorget',9123, 59.323359958036626, 17.93995499610901,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ålstens gård',9124, 59.32061201461015, 17.952078580856323,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ålstensgatan',9125, 59.32332711540307, 17.956466674804688,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Smedslätten',9126, 59.32091856910908, 17.964341640472412,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Klövervägen',9127, 59.324717425792606, 17.973836660385132,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Alléparken',9128, 59.32904672886196, 17.974212169647217,2,'','');",
	};
	
	public static void upgrade(SQLiteDatabase db)
	{
		try
		{
			insertData(db,updateData);
			insertData(db,newData);
		}
		catch (Exception ex)
		{
			DBSetup8.create(db);
		}
	}

	public static void create(SQLiteDatabase db)
	{
		try
		{
			db.execSQL(dropDonationTable);
			db.execSQL(dropSavedTable);
			db.execSQL(dropStationTable);
			db.execSQL(dropTransportationTypeTable);

			db.execSQL(createDonationTable);
			db.execSQL(createTransportationTypeTable);
			db.execSQL(createStationTable);
			db.execSQL(createSavedTable);

			insertData(db,transportationTypeData);
			insertData(db,stationData);
		}
		catch (Exception ex)
		{
		}
	}
}
