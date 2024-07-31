package com.vgsoftware.android.polisen.ui.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vgsoftware.android.polisen.dataabstraction.Feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("UseSparseArrays")
public class FeedPagerAdapter extends PagerAdapter implements OnItemClickListener
{
	private Context _context = null;
	private List<Feed> _feeds = null;
	private Map<Integer, FeedItemAdapter> _feedListViews = null;

	public FeedPagerAdapter(Context context, List<Feed> feeds)
	{
		_context = context;
		_feeds = feeds;
		_feedListViews = new HashMap<Integer, FeedItemAdapter>();
	}

	public void update()
	{
		for (FeedItemAdapter feedItemAdapter : _feedListViews.values())
		{
			if (feedItemAdapter != null)
			{
				feedItemAdapter.update();
			}
		}
	}

	public int getFeedPosition(int feedId)
	{
		for (int i = 0; i < _feeds.size(); i++)
		{
			if (_feeds.get(i).getId() == feedId)
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return _feeds.get(position).getName();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		Feed feed = _feeds.get(position);
		ListView listView = new ListView(_context);
		FeedItemAdapter feedItemAdapter = new FeedItemAdapter(_context, feed);
		listView.setAdapter(feedItemAdapter);
		listView.setOnItemClickListener(this);
		container.addView(listView);

		_feedListViews.remove(feed.getId());
		_feedListViews.put(feed.getId(), feedItemAdapter);

		return listView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object view)
	{
		container.removeView((View) view);
	}

	@Override
	public int getCount()
	{
		return _feeds.size();
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