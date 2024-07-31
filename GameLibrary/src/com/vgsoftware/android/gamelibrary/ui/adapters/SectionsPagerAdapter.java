package com.vgsoftware.android.gamelibrary.ui.adapters;

import java.util.List;

import com.vgsoftware.android.gamelibrary.model.DataStore;
import com.vgsoftware.android.gamelibrary.model.IOnDataChanged;
import com.vgsoftware.android.gamelibrary.model.Platform;
import com.vgsoftware.android.gamelibrary.ui.fragments.EmptyListFragment;
import com.vgsoftware.android.gamelibrary.ui.fragments.GameListFragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter implements IOnDataChanged
{
	private List<Platform> _platforms = null;
	private Context _context = null;

	public SectionsPagerAdapter(FragmentManager fm, Context context)
	{
		super(fm);
		_context = context;
		_platforms = DataStore.getInstance().listUsedPlatforms();
		DataStore.getInstance().registerOnDataChangedListener(this);
	}

	@Override
	public Fragment getItem(int position)
	{
		if (_platforms.size() > 0)
		{
			return new GameListFragment(_platforms.get(position));
		}
		return new EmptyListFragment();
	}

	@Override
	public int getCount()
	{
		int items = _platforms.size();
		if (items > 0)
		{
			return items;
		}
		return 1;
	}

	@Override
	public long getItemId(int position)
	{
		if (_platforms.size() > 0)
		{
			return _platforms.get(position).getId();
		}
		return -1;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		if (_platforms.size() > 0)
		{
			return _platforms.get(position).getName();
		}
		return "Empty";
	}

	@Override
	public void onDataChanged()
	{
		_platforms = DataStore.getInstance().listUsedPlatforms();
		((Activity) _context).runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				notifyDataSetChanged();
			}
		});
	}
}
