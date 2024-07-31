package com.vgsoftware.android.realtime.ui.fragments;

import java.util.List;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.Favorite;
import com.vgsoftware.android.realtime.model.IOnDataChanged;
import com.vgsoftware.android.realtime.model.ISite;
import com.vgsoftware.android.realtime.model.Site;
import com.vgsoftware.android.realtime.ui.SLRealTimeActivity;

import android.app.Fragment;
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
import android.widget.ListView;
import android.widget.TextView;

public class FavoritesFragment extends Fragment implements IOnDataChanged
{
	private DataStore _dataStore = null;
	private ListView _listView = null;
	private TextView _headingTextView = null;
	private boolean _favoriteList = false;

	public FavoritesFragment()
	{
		_dataStore = new DataStore(getActivity());
		_dataStore.addOnDataChangedListener(this);
	}

	@Override
	public void onStart()
	{
		Tracking.sendView(getActivity(), "/Favorites");
		super.onStart();
	}

	@Override
	public void onResume()
	{
		dataChanged();
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_favorites, container, false);

		_listView = (ListView) view.findViewById(android.R.id.list);
		_headingTextView = (TextView) view.findViewById(android.R.id.text1);

		registerForContextMenu(_listView);
		_listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
			{
				ISite selectedItem = (ISite) _listView.getAdapter().getItem(position);
				((SLRealTimeActivity) getActivity()).showDepartures(selectedItem);
			}
		});
		dataChanged();
		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo)
	{
		if (view.getId() == _listView.getId() && _favoriteList)
		{
			getActivity().getMenuInflater().inflate(R.menu.context_favorites, menu);
		}
		super.onCreateContextMenu(menu, view, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Favorite favorite = (Favorite) _listView.getAdapter().getItem(info.position);
		switch (item.getItemId())
		{
			case R.id.menu_view_times:
				if (favorite != null)
				{
					((SLRealTimeActivity) getActivity()).showDepartures(favorite);
					Tracking.sendView(getActivity(), "/Favorites/View");
					Tracking.sendEvent(getActivity(), "Favorite", "View", favorite.getName());
				}
				break;
			case R.id.menu_delete:
				if (favorite != null)
				{
					_dataStore.deleteFavorite((Favorite) _listView.getAdapter().getItem(info.position));
					Tracking.sendView(getActivity(), "/Favorites/Delete");
					Tracking.sendEvent(getActivity(), "Favorite", "Delete", favorite.getName());
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void dataChanged()
	{
		List<Favorite> favorites = _dataStore.listFavorites();
		if (favorites.size() > 0)
		{
			_favoriteList = true;
			_headingTextView.setText(R.string.tab_favorites);
			_listView.setAdapter(new ArrayAdapter<Favorite>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, favorites));
		}
		else
		{
			_headingTextView.setText(R.string.fragment_favorites_searches);
			List<Site> sites = _dataStore.listSitesByLastSearch();
			if (sites.size() > 0)
			{
				_listView.setAdapter(new ArrayAdapter<Site>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, sites));
			}
		}
	}
}
