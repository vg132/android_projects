package com.vgsoftware.android.othersideoftheworld.ui;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.vgsoftware.android.othersideoftheworld.LocationService;
import com.vgsoftware.android.othersideoftheworld.LocationUtility;
import com.vgsoftware.android.othersideoftheworld.OnLocationChangeListener;
import com.vgsoftware.android.othersideoftheworld.R;
import com.vgsoftware.android.othersideoftheworld.MapUtility;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Main extends MapActivity implements OnLocationChangeListener
{
	private static final int FLIP_WORLD_BOTH = 1001;
	private static final int FLIP_WORLD_LONGITUDE = 1002;
	private static final int FLIP_WORLD_LATITUDE = 1003;

	private MapView _mapView = null;
	private MarkerOverlay _markerOverlay = null;
	private Geocoder _geoCoder = null;
	private LocationService _locationService = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		_geoCoder = new Geocoder(this, Locale.getDefault());

		_mapView = (MapView) findViewById(R.id.MainMapView);
		_mapView.setBuiltInZoomControls(true);

		_markerOverlay = new MarkerOverlay(_geoCoder);
		List<Overlay> listOfOverlays = _mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(_markerOverlay);
		_mapView.invalidate();

		_locationService = new LocationService(this, true, true);
	}

	@Override
	protected void onStart()
	{
		_locationService.start();
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		_locationService.stop();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.MenuItemFlipWorld)
		{
			flipWorldMap(Main.FLIP_WORLD_BOTH);
		}
		else if (item.getItemId() == R.id.MenuItemMyLocation)
		{
			gotoMyLocation();
		}
		else if (item.getItemId() == R.id.MenuItemFlipLatitudeId)
		{
			flipWorldMap(Main.FLIP_WORLD_LATITUDE);
		}
		else if (item.getItemId() == R.id.MenuItemFLipLongitudeId)
		{
			flipWorldMap(Main.FLIP_WORLD_LONGITUDE);
		}
		return super.onOptionsItemSelected(item);
	}

	private void gotoMyLocation()
	{
		if (_locationService != null)
		{
			Location currentLocation = _locationService.getCurrentLocation();
			GeoPoint point = MapUtility.getPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
			_markerOverlay.setCurrentPoint(point);
			_mapView.getController().animateTo(point);
			Toast.makeText(this, "Your current location is based on '" + currentLocation.getProvider().toUpperCase() + "' provider", Toast.LENGTH_LONG).show();
			showLocationText(point);
		}
	}

	private void flipWorldMap(int type)
	{
		GeoPoint center = _markerOverlay.getCurrentPoint();
		if (center == null)
		{
			center = _mapView.getMapCenter();
		}

		double longitude = MapUtility.getLongitude(center);
		double latitude = MapUtility.getLatitude(center);

		if (type == Main.FLIP_WORLD_BOTH || type == Main.FLIP_WORLD_LATITUDE)
		{
			latitude = -1 * latitude;
		}
		if (type == Main.FLIP_WORLD_BOTH || type == Main.FLIP_WORLD_LONGITUDE)
		{
			if (longitude >= 0)
			{
				longitude -= 180;
			}
			else
			{
				longitude += 180;
			}
		}
		center = MapUtility.getPoint(latitude, longitude);
		showLocationText(center);
		_mapView.getController().animateTo(center);
		_markerOverlay.setCurrentPoint(center);
	}

	private void showLocationText(GeoPoint point)
	{
		try
		{
			List<Address> addresses = _geoCoder.getFromLocation(MapUtility.getLatitude(point), MapUtility.getLongitude(point), 1);
			String address = LocationUtility.getAddress(addresses);
			if (TextUtils.isEmpty(address))
			{
				address = getString(R.string.MapPromptLatitude) + " " + MapUtility.getLatitude(point) + "\n" + getString(R.string.MapPromptLongitude) + " " + MapUtility.getLongitude(point);
			}
			Toast.makeText(this, address.trim(), Toast.LENGTH_LONG).show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	@Override
	public void OnLocationChanged(Location location)
	{
	}
}