package com.vgsoftware.android.realtime.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vgsoftware.android.realtime.Constants;
import com.vgsoftware.android.realtime.DepartureComparator;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.Departure;
import com.vgsoftware.android.realtime.model.SiteSetting;
import com.vgsoftware.android.realtime.ui.DepartureActivity;
import com.vgsoftware.android.realtime.ui.adapters.DepartureAdapter;
import com.vgsoftware.android.vglib.CollectionsUtility;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DepartureFragment extends Fragment implements IDepartureInformationChangedListener, ITabSelected
{
	private List<Departure> _departures = null;
	private ListView _list = null;
	private int _transportationType = 0;
	private DepartureActivity _activity = null;
	private int _siteId = 0;
	private SwipeRefreshLayout _container = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (getArguments() != null && getArguments().containsKey(Constants.INTENT_EXTRA_DEPARTURE_LIST))
		{
			_siteId = getArguments().getInt(Constants.INTENT_EXTRA_SITE_ID);
			_transportationType = getArguments().getInt(Constants.INTENT_EXTRA_TRANSPORTATIOIN_TYPE);
			_departures = getArguments().getParcelableArrayList(Constants.INTENT_EXTRA_DEPARTURE_LIST);
		}
		_activity = (DepartureActivity) getActivity();
		if (_activity != null)
		{
			_activity.setOnDepartueInformationChangedListener(this);
		}
		saveTabSelection();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_departure, container, false);

		_container = (SwipeRefreshLayout) view.findViewById(R.id.container);
		_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				_activity.parseSite();
			}
		});
		_container.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		_list = (ListView) view.findViewById(android.R.id.list);
		if (_departures != null)
		{
			setDepartures(_departures);
		}
		return view;
	}

	public void setDepartures(List<Departure> departures)
	{
		Context context = getActivity();
		_departures = departures;
		if (_departures == null)
		{
			_departures = new ArrayList<Departure>();
		}
		if (context != null && _list != null)
		{
			Collections.sort(_departures, new DepartureComparator());

			Map<String, List<Departure>> groupedDepartures = new LinkedHashMap<String, List<Departure>>();
			for (Departure departure : _departures)
			{
				String groupName = getGroupName(departure);
				if (!groupedDepartures.containsKey(groupName))
				{
					groupedDepartures.put(groupName, new ArrayList<Departure>());
				}
				groupedDepartures.get(groupName).add(departure);
			}
			groupedDepartures = CollectionsUtility.asSortedKeyMap(groupedDepartures, new Comparator<String>()
			{
				@Override
				public int compare(String lhs, String rhs)
				{
					return lhs.compareTo(rhs);
				}
			});
			_list.setAdapter(new DepartureAdapter(context, groupedDepartures));
		}
	}

	@Override
	public void departureInformationUpdated(int transportationType, List<Departure> departures)
	{
		if (transportationType == _transportationType)
		{
			setDepartures(departures);
			if (_container != null)
			{
				_container.setRefreshing(false);
			}
		}
	}

	private String getGroupName(Departure departure)
	{
		if (departure.getTransportationType() == Constants.TRANSPORTATION_TYPE_BUS)
		{
			return departure.getStopAreaName();
		}
		else if (departure.getTransportationType() == Constants.TRANSPORTATION_TYPE_METRO)
		{
			if (departure.getGroupOfLineId() == 1)
			{
				return getString(R.string.departure_subway_green) + "|" + departure.getGroupOfLineId() + departure.getDirection();
			}
			else if (departure.getGroupOfLineId() == 2)
			{
				return getString(R.string.departure_subway_red) + "|" + departure.getGroupOfLineId() + departure.getDirection();
			}
			else if (departure.getGroupOfLineId() == 3)
			{
				return getString(R.string.departure_subway_blue) + "|" + departure.getGroupOfLineId() + departure.getDirection();
			}
		}
		else if (departure.getTransportationType() == Constants.TRANSPORTATION_TYPE_TRAIN)
		{
			return departure.getDirection() == 1 ? getString(R.string.departure_train_south) : getString(R.string.departure_train_north);
		}
		else if (departure.getTransportationType() == Constants.TRANSPORTATION_TYPE_TRAM)
		{
			return "Mot:|" + departure.getDirection();
		}
		return "";
	}

	@Override
	public void onTabSelected()
	{
		saveTabSelection();
	}

	private void saveTabSelection()
	{
		if (_siteId == 0)
		{
			return;
		}
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				LogManager.debug("Save selected tab. Site id: %d, Tab: %d", _siteId, _transportationType);
				DataStore dataStore = new DataStore(getActivity());
				SiteSetting siteSetting = dataStore.getSiteSetting(_siteId);
				if (siteSetting == null)
				{
					siteSetting = new SiteSetting(_siteId, _transportationType);
				}
				else
				{
					siteSetting.setSelectedTab(_transportationType);
				}
				dataStore.saveSiteSetting(siteSetting);
			}
		}).run();
	}
}
