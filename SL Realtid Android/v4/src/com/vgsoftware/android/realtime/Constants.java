package com.vgsoftware.android.realtime;

public class Constants
{
	private static final String BASE_URI = "com.vgsoftware.android.realtime";

	public static final String INTENT_ACTION_UPDATE_DEPARTURE_WIDGET = Constants.BASE_URI + ".UPDATE_DEPARTURE_WIDGET";
	public static final String INTENT_EXTRA_DEPARTURE_LIST = Constants.BASE_URI + "DEPARTURE_LIST";
	public static final String INTENT_EXTRA_TRANSPORTATIOIN_TYPE = Constants.BASE_URI + ".TRANSPORTATION_TYPE";
	public static final String INTENT_EXTRA_SITE_ID = Constants.BASE_URI + ".SITE_ID";
	public static final String INTENT_EXTRA_SELECTED_TAB = Constants.BASE_URI + "SELECTED_TAB";
	public static final String INTENT_EXTRA_SELECT_TAB = Constants.BASE_URI + ".SELECT_TAB";

	public static final int TRANSPORTATION_TYPE_TRAIN = 0;
	public static final int TRANSPORTATION_TYPE_METRO = 1;
	public static final int TRANSPORTATION_TYPE_TRAM = 2;
	public static final int TRANSPORTATION_TYPE_BUS = 3;
	public static final int TRANSPORTATION_TYPE_METRO_GREEN = 4;
	public static final int TRANSPORTATION_TYPE_METRO_RED = 5;
	public static final int TRANSPORTATION_TYPE_METRO_BLUE = 6;
}
