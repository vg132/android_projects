package com.vgsoftware.android.realtime.setup;

import android.database.sqlite.SQLiteDatabase;

public class BaseDBSetup
{
	protected static String dropStationTable = "DROP TABLE IF EXISTS Station;";

	protected static String createStationTable =
		"CREATE TABLE Station"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"SiteId INTEGER,"+
			"TransportationTypeId INTEGER,"+
			"Name VARCHAR(50) COLLATE NOCASE,"+
			"AreaName VARCHAR(50) COLLATE NOCASE,"+
			"Alias VARCHAR(50) COLLATE NOCASE,"+
			"Latitude FLOAT,"+
			"Longitude FLOAT"+
		")";

	protected static String[] stationData =
	{
		// Pendeltåg
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Barkarby',9703,59.403576,17.868834,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bro',9711,59.51142,17.635889,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bålsta',9710,59.569244,17.531003,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Farsta strand',9180,59.236435,18.101564,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Flemingsberg',9526,59.217584,17.946274,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gnesta',9540,59.048744,17.311621,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gröndalsviken',9721,58.899043,17.932069,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Handen',9730,59.167593,18.134394,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Helenelund',9507,59.409626,17.961445,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hemfosa',9725,59.068868,17.976422,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Huddinge',9527,59.237302,17.98022,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Häggvik',9505,59.444399,17.932262,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Jakobsberg',9702,59.423404,17.832892,1,'','jakan');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Jordbro',9729,59.141541,18.125811,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Järna',9542,59.093322,17.567267,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kallhäll',9701,59.453244,17.805812,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Karlberg',9510,59.339624,18.029423,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Krigslida',9727,59.109603,18.067499,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kungsängen',9700,59.477915,17.752361,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Märsta',9500,59.628126,17.861033,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Mölnbo',9541,59.047508,17.418137,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Norrviken',9504,59.458228,17.924237,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Nynäsgård',9722,58.913399,17.942466,1,'','');",	
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Nynäshamn',9720,58.901121,17.950962,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rosersberg',9501,59.58337,17.879776,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rotebro',9503,59.476433,17.914238,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rönninge',9523,59.193526,17.749979,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Segersäng',9724,59.029078,17.926641,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Skogås',9731,59.218211,18.154306,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sollentuna',9506,59.428861,17.948098,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Solna',9509,59.365154,18.010068,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Spånga',9704,59.383343,17.898788,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stockholms central',9000,59.330354,18.058841,1,'','centralen, t-centralen');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stockholms södra',9530,59.314163,18.064485,1,'','södra');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stuvsta',9528,59.253223,17.996013,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sundbyberg',9325,59.360751,17.970742,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Södertälje Syd',9543,59.162292,17.645416,1,'','syd');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Södertälje centrum',9520,59.192417,17.626791,1,'','centrum');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Södertälje hamn',9521,59.17928,17.646799,1,'','hamn');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Trångsund',9732,59.228027,18.129544,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tullinge',9525,59.205206,17.903037,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tumba',9524,59.199735,17.835617,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tungelsta',9726,59.102491,18.044872,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ulriksdal',9508,59.380742,18.000283,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Upplands Väsby',9502,59.521826,17.899647,1,'','väsby');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Västerhaninge',9728,59.122847,18.102593,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Älvsjö',9529,59.278774,18.010969,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Årstaberg',9531,59.300067,18.029181,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ösmo',9723,58.98471,17.902662,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Östertälje',9522,59.184415,17.659965,1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Uppsala centralstation', 6086, 59.858577, 17.646167, 1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Knivsta', 6091, 59.725709, 17.786736, 1,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Arlanda C', 9511, 59.649758, 17.929194, 1,'','');",

		// Roslagsbanan
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Altorp',9683,59.410194,18.072874,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bråvallavägen',9636,59.405585,18.060579,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bällsta',9627,59.523942,18.071844,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Djursholms Ekeby',9635,59.412727,18.057403,2,'','ekeby');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Djursholms Ösby',9637,59.397978,18.058648,2,'','ösby');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ekskogen',9621,59.639044,18.226983,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Enebyberg',9634,59.425565,18.051288,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ensta',9631,59.453468,18.063658,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Frösunda',9622,59.624204,18.170464,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Galoppfältet',9668,59.447147,18.085148,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hägernäs',9666,59.451155,18.124641,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kragstalund',9628,59.509264,18.075857,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kårsta',9620,59.656777,18.267441,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Lahäll',9682,59.427399,18.085341,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Lindholmen',9623,59.584152,18.109266,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Molnby',9624,59.556526,18.08474,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Mörby',9638,59.391871,18.046921,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Näsby allé',9681,59.427399,18.085341,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Näsbypark',9680,59.43052,18.096156,2,'','');",		
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ormsta',9625,59.545995,18.079504,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Roslags Näsby',9633,59.435245,18.057296,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rydbo',9665,59.465325,18.183231,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stockholms östra',9600,59.34609,18.071705,2,'','östra');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stocksund',9639,59.385086,18.043928,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tibble',9632,59.442467,18.062596,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tunagård',9661,59.469004,18.307471,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Täby centrum',9669,59.44394,18.074054,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Täby kyrkby',9629,59.460997,18.062725,2,'','kyrkby');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Täljö',9664,59.472389,18.233485,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Universitetet',9203,59.360309,18.056674,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Vallentuna',9626,59.533327,18.079805,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Vendevägen',9685,59.399906,18.067971,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Viggbyholm',9667,59.449045,18.103859,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Visinge',9630,59.460997,18.062725,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Åkers Runö',9663,59.480786,18.268901,2,'','runö');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Åkersberga',9662,59.479151,18.298437,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Östberga',9684,59.402839,18.073024,2,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Österskär',9660,59.460981,18.311945,2,'','');",

		// Tunnelbana
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Abrahamsberg',9110,59.336675,17.952947,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Akalla',9300,59.414198,17.920315,3,'','');",		
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Alby',9282,59.239508,17.845573,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Alvik',9112,59.333616,17.980263,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Aspudden',9293,59.306453,18.001528,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Axelsberg',9291,59.30435,17.975392,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bagarmossen',9141,59.276274,18.131453,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bandhagen',9163,59.270354,18.049335,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bergshamra',9202,59.381551,18.03659,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Björkhagen',9143,59.29112,18.11551,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Blackeberg',9105,59.348333,17.882813,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Blåsut',9187,59.29026,18.090963,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bredäng',9289,59.294906,17.933764,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Brommaplan',9109,59.338376,17.939257,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Danderyds sjukhus',9201,59.39191,18.04131,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Duvbo',9324,59.368472,17.96621,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Enskede gård',9167,59.2894,18.070289,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Farsta strand',9180,59.234991,18.101724,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Farsta',9181,59.24337,18.093194,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Fittja',9283,59.247453,17.86098,3,'','');",		
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Fridhemsplan',9115,59.333435,18.03477,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Fruängen',9260,59.285811,17.964964,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gamla stan',9193,59.323294,18.067059,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Globen',9168,59.294248,18.077917,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gubbängen',9183,59.262853,18.082058,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gullmarsplan',9189,59.299025,18.080835,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gärdet',9221,59.34667,18.099589,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hagsätra',9160,59.262722,18.012514,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hallonbergen',9303,59.374975,17.972149,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hallunda',9281,59.243239,17.825618,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hammarbyhöjden',9144,59.294736,18.104546,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hjulsta',9320,59.397266,17.892071,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hornstull',9295,59.315871,18.033886,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Husby',9301,59.410551,17.929213,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Huvudsta',9327,59.350044,17.989396,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hägerstensåsen',9262,59.295607,17.979125,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hässelby gård',9101,59.366875,17.84377,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hässelby strand',9100,59.361266,17.832344,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Högdalen',9162,59.263818,18.043026,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hökarängen',9182,59.257742,18.082809,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Hötorget',9119,59.335553,18.063583,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Islandstorget',9106,59.345833,17.894024,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Johannelund',9102,59.367925,17.85745,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Karlaplan',9222,59.338836,18.090835,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kista',9302,59.403337,17.946463,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kristineberg',9113,59.332691,18.00319,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kungsträdgården',9340,59.331288,18.076427,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kärrtorp',9142,59.284473,18.114438,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Liljeholmen',9294,59.310746,18.022814,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Mariatorget',9297,59.316977,18.063326,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Masmo',9284,59.249677,17.880329,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Medborgarplatsen',9191,59.31436,18.073454,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Midsommarkransen',9264,59.301852,18.012042,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Mälarhöjden',9290,59.300932,17.957282,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Mörby centrum',9200,59.398727,18.036203,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Norsborg',9280,59.243832,17.814417,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Näckrosen',9304,59.367122,17.986903,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Odenplan',9117,59.342994,18.049636,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rinkeby',9322,59.388624,17.932315,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rissne',9323,59.376333,17.943161,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ropsten',9223,59.357324,18.102121,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Råcksta',9104,59.354797,17.881826,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rådhuset',9309,59.330651,18.045749,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rådmansgatan',9118,59.340565,18.058691,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Rågsved',9161,59.256581,18.028134,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('S:t Eriksplan',9116,59.339909,18.036632,3,'','st eriksplan');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sandsborg',9186,59.284758,18.092401,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Skanstull',9190,59.307899,18.076158,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Skarpnäck',9140,59.266757,18.133342,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Skogskyrkogården',9185,59.27919,18.09549,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Skärholmen',9287,59.277174,17.9069,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Skärmarbrink',9188,59.295476,18.090362,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Slussen',9192,59.319528,18.072295,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sockenplan',9166,59.283296,18.070589,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Solna centrum',9305,59.358779,17.998717,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stadion',9205,59.342994,18.081779,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stadshagen',9307,59.337223,18.021148,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stora mossen',9111,59.334524,17.966176,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stureby',9164,59.274608,18.055601,3,'','');",		
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sundbybergs centrum',9325,59.361691,17.975454,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Svedmyra',9165,59.283334,18.070536,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sätra',9288,59.285022,17.921362,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('T-Centralen',9001,59.330792,18.063088,3,'','centralen,stockholm');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tallkrogen',9184,59.2711,18.085298,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tekniska högskolan',9204,59.34585,18.071694,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Telefonplan',9263,59.298324,17.997193,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Tensta',9321,59.394981,17.90468,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Thorildsplan',9114,59.331788,18.015432,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Universitetet',9203,59.365526,18.054872,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Vreten',9326,59.354621,17.976657,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Vällingby',9103,59.36324,17.872052,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Västertorp',9261,59.291444,17.966638,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Västra skogen',9306,59.347974,18.00734,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Vårberg',9286,59.275968,17.89012,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Vårby gård',9285,59.264631,17.884369,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Zinkensdamm',9296,59.31782,18.050065,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Ängbyplan',9107,59.341856,17.906942,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Åkeshov',9108,59.342037,17.924899,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Örnsberg',9292,59.305555,17.989168,3,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Östermalmstorg',9206,59.334962,18.074012,3,'','');",

		// Tvärbanan
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Alvik',9112,59.33322773187615,17.97965168952942,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Alviks strand',9812,59.32888801573286,17.98211932182312,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Stora Essingen',9811,59.324766687715886,17.993041276931763,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gröndal',1605,59.31580668526209,18.010443449020386,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Trekanten',1603,59.31407517684315,18.018372058868408,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Liljeholmen',9294,59.310822713968925,18.02454113960266,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Årstadal',9807,59.30582295146178,18.02552819252014,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Årstaberg',9531,59.2994147332507,18.02930474281311,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Årstafältet',1521,59.29636903714818,18.039711713790894,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Valla torg',1525,59.29504878560735,18.048434257507324,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Linde',1503,59.29334498280576,18.064184188842773,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Globen',9168,59.29415032788101,18.07647943496704,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Gullmarsplan',9189,59.29944212136717,18.08027744293213,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Mårtensdal',1555,59.30272579672863,18.088141679763794,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Luma',1552,59.304174456814664,18.095828890800476,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sickla kaj',1550,59.30285450815607,18.103601932525635,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sickla udde',9820,59.30620904912501,18.108880519866943,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Johannesfred', 3612, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Norra Ulvsunda', 3614, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Karlsbodavägen', 3685, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bällsta bro', 3680, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sundbybergs centrum', 9325, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Solna Business Park', 5119, 0, 0, 4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Solna centrum', 9305, 0, 0, 4,'','');",

		// Spårväg City
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Sergels torg',1000,59.33262,18.06618,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Kungsträdgården',1016,59.33276,18.07069,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Norrmalmstorg',1015,59.33342,18.07345,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Nybroplan',1105,59.33272,18.07706,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Styrmansgatan',1107,59.33155,18.08504,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Djurgårdsbron',1100,59.33181,18.09236,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Nordiska museet/Vasamuseet',1408,59.32930,18.09562,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Liljevalchs/Gröna Lund',1406,59.32524,18.09729,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Skansen',1405,59.32396,18.10064,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Djurgårdsskolan',1403,59.32298,18.10493,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Bellmansro',1409,59.32398,18.11053,4,'','');",
		"INSERT INTO Station(Name,SiteId,Latitude,Longitude,TransportationTypeId,AreaName, Alias) VALUES('Waldemarsudde',1400,59.32263,18.11133,4,'','');",
		
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

	protected static String dropTransportationTypeTable =	"DROP TABLE IF EXISTS TransportationType;";
	
	protected static String createTransportationTypeTable =
		"CREATE TABLE TransportationType"+
		"("+
			"Id INTEGER PRIMARY KEY,"+
			"Name VARCHAR(30) COLLATE NOCASE"+
		");";

	protected static String[] transportationTypeData =
	{
		"INSERT INTO TransportationType(Id,Name) VALUES(1,'Pendeltåg');",
		"INSERT INTO TransportationType(Id,Name) VALUES(2,'Lokalbana');",
		"INSERT INTO TransportationType(Id,Name) VALUES(3,'Tunnelbanan');",
		"INSERT INTO TransportationType(Id,Name) VALUES(4,'Spårvagn');",
		"INSERT INTO TransportationType(Id,Name) VALUES(5,'Buss');"
	};
	
	protected static String dropSavedTable = "DROP TABLE IF EXISTS Saved;";
	
	protected static String createSavedTable =
		"CREATE TABLE Saved"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"StationId INTEGER,"+
			"TransportationTypeId INTEGER"+
		");";
	
	protected static String dropDonationTable = "DROP TABLE IF EXISTS Donation;";
	
	protected static String createDonationTable = 
		"CREATE TABLE Donation"+
		"("+
			"Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"ProductId VARCHAR(100),"+
			"Status VARCHAR(100)"+
		");";

	protected static void insertData(SQLiteDatabase db, String[] dataList)
	{
		for(String data : dataList)
		{
			db.execSQL(data);
		}
	}
}