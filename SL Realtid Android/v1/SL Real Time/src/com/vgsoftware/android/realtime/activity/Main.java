package com.vgsoftware.android.realtime.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.vgsoftware.android.realtime.Departure;
import com.vgsoftware.android.realtime.GoogleAnalytics;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.RealTimeData;
import com.vgsoftware.android.realtime.RealTimeDbAdapter;
import com.vgsoftware.android.realtime.Favorite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Main extends TabActivity
{
	private static final int PROGRESS_DIALOG = 0;
	private static final int MENU_DELETE_FAVORITE=1;
	private static final int MENU_LOAD_FAVORITE=2;
	private static final String PREFERENCES_FILE = "SLRealTimePreferencesFile";
	private static final String LOG_GROUP="SL Real Time";

	private RealTimeData _realTimeDataThread;
	private RealTimeDbAdapter _databaseAdapter;
	
	private LinearLayout _realTimeLinearLayout=null;
	private LinearLayout _favoriteLinearLayout=null;

	private Spinner _transportTypeSpinner=null;
	private AutoCompleteTextView _stationTextView=null;
	private Button _showTimesButton=null;
	private TextView _resultTextView=null;
	private ListView _favoriteListView=null;

	private ProgressDialog _progressDialog;
	
	private SharedPreferences _settings = null;

	private void setupLocalVariables()
	{
		//Layouts
		_realTimeLinearLayout=(LinearLayout)findViewById(R.id.RealTimeLinearLayout);
		_favoriteLinearLayout=(LinearLayout)findViewById(R.id.FavoriteLinearLayout);

		//Controls
		_transportTypeSpinner=(Spinner)findViewById(R.id.TypeSpinner);
		_stationTextView=(AutoCompleteTextView)findViewById(R.id.StationTextView);
		_showTimesButton=(Button)findViewById(R.id.ShowTimesButton);
		_resultTextView=(TextView)findViewById(R.id.ResultTextView);
		_favoriteListView=(ListView)findViewById(R.id.FavoriteListView);

		_settings = getSharedPreferences(Main.PREFERENCES_FILE,Context.MODE_PRIVATE);
		_databaseAdapter=new RealTimeDbAdapter(this);
	}

	private void setupLayout()
	{
		TabHost mTabHost = getTabHost();
		Drawable draw= getResources().getDrawable(R.drawable.ic_menu_search);
		mTabHost.addTab(mTabHost.newTabSpec("realTimeTab").setIndicator(getString(R.string.RealTimeTabName),getResources().getDrawable(R.drawable.ic_menu_search)).setContent(R.id.RealTimeLinearLayout));
		mTabHost.addTab(mTabHost.newTabSpec("favoriteTab").setIndicator(getString(R.string.FavoriteTabName),getResources().getDrawable(R.drawable.ic_menu_favorite)).setContent(R.id.FavoriteLinearLayout));
		mTabHost.setCurrentTab(0);
	}

	private void supetListeners()
	{
		_showTimesButton.setOnClickListener
		(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ShowRealTimeData();
				}
			}
		);
		
		_transportTypeSpinner.setOnItemSelectedListener
		(
			new AdapterView.OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id)
				{
					initializeStations();
				}
	
		    @Override
		    public void onNothingSelected(AdapterView<?> parentView)
		    {
		    }
			}
		);

		_favoriteListView.setOnItemClickListener
		(
			new AdapterView.OnItemClickListener()
			{
				@SuppressWarnings("unchecked")
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
				{
					Object obj=_favoriteListView.getItemAtPosition(position);
					if(obj instanceof HashMap<?,?>)
					{
						HashMap<String,String> item=(HashMap<String,String>)obj;
						loadFavorite(Integer.parseInt(item.get("Id")));
					}
				}
			}
		);
		
		getTabHost().setOnTabChangedListener
		(
			new TabHost.OnTabChangeListener()
			{	
				public void onTabChanged(String tabName)
				{
					if(tabName.equals("favoriteTab"))
					{
						setupFavoriteList();
					}
				}
      }
		);
	}

	private void ShowRealTimeData()
	{
		showDialog(Main.PROGRESS_DIALOG);
		_realTimeDataThread=new RealTimeData(this,handler,_transportTypeSpinner.getSelectedItemPosition(),_stationTextView.getText().toString());
		_realTimeDataThread.start();
		GoogleAnalytics.getInstance().trackPageView("/ShowRealTimeData");
	}
	
  protected Dialog onCreateDialog(int id)
  {
  	switch(id)
  	{
  		case Main.PROGRESS_DIALOG:
        _progressDialog = new ProgressDialog(this);
        _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progressDialog.setMessage(getResources().getString(R.string.ProgressDialogText));
        return _progressDialog;
  	}
  	return null;
  }
  
  private final Handler handler = new Handler()
  {
  	public void handleMessage(Message msg)
  	{
  		if(msg.what==0)
  		{
  			displayComuterTrainInformation( _realTimeDataThread.getDepartures());
  			dismissDialog(Main.PROGRESS_DIALOG);
  		}
  	}
  };

  private void initializeStations()
  {
		switch(_transportTypeSpinner.getSelectedItemPosition())
		{
			case 0:
				setupStationTextView(R.array.CommuterTrainName);
				break;
			case 1:
				setupStationTextView(R.array.RoslagsbanaName);
				break;
			case 2:
				setupStationTextView(R.array.GreenLineName);
				break;
			case 3:
				setupStationTextView(R.array.RedLineName);
				break;
			case 4:
				setupStationTextView(R.array.BlueLineName);
				break;
			default:
				_stationTextView.setEnabled(false);
				_showTimesButton.setEnabled(false);
				break;
		}
  }
  
	private void setupStationTextView(int resourceId)
	{
  	ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,resourceId,android.R.layout.simple_dropdown_item_1line);
  	_stationTextView.setAdapter(adapter);
  	_stationTextView.setText("");
  	_stationTextView.setEnabled(true);
  	_showTimesButton.setEnabled(true);
	}

	private void displayComuterTrainInformation(ArrayList<Departure> departures)
	{
		if(departures.size()>0)
		{
			String result="";
			for(Departure departure : departures)
			{
				if(departure.getTime() == null || departure.getTime().equals(""))
				{
					result+=departure.getDestination() + "\n";
				}
				else if(departure.getStatus() == null || departure.getStatus().equals(""))
				{
					result+=departure.getDestination() + " - " + departure.getTime() + "\n";
				}
				else
				{
					result+=departure.getDestination() + " - " + departure.getTime() + " - " + departure.getStatus() + "\n";
				}
			}
			_resultTextView.setText(result);
		}
		else
		{
			_resultTextView.setText(R.string.NoInformationText);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//Setup google analytics
		GoogleAnalytics.initialize(this);
		GoogleAnalytics.getInstance().trackPageView("/Start");

		setContentView(R.layout.main);

		setupLocalVariables();
		setupLayout();
		supetListeners();

		registerForContextMenu(_favoriteListView);
		
		initializeStations();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{ 
		super.onCreateOptionsMenu(menu);
		menu.add(0,0,0,R.string.MenuSaveSearch);
		menu.add(0,1,0,R.string.MenuAbout);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(contextMenu, view, menuInfo);
		contextMenu.setHeaderTitle(R.string.MenuHeading);
		contextMenu.add(0,Main.MENU_LOAD_FAVORITE,0,R.string.MenuLoadFavorite);
		contextMenu.add(0,Main.MENU_DELETE_FAVORITE,0,R.string.MenuDeleteFavorite);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		int selectedFavoriteId=0;
		Object obj=_favoriteListView.getAdapter().getItem(info.position);
		if(obj instanceof HashMap<?,?>)
		{
			HashMap<String,String> item2=(HashMap<String,String>)obj;
			selectedFavoriteId=Integer.parseInt(item2.get("Id"));
		}
		
		switch (item.getItemId())
		{
			case Main.MENU_LOAD_FAVORITE:
				loadFavorite(selectedFavoriteId);
				return true;
			case Main.MENU_DELETE_FAVORITE:
				_databaseAdapter.deleteFavorite(selectedFavoriteId);
				setupFavoriteList();
				return true;
			default:
				Log.i("","Default");
				return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{
		menu.getItem(0).setEnabled(getStationId(_stationTextView.getText().toString())>=0);
		return true;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId()==0)
		{
			saveFavorite();
		}
		else if(item.getItemId()==1)
		{
			Kalle();
		}
		return false;
	}
	
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

	private void setupFavoriteList()
	{		
		SimpleAdapter notes = new SimpleAdapter(this, listFavorites(), R.layout.row,new String[] { "StationName","TransportationTypeName" }, new int[] { R.id.text1, R.id.text2 } );
		_favoriteListView.setAdapter(notes);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		GoogleAnalytics.getInstance().trackPageView("/Exit");
		GoogleAnalytics.getInstance().stop();
	}

	private ArrayList<HashMap<String,String>> listFavorites()
	{
		ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		try
		{
			ArrayList<Favorite> favorites=_databaseAdapter.getFavorites();
			for(Favorite favorite : favorites)
			{
				HashMap<String,String> map=new HashMap<String,String>();
				map.put("Id",String.valueOf(favorite.getId()));
				map.put("TransportationTypeId",String.valueOf(favorite.getTransportationTypeId()));
				map.put("StationId",String.valueOf(favorite.getStationId()));
				map.put("StationName",getStationName(favorite.getStationId(),favorite.getTransportationTypeId()));
				map.put("TransportationTypeName",getResources().getStringArray(R.array.TransportTypes)[favorite.getTransportationTypeId()]);
				list.add(map);
			}
		}
		catch(Exception ex)
		{
			Log.e(Main.LOG_GROUP,"Unable to list favorites",ex);
		}
		return list;
	}

	private void loadFavorite(int id)
	{
		Favorite station = _databaseAdapter.loadFavorite(id);
		if(station!=null)
		{
			try
			{
				_transportTypeSpinner.setSelection(station.getTransportationTypeId());
				_stationTextView.setText(getStationName(station.getStationId()));
				getTabHost().setCurrentTab(0);
				ShowRealTimeData();
			}
			catch(Exception ex)
			{
				Log.e(Main.LOG_GROUP,"Unable to load favorite with id: "+id,ex);
			}
		}
	}

	private void saveFavorite()
	{
		int stationId=getStationId(_stationTextView.getText().toString());
		if(stationId>=0)
		{
			Favorite station=new Favorite(stationId,_transportTypeSpinner.getSelectedItemPosition());
			_databaseAdapter.saveFavorite(station);
		}
	}

	private int getStationId(String name)
	{
		int nameResourceArray;
		int idResourceArray;

		switch(_transportTypeSpinner.getSelectedItemPosition())
		{
			case 0:
				nameResourceArray=R.array.CommuterTrainName;
				idResourceArray=R.array.CommuterTrainId;
				break;
			case 1:
				nameResourceArray=R.array.RoslagsbanaName;
				idResourceArray=R.array.RoslagsbananId;
				break;
			case 2:
				nameResourceArray=R.array.GreenLineName;
				idResourceArray=R.array.GreenLineId;
				break;
			case 3:
				nameResourceArray=R.array.RedLineName;
				idResourceArray=R.array.RedLineId;
				break;
			case 4:
				nameResourceArray=R.array.BlueLineName;
				idResourceArray=R.array.BlueLineId;
				break;
			default:
				return -1;
		}

		int ids[] =  getResources().getIntArray(idResourceArray);
		String stations[] = getResources().getStringArray(nameResourceArray);
		for(int i=0;i<stations.length;i++)
		{
			if(stations[i].equalsIgnoreCase(name))
			{
				return ids[i];
			}
		}
		return -1;
	}
	
	private String getStationName(int stationId)
	{
		return getStationName(stationId,_transportTypeSpinner.getSelectedItemPosition());
	}
	
	private String getStationName(int stationId, int transportationTypeId)
	{
		switch(transportationTypeId)
		{
			case 0:
				return getStationName(stationId,R.array.CommuterTrainName,R.array.CommuterTrainId);
			case 1:
				return getStationName(stationId,R.array.RoslagsbanaName,R.array.RoslagsbananId);
			case 2:
				return getStationName(stationId,R.array.GreenLineName,R.array.GreenLineId);
			case 3:
				return getStationName(stationId,R.array.RedLineName,R.array.RedLineId);
			case 4:
				return getStationName(stationId,R.array.BlueLineName,R.array.BlueLineId);
		}
		return null;
	}
	
	private String getStationName(int stationId, int nameResourceArray, int idResourceArray)
	{
		int ids[] =  getResources().getIntArray(idResourceArray);
		String stations[] = getResources().getStringArray(nameResourceArray);
		for(int i=0;i<ids.length;i++)
		{
			if(ids[i]==stationId)
			{
				return stations[i];
			}
		}
		return null;
	}
	
	private void Kalle()
	{
		Builder builder=new AlertDialog.Builder(this);
		builder.setTitle(R.string.AboutSLRealTimeHeading);
    builder.setMessage(R.string.AboutSLRealTime);
    builder.setIcon(R.drawable.ic_menu_info_details);
    builder.setPositiveButton("Ok", null); 
		builder.show();
	}

	/*
	private void kalle()
	{
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
		
		Location l=locationManager.getLastKnownLocation("gps");
		if(l==null)
		{
			l=locationManager.getLastKnownLocation("network");
		}
		if(l!=null)
		{
			_resultTextView.setText("Longiture: "+l.getLongitude()+", Latitude: "+l.getLatitude());
		}
		else
		{
			_resultTextView.setText("Null");
		}
	}*/
}