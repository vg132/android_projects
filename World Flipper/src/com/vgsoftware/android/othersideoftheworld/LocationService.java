package com.vgsoftware.android.othersideoftheworld;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationService implements LocationListener
{
	private Context _context;
	private boolean _gpsEnabled;
	private boolean _networkEnabled;
	private LocationManager _locationManager;
	private Location _currentGPSLocation;
	private Location _currentNetworkLocation;
	private OnLocationChangeListener _locationChangedListener;
	private boolean _running = false;

	private int _updateIntervalDistance = 100;
	private int _updateIntervalTime = 1000 * 60 * 5;

	public LocationService(Context context, boolean networkEnabled, boolean gpsEnabled)
	{
		_context = context;
		_networkEnabled = networkEnabled;
		_gpsEnabled = gpsEnabled;
		_locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public LocationService(Context context, OnLocationChangeListener locationChangeListener, boolean networkEnabled, boolean gpsEnabled)
	{
		_context = context;
		_locationChangedListener = locationChangeListener;
		_networkEnabled = networkEnabled;
		_gpsEnabled = gpsEnabled;
		_locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean getGPSEnabled()
	{
		return _gpsEnabled;
	}

	public void setGPSEnabled(boolean gpsEnabled)
	{
		boolean restart = gpsEnabled != _gpsEnabled;
		_gpsEnabled = gpsEnabled;
		if (_running && restart)
		{
			restart();
		}
	}

	public boolean getNetworkEnabled()
	{
		return _networkEnabled;
	}

	public void setNetworkEnabled(boolean networkEnabled)
	{
		boolean restart = networkEnabled != _networkEnabled;
		_networkEnabled = networkEnabled;
		if (_running && restart)
		{
			restart();
		}
	}

	public int getUpdateIntervalTime()
	{
		return _updateIntervalTime;
	}

	public void setUpdateIntervalTime(int seconds)
	{
		_updateIntervalTime = seconds;
	}

	public int getUpdateIntervalDistance()
	{
		return _updateIntervalDistance;
	}

	public void setUpdateIntervalDistance(int meters)
	{
		_updateIntervalDistance = meters;
	}

	public Location getCurrentLocation()
	{
		if (_currentGPSLocation != null)
		{
			return _currentGPSLocation;
		}
		return _currentNetworkLocation;
	}

	private void OnLocationChanged()
	{
		if (_locationChangedListener != null)
		{
			_locationChangedListener.OnLocationChanged(getCurrentLocation());
		}
	}
	
	public void setOnLocationChangeListener(OnLocationChangeListener listener)
	{
		_locationChangedListener = listener;
	}

	public void restart()
	{
		stop();
		start();
	}

	public void start()
	{
		if (_running)
		{
			stop();
		}
		if (getNetworkEnabled() && _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, getUpdateIntervalTime(), getUpdateIntervalDistance(), this);
			_currentNetworkLocation = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (_currentNetworkLocation != null && (_currentNetworkLocation.getTime() - System.currentTimeMillis()) > 600000)
			{
				_currentNetworkLocation = null;
			}
		}
		if (getGPSEnabled() && _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, getUpdateIntervalTime(), getUpdateIntervalDistance(), this);
			_currentGPSLocation = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (_currentGPSLocation != null && (_currentGPSLocation.getTime() - System.currentTimeMillis()) > 600000)
			{
				_currentGPSLocation = null;
			}
		}
		_running = true;
	}

	public void stop()
	{
		_locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER))
		{
			_currentNetworkLocation = location;
			OnLocationChanged();
		}
		else if (location.getProvider().equals(LocationManager.GPS_PROVIDER))
		{
			_currentGPSLocation = location;
			OnLocationChanged();
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}

	@Override
	public void onProviderEnabled(String provider)
	{
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		if (LocationManager.NETWORK_PROVIDER.equals(provider))
		{
			_currentNetworkLocation = null;
		}
		else if (LocationManager.GPS_PROVIDER.equals(provider))
		{
			_currentGPSLocation = null;
		}
	}
}
