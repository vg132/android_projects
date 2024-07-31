package com.vgsoftware.android.trafik.ui.adapters;

import com.vgsoftware.android.trafik.dataabstraction.Feed;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TrafficPagerAdapter extends PagerAdapter implements OnItemClickListener
{
	private Context _context = null;
	private Feed _feed = null;
	private FeedItemAdapter _feedItemAdapter = null;

	public TrafficPagerAdapter(Context context, Feed feed)
	{
		_context = context;
		_feed = feed;
	}

	public void update()
	{
		if (_feedItemAdapter != null)
		{
			_feedItemAdapter.update();
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		if (position == 0)
		{
			ListView listView = new ListView(_context);
			_feedItemAdapter = new FeedItemAdapter(_context, _feed);
			listView.setAdapter(_feedItemAdapter);
			listView.setOnItemClickListener(this);
			container.addView(listView);
			return listView;
		}
		else
		{
			// TrafficImage imageView = new TrafficImage(_context,
			// _imageUrls.get(position));
		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return _feed.getName();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object view)
	{
		container.removeView((View) view);
	}

	@Override
	public int getCount()
	{
		return 1;
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == object;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		((FeedItemAdapter) parent.getAdapter()).toggle(position);
	}
}
