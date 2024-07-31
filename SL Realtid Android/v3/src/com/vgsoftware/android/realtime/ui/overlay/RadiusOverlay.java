package com.vgsoftware.android.realtime.ui.overlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.vgsoftware.android.vglib.MapUtility;

public class RadiusOverlay extends Overlay
{
	private GeoPoint _centerPoint = null;
	private int _radiusInMeters;
	private Paint _fillPaint = null;
	private Paint _strokePaint = null;

	public RadiusOverlay()
	{
		setupPaint();
	}

	private void setupPaint()
	{
		_fillPaint = new Paint();
		_fillPaint.setAntiAlias(true);
		_fillPaint.setAlpha(10);
		_fillPaint.setColor(Color.argb(10, 0, 0, 255));
		_fillPaint.setStyle(Style.FILL);

		_strokePaint = new Paint();
		_strokePaint.setAntiAlias(true);
		_strokePaint.setAlpha(200);
		_strokePaint.setColor(Color.argb(200, 155, 155, 155));
		_strokePaint.setStrokeWidth(2.0f);
		_strokePaint.setStyle(Style.STROKE);
	}

	public void setRadius(GeoPoint centerPoint, int radiusInMeters)
	{
		_centerPoint = centerPoint;
		_radiusInMeters = radiusInMeters;
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
	{
		if (_centerPoint != null)
		{
			Point center = new Point();
			mapView.getProjection().toPixels(_centerPoint, center);
			int radius = MapUtility.metersToRadius(_radiusInMeters, mapView, _centerPoint);
			canvas.drawCircle(center.x, center.y, radius, _fillPaint);
			canvas.drawCircle(center.x, center.y, radius, _strokePaint);
			mapView.invalidate();
		}
		return super.draw(canvas, mapView, shadow, when);
	}
}
