package com.vgsoftware.android.realtime.ui.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.parse.Site;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteAdapter extends BaseExpandableListAdapter
{
	private ArrayList<ArrayList<Site>> _sites = null;
	private LayoutInflater _layoutInflater = null;

	public SiteAdapter(Context context, ArrayList<Site> sites)
	{
		_layoutInflater = LayoutInflater.from(context);

		HashMap<String, ArrayList<Site>> sitesMap = new HashMap<String, ArrayList<Site>>();
		_sites = new ArrayList<ArrayList<Site>>();
		for (Site site : sites)
		{
			if (!sitesMap.containsKey(site.getAreaName()))
			{
				sitesMap.put(site.getAreaName(), new ArrayList<Site>());
				_sites.add(sitesMap.get(site.getAreaName()));
			}
			sitesMap.get(site.getAreaName()).add(site);
		}
	}

	public int getGroupCount()
	{
		return _sites.size();
	}

	public int getChildrenCount(int groupPosition)
	{
		if ((_sites.size() - 1) >= groupPosition)
		{
			return _sites.get(groupPosition).size();
		}
		return 0;
	}

	public Object getGroup(int groupPosition)
	{
		return _sites.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition)
	{
		return _sites.get(groupPosition).get(childPosition);
	}

	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	public long getChildId(int groupPosition, int childPosition)
	{
		return (groupPosition * 10000) + childPosition;
	}

	public boolean hasStableIds()
	{
		return false;
	}

	@SuppressWarnings("unchecked")
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		ArrayList<Site> sites = (ArrayList<Site>) getGroup(groupPosition);
		View view = _layoutInflater.inflate(R.layout.expandable_view_heading, parent, false);
		view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64));

		TextView textView = (TextView) view.findViewById(R.id.textView);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding(55, 0, 0, 0);
		textView.setText(sites.get(0).getAreaName());

		ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
		imageView.setVisibility(View.GONE);

		return view;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		TextView textView = new TextView(parent.getContext());

		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding(36, 10, 0, 10);
		Site site = (Site) getChild(groupPosition, childPosition);

		textView.setText(site.getName());
		textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, textView.getHeight()));

		return textView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
}
