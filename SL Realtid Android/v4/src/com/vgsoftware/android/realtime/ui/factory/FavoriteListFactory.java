package com.vgsoftware.android.realtime.ui.factory;

import java.util.List;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.Favorite;
import com.vgsoftware.android.realtime.search.StationContentProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class FavoriteListFactory implements RemoteViewsFactory
{
	public static final int FACTORY_ID = 10;

	private Context _context = null;
	private DataStore _dataStore = null;
	private List<Favorite> _stations = null;

	public FavoriteListFactory(Context context, Intent intent)
	{
		_context = context;
		_dataStore = new DataStore(context);
	}

	@Override
	public int getCount()
	{
		return _stations != null ? _stations.size() : 0;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public RemoteViews getLoadingView()
	{
		RemoteViews row = new RemoteViews(_context.getPackageName(), android.R.layout.simple_list_item_1);
		row.setTextViewText(android.R.id.text1, "Laddar");
		row.setTextColor(android.R.id.text1, Color.GRAY);
		return row;
	}

	@Override
	public RemoteViews getViewAt(int position)
	{
		RemoteViews row = new RemoteViews(_context.getPackageName(), android.R.layout.simple_list_item_1);
		Favorite station = _stations.get(position);
		row.setTextViewText(android.R.id.text1, station.getName());
		row.setTextColor(android.R.id.text1, Color.BLACK);
		row.setInt(android.R.id.text1, "setBackgroundResource", R.drawable.list_selector_background_light);

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(StationContentProvider.BuildViewUri(station));
		row.setOnClickFillInIntent(android.R.id.text1, intent);
		return row;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public void onCreate()
	{
	}

	@Override
	public void onDataSetChanged()
	{
		_stations = _dataStore.listFavorites();
	}

	@Override
	public void onDestroy()
	{
	}
}
