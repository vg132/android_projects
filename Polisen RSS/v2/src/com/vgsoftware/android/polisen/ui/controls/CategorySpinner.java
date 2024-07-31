package com.vgsoftware.android.polisen.ui.controls;

import java.util.ArrayList;
import java.util.HashMap;

import com.vgsoftware.android.polisen.dataabstraction.Category;
import com.vgsoftware.android.polisen.dataabstraction.FeedDroidDB;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class CategorySpinner extends Spinner
{
	public CategorySpinner(Context context)
	{
		super(context);
		setupControl();
	}

	public CategorySpinner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupControl();
	}

	public CategorySpinner(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setupControl();
	}

	private void setupControl()
	{
		loadCategories();
	}

	@SuppressWarnings("unchecked")
	public Category getCategoryAtPosition(int position)
	{
		FeedDroidDB db = new FeedDroidDB();
		Object itemData = getItemAtPosition(position);
		if (itemData instanceof HashMap<?, ?>)
		{
			HashMap<String, String> selectedItem = (HashMap<String, String>) itemData;
			return db.loadCategory(Integer.parseInt(selectedItem.get("Id")));
		}
		return null;
	}

	public Category getSelectedCategory()
	{
		return getCategoryAtPosition(this.getSelectedItemPosition());
	}

	public void loadCategories()
	{
		SimpleAdapter categoryAdapter = new SimpleAdapter(this.getContext(), listCategories(), android.R.layout.simple_spinner_item, new String[] { "Name" }, new int[] { android.R.id.text1 });
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		setAdapter(categoryAdapter);
	}

	private ArrayList<HashMap<String, String>> listCategories()
	{
		FeedDroidDB db = new FeedDroidDB();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try
		{
			for (Category feed : db.listActiveCategories())
			{
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Id", String.valueOf(feed.getId()));
				map.put("Name", feed.getName());
				list.add(map);
			}
		}
		catch (Exception ex)
		{
		}
		return list;
	}
}
