package com.vgsoftware.android.realtime.ui.fragments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Settings;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.Utilities;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.Station;
import com.vgsoftware.android.realtime.ui.SLRealTimeActivity;
import com.vgsoftware.android.vglib.location.LocationService;
import com.vgsoftware.android.vglib.location.OnLocationChangeListener;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CloseToMeFragment extends Fragment implements OnLocationChangeListener
{
	private final static String BundleKeyList = "__STATION_LIST__";
	private final static String BundleKeyDate = "__LAST_KNOWN_LOCATION_DATE__";

	private ListView _listView = null;
	private LocationService _locationService = null;
	private Location _lastKnownLocation = null;
	private Settings _settings = null;
	private List<Station> _stations = null;
	private DataStore _dataStore = null;
	private RelativeLayout _loadingContainer = null;
	private LinearLayout _contentContainer = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		_dataStore = new DataStore(getActivity().getApplicationContext());
		_settings = new Settings(getActivity().getApplicationContext());

		_locationService = new LocationService(getActivity().getApplicationContext(), this, true, _settings.getUseGPS());

		if (savedInstanceState != null &&
				savedInstanceState.containsKey(CloseToMeFragment.BundleKeyList) &&
				savedInstanceState.containsKey(CloseToMeFragment.BundleKeyDate))
		{
			_stations = savedInstanceState.getParcelableArrayList(CloseToMeFragment.BundleKeyList);
			_lastKnownLocation = savedInstanceState.getParcelable(CloseToMeFragment.BundleKeyDate);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_closetome, container, false);

		_loadingContainer = (RelativeLayout) view.findViewById(R.id.container_loading);
		_contentContainer = (LinearLayout) view.findViewById(android.R.id.content);
		_listView = (ListView) view.findViewById(android.R.id.list);
		registerForContextMenu(_listView);
		_listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
			{
				if (position >= 0 && position < _stations.size())
				{
					((SLRealTimeActivity) getActivity()).showDepartures(_stations.get(position));
				}
			}
		});

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo)
	{
		if (view.getId() == _listView.getId())
		{
			getActivity().getMenuInflater().inflate(R.menu.context_closetome, menu);
		}
		super.onCreateContextMenu(menu, view, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId())
		{
			case R.id.menu_view_times:
				if (info.position >= 0 && _stations.size() > info.position)
				{
					Station station = _stations.get(info.position);
					((SLRealTimeActivity) getActivity()).showDepartures(station);
					Tracking.sendView(getActivity(), "/CloseToMe/ViewDepartures");
					Tracking.sendEvent(getActivity(), "CloseToMe", "ViewDepartures", station.getName());
				}
				return true;
			case R.id.menu_view_on_map:
				if (info.position >= 0 && _stations.size() > info.position)
				{
					Station station = _stations.get(info.position);
					String url = "http://maps.google.com/maps?q=" + station.getName() + "@" + station.getLatitude() + "," + station.getLongitude();
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
					Tracking.sendView(getActivity(), "/CloseToMe/ViewOnMap");
					Tracking.sendEvent(getActivity(), "CloseToMe", "ViewOnMap", station.getName());
				}
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		_locationService.start();
		updateStationsList();
		Tracking.sendView(getActivity(), "/CloseToMe");
	}

	@Override
	public void onStop()
	{
		_locationService.stop();
		super.onStop();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		if (_stations != null)
		{
			outState.putParcelableArrayList(CloseToMeFragment.BundleKeyList, new ArrayList<Station>(_stations));
			outState.putParcelable(CloseToMeFragment.BundleKeyDate, _lastKnownLocation);
		}
	}

	@Override
	public void OnLocationChanged(Location location)
	{
		if (Utilities.isBetterLocation(location, _lastKnownLocation) && this.getActivity() != null)
		{
			_lastKnownLocation = location;
			_stations = _dataStore.listStations(location, 10000);
			if (_stations.size() > 10)
			{
				_stations = _stations.subList(0, 10);
			}
			this.getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					updateStationsList();
				}
			});
		}
	}

	private void updateStationsList()
	{
		if (_stations != null && _lastKnownLocation != null)
		{
			_contentContainer.setVisibility(View.VISIBLE);
			_loadingContainer.setVisibility(View.GONE);
			_listView.setAdapter(new ArrayAdapter<Station>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, _stations)
			{
				@Override
				public View getView(int position, View convertView, ViewGroup parent)
				{
					View view = super.getView(position, convertView, parent);
					Station station = getItem(position);
					if (station != null)
					{
						TextView textView = (TextView) view.findViewById(android.R.id.text2);
						String distance = new DecimalFormat("#.##").format(station.getDistanceInMeters(_lastKnownLocation) / 1000);
						textView.setText(String.format(getString(R.string.fragment_close_to_me_item_subheading), distance));
					}
					return view;
				}
			});
		}
	}
}
