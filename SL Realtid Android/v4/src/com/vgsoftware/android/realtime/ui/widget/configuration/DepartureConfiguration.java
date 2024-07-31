package com.vgsoftware.android.realtime.ui.widget.configuration;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.vgsoftware.android.realtime.Constants;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.DatabaseHelper;
import com.vgsoftware.android.realtime.model.DepartureSetting;
import com.vgsoftware.android.realtime.ui.adapters.StationListAdapter;
import com.vgsoftware.android.realtime.ui.widget.DepartureProvider;

public class DepartureConfiguration extends OrmLiteBaseActivity<DatabaseHelper>
{
	private int _appWidgetId;
	private DataStore _dataStore = null;
	private AutoCompleteTextView _stationTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setResult(RESULT_CANCELED);
		setContentView(R.layout.widget_departure_configuration);
		_dataStore = new DataStore(this);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null)
		{
			_appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_traffic_status_list);
		appWidgetManager.updateAppWidget(_appWidgetId, views);

		_stationTextView = (AutoCompleteTextView) findViewById(android.R.id.edit);
		final Spinner _transportationTypeSpinner = (Spinner) findViewById(android.R.id.list);
		_transportationTypeSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.widget_departure_transportation_types, android.R.layout.simple_spinner_dropdown_item));
		final DepartureSetting setting = new DepartureSetting();
		setting.setAutoUpdate(false);
		final CheckedTextView autoUpdateCheckBox = (CheckedTextView) findViewById(android.R.id.checkbox);
		autoUpdateCheckBox.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				autoUpdateCheckBox.toggle();
				setting.setAutoUpdate(autoUpdateCheckBox.isChecked());
			}
		});

		((Button) findViewById(android.R.id.button1)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String stationName = _stationTextView.getText().toString();
				Integer siteId;
				try
				{
					siteId = new AsyncTask<String, Void, Integer>()
					{
						@Override
						protected Integer doInBackground(String... params)
						{
							Integer siteId = null;
							Cursor cursor = getApplicationContext().getContentResolver().query(Uri.parse("content://com.vgsoftware.android.realtime.search.StationContentProvider/search"), null, null, new String[] { params[0] }, null);
							if (cursor.moveToNext())
							{
								siteId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
							}
							return siteId;
						}
					}.execute(stationName).get();

					if (siteId != null)
					{
						setting.setWidgetId(_appWidgetId);
						setting.setSiteId(siteId);
						switch (_transportationTypeSpinner.getSelectedItemPosition())
						{
							case 0:
								setting.setTransportationType(Constants.TRANSPORTATION_TYPE_TRAIN);
								break;
							case 1:
								setting.setTransportationType(Constants.TRANSPORTATION_TYPE_METRO);
								break;
							case 2:
								setting.setTransportationType(Constants.TRANSPORTATION_TYPE_TRAM);
								break;
							case 3:
								setting.setTransportationType(Constants.TRANSPORTATION_TYPE_BUS);
								break;
						}

						_dataStore.saveDepartureSetting(setting);

						AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
						DepartureProvider.updateWidget(getApplicationContext(), appWidgetManager, _appWidgetId);

						Intent resultValue = new Intent();
						resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _appWidgetId);
						setResult(RESULT_OK, resultValue);
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), R.string.widget_departure_configuration_station_not_found, Toast.LENGTH_SHORT).show();
					}
				}
				catch (Exception e)
				{
					LogManager.error("Unable to configure departure widget", e);
					Toast.makeText(getApplicationContext(), R.string.widget_departure_configuration_station_not_found, Toast.LENGTH_SHORT).show();
				}
			}
		});
		StationListAdapter adapter = new StationListAdapter(this, null, 0);
		_stationTextView = (AutoCompleteTextView) findViewById(android.R.id.edit);
		_stationTextView.setAdapter(adapter);
		super.onCreate(savedInstanceState);
	}
}
