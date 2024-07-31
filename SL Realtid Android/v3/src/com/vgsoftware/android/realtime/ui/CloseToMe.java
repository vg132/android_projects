package com.vgsoftware.android.realtime.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.ads.AdView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.UserPreferences;
import com.vgsoftware.android.realtime.Settings;
import com.vgsoftware.android.realtime.Utilities;
import com.vgsoftware.android.realtime.dataabstraction.Database;
import com.vgsoftware.android.realtime.dataabstraction.Station;
import com.vgsoftware.android.realtime.dataabstraction.TransportationType;
import com.vgsoftware.android.realtime.ui.adapters.CloseToMeAdapter;
import com.vgsoftware.android.realtime.ui.overlay.OnStationSelectedListener;
import com.vgsoftware.android.realtime.ui.overlay.RadiusOverlay;
import com.vgsoftware.android.realtime.ui.overlay.StationOverlay;
import com.vgsoftware.android.realtime.ui.overlay.StationOverlayItem;
import com.vgsoftware.android.vglib.MapUtility;
import com.vgsoftware.android.vglib.location.LocationService;
import com.vgsoftware.android.vglib.location.OnLocationChangeListener;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CloseToMe extends MapActivity implements OnLocationChangeListener, OnItemClickListener, OnStationSelectedListener
{
	private ListView _listView = null;
	private AdView _bannerAdView = null;
	private Location _lastKnownLocation = null;
	private LocationService _locationService = null;
	private MapView _mapView = null;
	private LinearLayout _container = null;
	private TextView _textView = null;

	private int _currentView = R.id.listView;

	private Map<Integer, StationOverlay> _mapOverlays = new HashMap<Integer, StationOverlay>();
	private RadiusOverlay _radiusOverlay = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupApplication();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		_locationService.stop();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		_locationService.start();
	}

	private void setupApplication()
	{
		setContentView(R.layout.close_to_me);

		_currentView = UserPreferences.getInstance().getCloseToMeView();
		_container = (LinearLayout) findViewById(R.id.container);

		if (Utilities.isDebug(this))
		{
			_mapView = new MapView(this, getString(R.string.apiKeyGoogleMapsDebug));
		}
		else
		{
			_mapView = new MapView(this, getString(R.string.apiKeyGoogleMapsRelease));
		}

		_mapView.setVisibility(View.GONE);
		_mapView.setClickable(true);
		_container.addView(_mapView);

		_textView = (TextView) findViewById(R.id.textView);
		_textView.setText(R.string.searchingLocation);
		_bannerAdView = (AdView) findViewById(R.id.adView);
		if (Database.getInstance().listDonation().size() > 0)
		{
			_bannerAdView.setVisibility(View.GONE);
		}

		_listView = (ListView) findViewById(R.id.listView);
		_listView.setOnItemClickListener(this);

		_locationService = new LocationService(this, this, true, Settings.getInstance().isMyLocationGPSEnabled());
		_locationService.start();
		setupView();
		runOnUiThread(new Runnable()
		{
			public void run()
			{
				setupMapOverlays();
			}
		});
	}

	private void setupMapOverlays()
	{
		List<Overlay> mapOverlays = _mapView.getOverlays();
		Drawable marker = getResources().getDrawable(R.drawable.marker);
		for (TransportationType type : Database.getInstance().listTransportationTypes())
		{
			StationOverlay stationOverlay = new StationOverlay(marker, this);
			stationOverlay.setOnStationSelectedListener(this);
			for (Station station : Database.getInstance().listStations(type.getId()))
			{
				if (station.getLatitude() != 0 && station.getLongitude() != 0)
				{
					GeoPoint point = MapUtility.getPoint(station.getLatitude(), station.getLongitude());
					stationOverlay.addOverlay(new StationOverlayItem(point, station));
				}
			}
			if (stationOverlay.size() > 0)
			{
				_mapOverlays.put(type.getId(), stationOverlay);
			}
		}
		for (StationOverlay overlay : _mapOverlays.values())
		{
			mapOverlays.add(overlay);
		}
	}

	private void setupMyLocation()
	{
		if (_currentView == R.id.listView)
		{
			List<Station> stations = listCloseStations(Settings.getInstance().getMyLocationDistance());
			if (stations.size() > 0)
			{
				_textView.setVisibility(View.GONE);
				_listView.setAdapter(new CloseToMeAdapter(this.getApplicationContext(), R.layout.close_to_me_row, stations, _lastKnownLocation));
			}
			else
			{
				_textView.setVisibility(View.VISIBLE);
				_textView.setText(R.string.noStationsFound);
			}
		}
		else if (_currentView == R.id.mapView && _lastKnownLocation != null)
		{
			if (_radiusOverlay == null)
			{
				_radiusOverlay = new RadiusOverlay();
				List<Overlay> overlays = _mapView.getOverlays();
				overlays.add(0, _radiusOverlay);
			}
			GeoPoint centerPoint = MapUtility.getPoint(_lastKnownLocation);
			_mapView.getController().setZoom(13);
			_mapView.getController().setCenter(centerPoint);
			_radiusOverlay.setRadius(centerPoint, Settings.getInstance().getMyLocationDistance() * 1000);
		}
	}

	private List<Station> listCloseStations(int distance)
	{
		try
		{
			Location location = _lastKnownLocation;
			if (location != null)
			{
				List<Station> stations = Database.getInstance().listStations(location.getLatitude(), location.getLongitude(), distance);
				Collections.sort(stations, new Comparator<Station>()
				{
					public int compare(Station station1, Station station2)
					{
						Location loc = _lastKnownLocation;
						double difference = station1.getDistance(loc.getLatitude(), loc.getLongitude()) - station2.getDistance(loc.getLatitude(), loc.getLongitude());
						if (difference > 0)
						{
							return 1;
						}
						else if (difference < 0)
						{
							return -1;
						}
						else
						{
							return station1.getTransportationTypeId() - station2.getTransportationTypeId();
						}
					}
				});
				return stations;
			}
		}
		catch (Exception ex)
		{
		}
		return new ArrayList<Station>();
	}

	public void OnLocationChanged(Location location)
	{
		if (Utilities.isBetterLocation(location, _lastKnownLocation))
		{
			_lastKnownLocation = location;
			setupMyLocation();
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Object itemData = _listView.getItemAtPosition(position);
		if (itemData instanceof Station)
		{
			doRealTimeSearch((Station) itemData);
		}
	}

	private void doRealTimeSearch(Station station)
	{
		if (station != null)
		{
			Intent returnIntent = new Intent();
			returnIntent.putExtra(SLRealTime.INTENT_EXTRA_STATION, station.getId());
			returnIntent.putExtra(SLRealTime.INTENT_EXTRA_TRANSPORTATION_TYPE, station.getTransportationTypeId());
			setResult(RESULT_OK, returnIntent);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.close_to_me_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.realTimeView)
		{
			setResult(RESULT_CANCELED);
			finish();
		}
		else if (item.getItemId() == R.id.listView)
		{
			_currentView = R.id.listView;
			UserPreferences.getInstance().setCloseToMeView(_currentView);
			UserPreferences.getInstance().save();
			setupView();
			setupMyLocation();
		}
		else if (item.getItemId() == R.id.mapView)
		{
			_currentView = R.id.mapView;
			UserPreferences.getInstance().setCloseToMeView(_currentView);
			UserPreferences.getInstance().save();
			setupView();
			setupMyLocation();
		}
		return false;
	}

	private void setupView()
	{
		if (_currentView == R.id.listView)
		{
			_listView.setVisibility(View.VISIBLE);
			_mapView.setVisibility(View.GONE);
		}
		else if (_currentView == R.id.mapView)
		{
			_listView.setVisibility(View.GONE);
			_mapView.setVisibility(View.VISIBLE);
			_textView.setVisibility(View.GONE);
		}
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	public void onStationSelected(Station station)
	{
		doRealTimeSearch(station);
	}
}