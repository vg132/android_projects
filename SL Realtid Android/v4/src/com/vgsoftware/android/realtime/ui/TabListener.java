package com.vgsoftware.android.realtime.ui;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.ui.fragments.ITabSelected;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class TabListener<T extends Fragment> implements ActionBar.TabListener
{
	private final Activity _activity;
	private final String _tag;
	private final Class<T> _fragmentClass;
	private final Bundle _args;
	private Fragment _fragment;

	public TabListener(Activity activity, String tag, Class<T> fragmentClass)
	{
		this(activity, tag, fragmentClass, null);
	}

	public TabListener(Activity activity, String tag, Class<T> fragmentClass, Bundle args)
	{
		_activity = activity;
		_tag = tag;
		_fragmentClass = fragmentClass;
		_args = args;

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		_fragment = _activity.getFragmentManager().findFragmentByTag(_tag);
		if (_fragment != null && !_fragment.isDetached())
		{
			FragmentTransaction ft = _activity.getFragmentManager().beginTransaction();
			ft.detach(_fragment);
			ft.commit();
		}
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		// _fragment = _activity.getFragmentManager().findFragmentByTag(_tag);
		if (_fragment == null)
		{
			_fragment = (Fragment) Fragment.instantiate(_activity, _fragmentClass.getName(), _args);
			ft.replace(R.id.container, _fragment, _tag);
		}
		else
		{
			ft.attach(_fragment);
		}
		if (_fragment instanceof ITabSelected)
		{
			((ITabSelected) _fragment).onTabSelected();
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
		// _fragment = _activity.getFragmentManager().findFragmentByTag(_tag);
		if (_fragment != null)
		{
			ft.detach(_fragment);
		}
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{
	}
}
