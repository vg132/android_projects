package com.vgsoftware.android.autosportrss.ui.controls;

import java.util.ArrayList;
import java.util.HashMap;

import com.vgsoftware.android.autosportrss.dataabstraction.Feed;
import com.vgsoftware.android.autosportrss.dataabstraction.FeedDroidDB;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class FeedSpinner extends Spinner
{
	public FeedSpinner(Context context)
	{
		super(context);
		setupControl();
	}

	public FeedSpinner(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setupControl();
	}

	public FeedSpinner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupControl();
	}
	

	
	private void setupControl()
	{
		loadFeeds();
	}
	
	@SuppressWarnings("unchecked")
	public Feed getFeedAtPosition(int position)
	{
		FeedDroidDB db=new FeedDroidDB();
		Object itemData=getItemAtPosition(position);
		if(itemData instanceof HashMap<?,?>)
		{
			HashMap<String,String> selectedItem=(HashMap<String,String>)itemData;
			return db.loadFeed(Integer.parseInt(selectedItem.get("Id")));
		}
		return null;
	}
	
	public Feed getSelectedFeed()
	{
		return getFeedAtPosition(this.getSelectedItemPosition());
	}
	
	public void loadFeeds()
	{
		SimpleAdapter feedAdapter = new SimpleAdapter(this.getContext(), listFeeds(), android.R.layout.simple_spinner_item,new String[] { "Name" }, new int[] { android.R.id.text1 } );
		feedAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		setAdapter(feedAdapter);
	}

	private ArrayList<HashMap<String,String>> listFeeds()
	{
		FeedDroidDB db=new FeedDroidDB();
		ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		try
		{
			for(Feed feed : db.listActiveFeeds())
			{
				HashMap<String,String> map=new HashMap<String,String>();
				map.put("Id",String.valueOf(feed.getId()));
				map.put("Name",feed.getName());
				map.put("Url",feed.getUrl());
				list.add(map);
			}
		}
		catch(Exception ex)
		{
		}
		return list;		
	}
}
