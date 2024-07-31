package com.vgsoftware.android.trafik.ui.adapters;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.vgsoftware.android.trafik.Log;
import com.vgsoftware.android.trafik.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.trafik.dataabstraction.Feed;
import com.vgsoftware.android.trafik.dataabstraction.FeedItem;
import com.vgsoftware.android.trafik.R;
import com.vgsoftware.android.vglib.GoogleAnalytics;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedItemAdapter extends BaseAdapter
{
	private SimpleDateFormat _dateFormat = new SimpleDateFormat("dd MMM HH:mm");
	private SimpleDateFormat _yearFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
	private Context _context = null;
	private List<FeedItem> _feedItems = null;
	private List<Integer> _expandedFeedItem = null;
	private Feed _feed = null;

	public FeedItemAdapter(Context context, Feed feed)
	{
		super();
		_expandedFeedItem = new ArrayList<Integer>();
		_context = context;
		_feed = feed;
		update();
	}

	public void update()
	{
		try
		{
			_feedItems = DatabaseHelper.getHelper(_context).getFeedItemDao().queryBuilder().orderBy("date", false).where().eq("feedId", _feed.getId()).query();
		}
		catch (SQLException ex)
		{
			GoogleAnalytics.getInstance(_context).trackEvent("sql exception", "load feed items", ex.getMessage());
			Log.error("Unable to load feed items.", ex);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return _feedItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _feedItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return _feedItems.get(position).getId();
	}

	public void toggle(int position)
	{
		FeedItem item = (FeedItem) getItem(position);
		if (_expandedFeedItem.contains(item.getId()))
		{
			_expandedFeedItem.remove((Integer) item.getId());
		}
		else
		{
			_expandedFeedItem.add(item.getId());
		}
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final FeedItem feedItem = _feedItems.get(position);
		if (feedItem != null)
		{
			View view = null;
			if (_expandedFeedItem.contains(feedItem.getId()))
			{
				view = LayoutInflater.from(_context).inflate(R.layout.feed_item_row_expanded, parent, false);
				TextView descriptionTextView = (TextView) view.findViewById(R.id.feedItemDescriptionTextView);
				TextView readMoreTextView = (TextView) view.findViewById(R.id.feedItemReadMoreTextView);

				descriptionTextView.setText(feedItem.getDescription());
				if (feedItem.getUrl() != null && !feedItem.getUrl().trim().equals(""))
				{
					readMoreTextView.setVisibility(View.VISIBLE);
					readMoreTextView.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedItem.getUrl()));
							_context.startActivity(browserIntent);
						}
					});
				}
				else
				{
					readMoreTextView.setVisibility(View.GONE);
				}
			}
			else
			{
				view = LayoutInflater.from(_context).inflate(R.layout.feed_item_row, parent, false);
			}
			TextView dateTextView = (TextView) view.findViewById(R.id.feedItemDateTextView);
			TextView titleTextView = (TextView) view.findViewById(R.id.feedItemHeadingTextView);
			dateTextView.setText(getDate(feedItem));
			titleTextView.setText(feedItem.getTitle());
			return view;
		}
		return convertView;
	}

	private String getDate(FeedItem feedItem)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(feedItem.getDate());
		if (Calendar.getInstance().get(Calendar.YEAR) != calendar.get(Calendar.YEAR))
		{
			return _yearFormat.format(feedItem.getDate());
		}
		return _dateFormat.format(feedItem.getDate());
	}
}
