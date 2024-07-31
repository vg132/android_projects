package com.vgsoftware.android.fastcheckin.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.vgsoftware.android.fastcheckin.AnalyticsTracker;
import com.vgsoftware.android.fastcheckin.LocationService;
import com.vgsoftware.android.fastcheckin.LogManager;
import com.vgsoftware.android.fastcheckin.OnLocationChangeListener;
import com.vgsoftware.android.fastcheckin.R;
import com.vgsoftware.android.fastcheckin.Settings;
import com.vgsoftware.android.fastcheckin.Utilities;
import com.vgsoftware.android.fastcheckin.dataabstraction.Place;
import com.vgsoftware.android.fastcheckin.facebook.BaseRequestListener;
import com.vgsoftware.android.fastcheckin.facebook.SessionEvents;
import com.vgsoftware.android.fastcheckin.facebook.SessionEvents.AuthListener;
import com.vgsoftware.android.fastcheckin.facebook.SessionEvents.LogoutListener;
import com.vgsoftware.android.fastcheckin.facebook.SessionStore;
import com.vgsoftware.android.fastcheckin.facebook.Utility;
import com.vgsoftware.android.fastcheckin.ui.controls.LoginButton;
import com.vgsoftware.android.fastcheckin.ui.controls.PlacesListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FastCheckIn extends Activity implements AdapterView.OnItemClickListener, OnLocationChangeListener
{
	// Constants
	private static final int DIALOG_PREFERENCES = 1000;

	// Class variables
	private Facebook _facebook = null;
	private AsyncFacebookRunner _asyncRunner = null;
	private AnalyticsTracker _tracker = null;
	private LocationService _locationService = null;
	private Location _lastKnownLocation = null;
	private Settings _settings = null;

	// Controls
	private LoginButton _logoutButton = null;
	private LoginButton _loginButton = null;
	private PlacesListView _placeListViewControl = null;
	private LinearLayout _loginLinearLayout = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		LogManager.info("FastCheckIn.onCreate");
		super.onCreate(savedInstanceState);

		_tracker = new AnalyticsTracker(this);
		if (!Utilities.isDebug())
		{
			_tracker.trackPageView("/appstart");
		}

		setContentView(R.layout.fastcheckin);

		_settings = new Settings(this);

		setupLocationService();
		bindControls();
		setupFacebook();

		_placeListViewControl.setOnItemClickListener(this);

		_logoutButton.init(this, _facebook, getResources().getStringArray(R.array.facebook_application_permissions));
		_loginButton.init(this, _facebook, getResources().getStringArray(R.array.facebook_application_permissions));

		_loginLinearLayout.setVisibility(_facebook.isSessionValid() ? View.GONE : View.VISIBLE);
		_placeListViewControl.setVisibility(!_facebook.isSessionValid() ? View.GONE : View.VISIBLE);
		_logoutButton.setVisibility(!_facebook.isSessionValid() ? View.GONE : View.VISIBLE);
	}

	private void setupLocationService()
	{
		if (_locationService == null)
		{
			_locationService = new LocationService(this, this, true, _settings.getUseGPS());
		}
		else
		{
			_locationService.setGPSEnabled(_settings.getUseGPS());
		}
	}

	private void setupFacebook()
	{
		_facebook = new Facebook(getString(R.string.facebook_application_id));
		_asyncRunner = new AsyncFacebookRunner(_facebook);

		SessionEvents.addAuthListener(new AuthListener()
		{
			public void onAuthSucceed()
			{
				LogManager.info("onCreate.onAuthSucceed");

				_loginLinearLayout.setVisibility(View.GONE);
				_placeListViewControl.setVisibility(View.VISIBLE);
				_logoutButton.setVisibility(View.VISIBLE);
				_locationService.start();
			}

			public void onAuthFail(String error)
			{
				LogManager.info("onCreate.onAuthFail - '" + error + "'");

				_loginLinearLayout.setVisibility(View.VISIBLE);
				_placeListViewControl.setVisibility(View.GONE);
				_logoutButton.setVisibility(View.GONE);
			}
		});

		SessionEvents.addLogoutListener(new LogoutListener()
		{
			public void onLogoutFinish()
			{
				LogManager.info("onCreate.onLogoutFinish");

				_loginLinearLayout.setVisibility(View.VISIBLE);
				_placeListViewControl.setVisibility(View.GONE);
				_logoutButton.setVisibility(View.GONE);
				_locationService.stop();
			}

			public void onLogoutBegin()
			{
				LogManager.info("onCreate.onLogoutBegin");
			}
		});
		SessionStore.restore(_facebook, this);
	}

	private void requestPlacesList()
	{
		LogManager.info("FastCheckIn.requestPlaceList");
		if (_lastKnownLocation != null && _facebook.isSessionValid())
		{
			Bundle params = new Bundle();
			params.putString("type", "place");
			params.putString("center", _lastKnownLocation.getLatitude() + "," + _lastKnownLocation.getLongitude());
			if (_lastKnownLocation.getAccuracy() <= 1000)
			{
				params.putString("distance", "1000");
			}
			else
			{
				params.putString("distance", "" + (int) (_lastKnownLocation.getAccuracy() + 250.0f));
			}

			_asyncRunner.request("search", params, new BaseRequestListener()
			{
				public void onComplete(final String response, Object state)
				{
					FastCheckIn.this.runOnUiThread(new Runnable()
					{
						public void run()
						{
							try
							{
								List<Place> items = new LinkedList<Place>();
								JSONObject data = Util.parseJson(response);
								JSONArray places = data.getJSONArray("data");
								for (int i = 0; i < places.length(); i++)
								{
									items.add(new Place(places.getJSONObject(i)));
								}
								_placeListViewControl.setPlaces(items);
								return;
							}
							catch (FacebookError ex)
							{
								LogManager.error("Facebook Error.", ex);
							}
							catch (JSONException ex)
							{
								LogManager.error("Unable to parse json places result from Facebook.", ex);
							}
							Toast.makeText(getApplicationContext(), getString(R.string.unable_to_list_places), Toast.LENGTH_LONG).show();
						}
					});
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		LogManager.info("FastCheckIn.onActivityResult");
		switch (requestCode)
		{
		case Facebook.DEFAULT_AUTH_ACTIVITY_CODE:
			_facebook.authorizeCallback(requestCode, resultCode, data);
			break;
		case FastCheckIn.DIALOG_PREFERENCES:
			setupLocationService();
			break;
		}

	}

	@Override
	protected void onStop()
	{
		LogManager.info("FastCheckIn.onStop");

		_locationService.stop();

		super.onStop();
	}

	@Override
	protected void onStart()
	{
		LogManager.info("FastCheckIn.onStart");

		if (_facebook.isSessionValid())
		{
			_locationService.start();
		}
		else
		{
			try
			{
				_facebook.logout(this);
			}
			catch (Exception ex)
			{
				LogManager.error("Unable to logout from Facebook", ex);
			}
		}
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		LogManager.info("FastCheckIn.onResume");
		super.onResume();
	}

	private void bindControls()
	{
		LogManager.info("FastCheckIn.bindControls");

		_logoutButton = (LoginButton) findViewById(R.id.LogoutButton);
		_logoutButton.setVisibility(View.GONE);

		_loginButton = (LoginButton) findViewById(R.id.LoginButton);

		_placeListViewControl = (PlacesListView) findViewById(R.id.PlacesListViewControl);
		_placeListViewControl.setVisibility(View.GONE);

		_loginLinearLayout = (LinearLayout) findViewById(R.id.LoginLinearLayout);
		_loginLinearLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		final Place place = _placeListViewControl.getPlaceAtPosition(position);
		if (place == null)
		{
			Toast.makeText(this, getString(R.string.error_message_no_place_selected), Toast.LENGTH_LONG).show();
			return;
		}
		if (_lastKnownLocation == null)
		{
			Toast.makeText(this, getString(R.string.error_message_no_location), Toast.LENGTH_LONG).show();
			return;
		}
		if (place != null && _lastKnownLocation != null)
		{
			if (_settings.getShowMessage())
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);

				alert.setTitle(getString(R.string.dialog_message_title));
				alert.setMessage(getString(R.string.dialog_message_content));

				// Set an EditText view to get user input
				final EditText input = new EditText(this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						checkIn(place, input.getText().toString());
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						// Canceled.
					}
				});

				alert.show();
			}
			else
			{
				checkIn(place, null);
			}
		}
	}

	private void checkIn(Place place, String message)
	{
		if (Utility.checkIn(_facebook, place, _lastKnownLocation, message))
		{
			Toast.makeText(FastCheckIn.this, String.format(getString(R.string.success_message_check_in), place.getName()), Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(FastCheckIn.this, String.format(getString(R.string.error_message_check_in), place.getName()), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void OnLocationChanged(Location location)
	{
		if (location != null)
		{
			LogManager.info("FastCheckIn.OnLocationChanged - Location: " + location.getLatitude() + ", " + location.getLongitude());
		}
		else
		{
			LogManager.info("FastCheckIn.OnLocationChanged - Location: null");
		}
		if (Utilities.isBetterLocation(location, _lastKnownLocation))
		{
			_lastKnownLocation = location;
			requestPlacesList();
		}
	}

	// Context menu

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fastcheckin_options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MenuItem item = menu.findItem(R.id.FastCheckIn_Menu_Refresh);
		item.setEnabled(_facebook.isSessionValid());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.FastCheckIn_Menu_Refresh:
			_locationService.restart();
			break;
		case R.id.FastCheckIn_Menu_Preferences:
			Intent preferencesIntent = new Intent(FastCheckIn.this, Preferences.class);
			startActivityForResult(preferencesIntent, FastCheckIn.DIALOG_PREFERENCES);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}