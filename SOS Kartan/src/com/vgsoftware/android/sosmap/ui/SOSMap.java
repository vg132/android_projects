package com.vgsoftware.android.sosmap.ui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.vgsoftware.android.sosmap.Event;
import com.vgsoftware.android.sosmap.EventStore;
import com.vgsoftware.android.sosmap.R;
import com.vgsoftware.android.sosmap.Utilities;
import com.vgsoftware.android.sosmap.ui.adapters.EventAdapter;
import com.vgsoftware.android.sosmap.ui.overlay.EventOverlay;
import com.vgsoftware.android.sosmap.ui.overlay.EventOverlayItem;
import com.vgsoftware.android.vglib.MapUtility;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

public class SOSMap extends MapActivity implements OnItemSelectedListener, OnItemClickListener
{
	private static final int NEW_ITEMS_NOTIFICATION_ID = 100;

	private MapView _mapView = null;
	private Spinner _countyList = null;
	private ListView _eventList = null;
	private ScheduledExecutorService _scheduleTaskExecutor = null;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		_eventList = (ListView) findViewById(R.id.eventListView);
		_eventList.setOnItemClickListener(this);
		LinearLayout container = (LinearLayout) findViewById(R.id.mapContainer);

		if (Utilities.isDebug(this))
		{
			_mapView = new MapView(this, getString(R.string.apiKeyGoogleMapsDebug));
		}
		else
		{
			_mapView = new MapView(this, getString(R.string.apiKeyGoogleMapsRelease));
		}
		_mapView.setClickable(true);
		container.addView(_mapView);

		_countyList = (Spinner) findViewById(R.id.countyList);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countyNames, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_countyList.setAdapter(adapter);
		_countyList.setOnItemSelectedListener(this);
		_scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
		_scheduleTaskExecutor.scheduleAtFixedRate(new Runnable()
		{
			public void run()
			{
				final int newEvents = EventStore.update();
				runOnUiThread(new Runnable()
				{
					public void run()
					{
						setupMapOverlays();
						fillEventList();
						if (newEvents > 0)
						{
							showNotification(newEvents);
						}
					}
				});
			}
		}, 0, 5 * 60, TimeUnit.SECONDS);
	}

	private void fillEventList()
	{
		EventAdapter adapter = new EventAdapter(this, R.layout.list_row, EventStore.getEvents());
		_eventList.setAdapter(adapter);
	}

	private void showNotification(int items)
	{
		if (!Utilities.isAppOnForeground(this))
		{
			String title = null;
			String description = null;
			if (items > 1)
			{
				title = Utilities.stringFormat(getString(R.string.notificationTitle), items);
			}
			else
			{
				title = Utilities.stringFormat(getString(R.string.notificationTitleSingular), items);
			}

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
			notification.number = items;

			//notification.defaults |= Notification.DEFAULT_LIGHTS;
			//notification.defaults |= Notification.DEFAULT_VIBRATE;
			
			//notification.defaults |= Notification.DEFAULT_SOUND;
			//notification.defaults |= Notification.DEFAULT_VIBRATE;

			notification.ledARGB = 0xff00ff00;
			notification.ledOnMS = 300;
			notification.ledOffMS = 1000;
			//notification.defaults |= Notification.FLAG_SHOW_LIGHTS;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			Intent notificationIntent = new Intent(this, SOSMap.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

			notification.setLatestEventInfo(getApplicationContext(), title, description, contentIntent);
			notificationManager.notify(SOSMap.NEW_ITEMS_NOTIFICATION_ID, notification);
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		_scheduleTaskExecutor.shutdownNow();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menuListView:
			_mapView.setVisibility(View.GONE);
			_countyList.setVisibility(View.GONE);
			_eventList.setVisibility(View.VISIBLE);
			return true;
		case R.id.menuMapView:
			_mapView.setVisibility(View.VISIBLE);
			_countyList.setVisibility(View.VISIBLE);
			_eventList.setVisibility(View.GONE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupMapOverlays()
	{
		Drawable marker = getResources().getDrawable(R.drawable.marker);
		EventOverlay eventOverlay = new EventOverlay(marker, _mapView);

		for (Event event : EventStore.getEvents())
		{
			GeoPoint point = MapUtility.getPoint(event.getLatitude(), event.getLongitude());
			eventOverlay.addOverlay(new EventOverlayItem(point, event));
		}

		_mapView.getOverlays().clear();
		_mapView.getOverlays().add(eventOverlay);
		_mapView.invalidate();
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		switch (position)
		{
		case 0:
			_mapView.getController().setZoom(6);
			_mapView.getController().setCenter(MapUtility.getPoint(63.2, 14.8));
			break;
		case 1:
			_mapView.getController().setZoom(10);
			_mapView.getController().setCenter(MapUtility.getPoint(59.355596, 18.056030));
			break;
		case 2:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(59.875155, 17.880249));
			break;
		case 3:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(59.071625, 16.463013));
			break;
		case 4:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(58.464975, 15.798340));
			break;
		case 5:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(57.545313, 14.436035));
			break;
		case 6:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(57.136239, 14.611816));
			break;
		case 7:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(57.142200, 16.386108));
			break;
		case 8:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(57.459726, 18.484497));
			break;
		case 9:
			_mapView.getController().setZoom(10);
			_mapView.getController().setCenter(MapUtility.getPoint(56.292157, 15.161133));
			break;
		case 10:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(55.866065, 13.645020));
			break;
		case 11:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(56.827939, 12.793579));
			break;
		case 12:
			_mapView.getController().setZoom(8);
			_mapView.getController().setCenter(MapUtility.getPoint(57.580669, 12.535400));
			break;
		case 13:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(59.670515, 14.117432));
			break;
		case 14:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(59.192812, 15.012817));
			break;
		case 15:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(59.609433, 16.182861));
			break;
		case 16:
			_mapView.getController().setZoom(9);
			_mapView.getController().setCenter(MapUtility.getPoint(60.659723, 14.584351));
			break;
		case 17:
			_mapView.getController().setZoom(8);
			_mapView.getController().setCenter(MapUtility.getPoint(60.871661, 16.430054));
			break;
		case 18:
			_mapView.getController().setZoom(8);
			_mapView.getController().setCenter(MapUtility.getPoint(62.830073, 17.907715));
			break;
		case 19:
			_mapView.getController().setZoom(8);
			_mapView.getController().setCenter(MapUtility.getPoint(63.223730, 14.930420));
			break;
		case 20:
			_mapView.getController().setZoom(8);
			_mapView.getController().setCenter(MapUtility.getPoint(64.783488, 21.093750));
			break;
		case 21:
			_mapView.getController().setZoom(7);
			_mapView.getController().setCenter(MapUtility.getPoint(66.739902, 19.731445));
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		final Event event = (Event) _eventList.getItemAtPosition(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(event.getHeading()).setMessage(event.toString()).setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		}).setNeutralButton("Visa på kartan", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				_mapView.getController().setZoom(15);
				_mapView.getController().setCenter(MapUtility.getPoint(event.getLatitude(), event.getLongitude()));
				_mapView.setVisibility(View.VISIBLE);
				_countyList.setVisibility(View.VISIBLE);
				_eventList.setVisibility(View.GONE);
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}