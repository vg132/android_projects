package com.vgsoftware.android.realtime.ui.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.model.TrafficStatus;
import com.vgsoftware.android.realtime.parse.ITrafficStatusParsedListener;
import com.vgsoftware.android.realtime.parse.TrafficStatusParser;
import com.vgsoftware.android.realtime.ui.adapters.TrafficStatusAdapter;
import com.vgsoftware.android.vglib.NetworkUtility;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TrafficStatusFragment extends Fragment implements ITrafficStatusParsedListener
{
	private final static String BundleKeyList = "TrafficStatus";
	private final static String BundleKeyDate = "TrafficStatusDate";
	@SuppressLint("SimpleDateFormat")
	private final static SimpleDateFormat TrafficStatusDateFormat = new SimpleDateFormat("HH:mm");

	private TextView _textView = null;
	private ExpandableListView _listView = null;
	private TrafficStatusParser _parser = null;
	private Context _context = null;
	private List<TrafficStatus> _trafficStatus = null;
	private Date _trafficStatusDate = null;
	private ScheduledExecutorService _scheduleTaskExecutor = null;
	private RelativeLayout _loadingContainer = null;
	private LinearLayout _contentContainer = null;
	private RelativeLayout _noNetworkContainer = null;

	public TrafficStatusFragment()
	{
		_parser = new TrafficStatusParser();
		_parser.setOnTrafficStatusParsedListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null &&
				savedInstanceState.containsKey(TrafficStatusFragment.BundleKeyList) &&
				savedInstanceState.containsKey(TrafficStatusFragment.BundleKeyDate))
		{
			_trafficStatus = savedInstanceState.getParcelableArrayList(TrafficStatusFragment.BundleKeyList);
			_trafficStatusDate = new Date(savedInstanceState.getLong(TrafficStatusFragment.BundleKeyDate));
		}
		_scheduleTaskExecutor = Executors.newScheduledThreadPool(2);

		_scheduleTaskExecutor.scheduleAtFixedRate(new Runnable()
		{
			public void run()
			{
				_parser.parseTrafficStatusAsync();
			}
		}, 0, 1, TimeUnit.MINUTES);
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		if (_trafficStatus != null && _trafficStatusDate != null)
		{
			outState.putParcelableArrayList(TrafficStatusFragment.BundleKeyList, (ArrayList<TrafficStatus>) _trafficStatus);
			outState.putLong(TrafficStatusFragment.BundleKeyDate, _trafficStatusDate.getTime());
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		updateListView();
		Tracking.sendView(getActivity(), "/TrafficStatus");
	}

	@Override
	public void onDestroy()
	{
		_scheduleTaskExecutor.shutdown();
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		_context = getActivity().getApplicationContext();

		View view = inflater.inflate(R.layout.fragment_trafficstatus, container, false);
		_loadingContainer = (RelativeLayout) view.findViewById(R.id.container_loading);
		_noNetworkContainer = (RelativeLayout) view.findViewById(R.id.container_no_network);
		_contentContainer = (LinearLayout) view.findViewById(android.R.id.content);
		_listView = (ExpandableListView) view.findViewById(android.R.id.list);
		_textView = (TextView) view.findViewById(android.R.id.text1);

		_parser.parseTrafficStatusAsync();

		return view;
	}

	@Override
	public void trafficStatusParsed(final List<TrafficStatus> trafficStatus)
	{
		if (this.getActivity() != null)
		{
			this.getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					_trafficStatus = trafficStatus;
					_trafficStatusDate = new Date(System.currentTimeMillis());
					updateListView();
				}
			});
		}
	}

	private void updateListView()
	{
		if (_trafficStatusDate != null && _trafficStatus != null && _textView != null && _listView != null)
		{
			_contentContainer.setVisibility(View.VISIBLE);
			_loadingContainer.setVisibility(View.GONE);
			_noNetworkContainer.setVisibility(View.GONE);
			_textView.setText(String.format(getString(R.string.fragment_traffic_status_heading), TrafficStatusDateFormat.format(_trafficStatusDate)));
			_listView.setAdapter(new TrafficStatusAdapter(_context, _trafficStatus));
		}
		else if (!NetworkUtility.isOnline(getActivity()))
		{
			_contentContainer.setVisibility(View.GONE);
			_loadingContainer.setVisibility(View.GONE);
			_noNetworkContainer.setVisibility(View.VISIBLE);
		}
	}
}
