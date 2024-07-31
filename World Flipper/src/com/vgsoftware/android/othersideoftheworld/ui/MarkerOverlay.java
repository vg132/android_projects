package com.vgsoftware.android.othersideoftheworld.ui;

import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.vgsoftware.android.othersideoftheworld.LocationUtility;
import com.vgsoftware.android.othersideoftheworld.R;
import com.vgsoftware.android.othersideoftheworld.MapUtility;

public class MarkerOverlay extends Overlay
{
	private GeoPoint _currentMarkerPosition = null;
	private Geocoder _geoCoder = null;

	public MarkerOverlay(Geocoder geoCoder)
	{
		_geoCoder = geoCoder;
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
	{
		if (_currentMarkerPosition != null)
		{
			Point screenPts = new Point();
			mapView.getProjection().toPixels(_currentMarkerPosition, screenPts);
			Bitmap bmp = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.red_dot);
			canvas.drawBitmap(bmp, screenPts.x - 5, screenPts.y - 32, null);
		}
		return super.draw(canvas, mapView, shadow, when);
	}

	@Override
	public boolean onTap(GeoPoint point, MapView mapView)
	{
		_currentMarkerPosition = point;
		try
		{
			List<Address> addresses = _geoCoder.getFromLocation(MapUtility.getLatitude(point), MapUtility.getLongitude(point), 1);
			String address = LocationUtility.getAddress(addresses);
			if (TextUtils.isEmpty(address))
			{
				address = mapView.getContext().getString(R.string.MapPromptLatitude) + " " + MapUtility.getLatitude(_currentMarkerPosition) + "\n" + mapView.getContext().getString(R.string.MapPromptLongitude) + " " + MapUtility.getLongitude(_currentMarkerPosition);
			}
			Toast.makeText(mapView.getContext(), address.trim(), Toast.LENGTH_LONG).show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return super.onTap(point, mapView);
	}

	public GeoPoint getCurrentPoint()
	{
		return _currentMarkerPosition;
	}

	public void setCurrentPoint(GeoPoint point)
	{
		_currentMarkerPosition = point;
	}
}
