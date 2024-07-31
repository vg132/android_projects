package com.vgsoftware.android.realtime.ui.widget.configuration;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.DatabaseHelper;
import com.vgsoftware.android.realtime.model.TrafficStatusSetting;
import com.vgsoftware.android.realtime.ui.widget.TrafficStatusProvider;

public class TrafficStatusConfiguration extends OrmLiteBaseActivity<DatabaseHelper>
{
	private int _appWidgetId;
	private DataStore _dataStore = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setResult(RESULT_CANCELED);
		setContentView(R.layout.widget_traffic_status_configuration);
		_dataStore = new DataStore(this);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null)
		{
			_appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		final TrafficStatusSetting setting = new TrafficStatusSetting();
		setting.setWidgetId(_appWidgetId);

		final ListView trafficTypeList = (ListView) findViewById(android.R.id.list);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.widget_traffic_status_traffic_types));
		trafficTypeList.setAdapter(adapter);
		trafficTypeList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		trafficTypeList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id)
			{
				CheckedTextView textView = (CheckedTextView) view;
				switch (position)
				{
					case 0:
						setting.setShowSubway(textView.isChecked());
						break;
					case 1:
						setting.setShowTrain(textView.isChecked());
						break;
					case 2:
						setting.setShowBus(textView.isChecked());
						break;
					case 3:
						setting.setShowTram(textView.isChecked());
						break;
					case 4:
						setting.setShowTram2(textView.isChecked());
						break;
					case 5:
						setting.setShowBoat(textView.isChecked());
						break;
				}

				textView.isChecked();
			}
		});

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_traffic_status_list);
		appWidgetManager.updateAppWidget(_appWidgetId, views);

		((Button) findViewById(android.R.id.button1)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!(setting.isShowBoat() || setting.isShowBus() || setting.isShowTram() ||
						setting.isShowTram2() || setting.isShowTrain() || setting.isShowSubway()))
				{
					Toast.makeText(getApplicationContext(), R.string.widget_traffic_status_configuration_none_selected, Toast.LENGTH_SHORT).show();
					return;
				}

				setting.setNextExecution(0);
				_dataStore.saveTrafficStatusSetting(setting);

				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
				TrafficStatusProvider.updateWidget(getApplicationContext(), appWidgetManager, _appWidgetId);

				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _appWidgetId);
				setResult(RESULT_OK, resultValue);
				finish();
			}
		});
		super.onCreate(savedInstanceState);
	}
}
