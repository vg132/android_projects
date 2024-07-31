package com.vgsoftware.android.autosportrss.ui.controls;

import java.util.List;

import com.vgsoftware.android.autosportrss.ui.R;
import com.vgsoftware.android.autosportrss.dataabstraction.Feed;
import com.vgsoftware.android.autosportrss.dataabstraction.FeedDroidDB;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FeedListView extends ListView
{
	public final static int CONTEXTMENU_EDIT=100;
	public final static int CONTEXTMENU_DELETE=101;
	
	private FeedAdapter _feedAdapter;
	
	public FeedListView(Context context)
	{
		super(context);
		setupControl();
	}
	
	public FeedListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setupControl();
	}

	public FeedListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupControl();
	}
	
	private void setupControl()
	{
		loadFeeds();
	}
	
	public void loadFeeds()
	{
		FeedDroidDB db=new FeedDroidDB();
		_feedAdapter = new FeedAdapter(getContext(), R.layout.feed_row_active, db.listFeeds());
		setAdapter(_feedAdapter);
	}
	
	public Feed getFeedAtPosition(int position)
	{
		Object item = getItemAtPosition(position);
		if (item instanceof Feed)
		{
			return (Feed) item;
		}
		return null;
	}
	
	public Feed getSelectedFeed()
	{
		Object item = getSelectedItem();
		if(item instanceof Feed)
		{
			return (Feed)item;
		}
		return null;
	}
	
	public void refresh()
	{
		if(_feedAdapter!=null)
		{
			_feedAdapter.refresh();
		}
	}


	@Override
	protected void onCreateContextMenu(ContextMenu menu)
	{
		super.onCreateContextMenu(menu);
		menu.setHeaderTitle(R.string.FeedListView_ContextMenu_Heading);
		menu.add(0,FeedListView.CONTEXTMENU_EDIT,0,R.string.Common_Edit);
		menu.add(0,FeedListView.CONTEXTMENU_DELETE,0,R.string.Common_Delete);
	}
	
	private class FeedAdapter extends ArrayAdapter<Feed>
	{
		private List<Feed> _items = null;

		public FeedAdapter(Context context, int textViewResourceId, List<Feed> items)
		{
			super(context, textViewResourceId, items);
			_items = items;
		}

		public void refresh()
		{
			FeedListView.this.post(new Runnable()
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
			Feed item = _items.get(position);
			View view = convertView;
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (item.isActive())
			{
				view = layoutInflater.inflate(R.layout.feed_row_active, null);
			}
			else
			{
				view = layoutInflater.inflate(R.layout.feed_row_inactive, null);
			}
			if (item != null)
			{
				TextView nameTextView = (TextView) view.findViewById(R.id.FeedRow_Name);
				TextView urlTextView = (TextView) view.findViewById(R.id.FeedRow_Url);
				nameTextView.setText(item.getName());
				String url=item.getUrl().replace("http://","").replace("https://","");
				if(item.isActive())
				{
					urlTextView.setText("Active - " + url);
				}
				else
				{
					urlTextView.setText("Inactive - " + url);
				}
			}
			return view;
		}
	}
}
