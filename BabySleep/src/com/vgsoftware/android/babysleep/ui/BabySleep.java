package com.vgsoftware.android.babysleep.ui;

import com.vgsoftware.android.babysleep.R;
import com.vgsoftware.android.babysleep.ui.fragment.IFragmentName;
import com.vgsoftware.android.babysleep.ui.fragment.StartFragment;
import com.vgsoftware.android.babysleep.ui.fragment.StatisticsFragment;
import com.vgsoftware.android.babysleep.ui.fragment.TimerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BabySleep extends FragmentActivity
{
	private SectionsPagerAdapter _sectionsPagerAdapter = null;
	private ViewPager _viewPager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_sleep);
		_sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_sectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.baby_sleep, menu);
		return true;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			switch(position)
			{
				case 0:
					return new StartFragment();
				case 1:
					return new TimerFragment();
				case 2:
					return new StatisticsFragment();
			}
			return null;
		}

		@Override
		public int getCount()
		{
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			IFragmentName fragment =  (IFragmentName)getItem(position);
			return fragment.getName();
		}
	}

	public static class DummySectionFragment extends Fragment
	{
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_baby_sleep_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
}
