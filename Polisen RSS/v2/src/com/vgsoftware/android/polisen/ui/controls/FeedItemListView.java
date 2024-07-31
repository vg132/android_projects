package com.vgsoftware.android.polisen.ui.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vgsoftware.android.polisen.Utility;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.polisen.dataabstraction.FeedItem;
import com.vgsoftware.android.polisen.ui.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FeedItemListView extends ListView implements OnItemClickListener
{
	private Feed _currentFeed;
	private FeedItemAdapter _feedItemAdapter;

	public FeedItemListView(Context context)
	{
		super(context);
		setupControl();
	}

	public FeedItemListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupControl();
	}

	public FeedItemListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setupControl();
	}

	private void setupControl()
	{
		this.setOnItemClickListener(this);
	}

	public Feed getCurrentFeed()
	{
		return _currentFeed;
	}

	public FeedItem getFeedItemAtPosition(int position)
	{
		Object item = getItemAtPosition(position);
		if (item instanceof FeedItem)
		{
			return (FeedItem) item;
		}
		return null;
	}

	public void updateFeed()
	{
		FeedDroidDB db = new FeedDroidDB();
		if (_currentFeed != null && _feedItemAdapter != null)
		{
			ArrayList<FeedItem> newItems = new ArrayList<FeedItem>();
			for (FeedItem feedItem : db.listFeedItems(_currentFeed))
			{
				if (!_feedItemAdapter.contains(feedItem))
				{
					newItems.add(feedItem);
				}
			}
			Collections.sort(newItems, FeedItem.DateSorter);
			_feedItemAdapter.addAll(newItems);
		}
	}

	public void setCurrentFeed(Feed feed)
	{
		if (feed != null)
		{
			FeedDroidDB db = new FeedDroidDB();
			_currentFeed = feed;
			_feedItemAdapter = new FeedItemAdapter(getContext(), R.layout.feed_item_row_read, db.listFeedItems(_currentFeed));
			setAdapter(_feedItemAdapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		FeedItem item = getFeedItemAtPosition(position);
		if (item != null)
		{
			FeedDroidDB db = new FeedDroidDB();
			item.setRead(true);
			db.saveFeedItem(item);
			updateFeed();
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
			getContext().startActivity(myIntent);
		}
	}

	private class FeedItemAdapter extends ArrayAdapter<FeedItem>
	{
		private List<FeedItem> _items = null;

		public FeedItemAdapter(Context context, int textViewResourceId, List<FeedItem> items)
		{
			super(context, textViewResourceId, items);
			_items = items;
		}

		public boolean contains(FeedItem feedItem)
		{
			return _items.contains(feedItem);
		}

		public void addAll(List<FeedItem> items)
		{
			_items.addAll(0, items);
			FeedItemListView.this.post(new Runnable()
			{
				@Override
				public void run()
				{
					notifyDataSetChanged();
				}
			});
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			FeedItem item = _items.get(position);
			View view = convertView;
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (item.isRead())
			{
				view = layoutInflater.inflate(R.layout.feed_item_row_read, null);
			}
			else
			{
				view = layoutInflater.inflate(R.layout.feed_item_row_unread, null);
			}
			if (item != null)
			{
				TextView dateTextView = (TextView) view.findViewById(R.id.DateTextView);
				TextView titleTextView = (TextView) view.findViewById(R.id.TitleTextView);
				TextView descriptionTextView = (TextView) view.findViewById(R.id.DescriptionTextView);
				dateTextView.setText(Utility.getInstance().formatDateTime(item.getDate()));
				titleTextView.setText(Utility.removeHtml(item.getTitle()).trim());
				descriptionTextView.setText(Utility.removeHtml(item.getDescription()).trim());
			}
			return view;
		}
	}
}
