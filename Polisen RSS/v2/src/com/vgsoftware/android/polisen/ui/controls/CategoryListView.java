package com.vgsoftware.android.polisen.ui.controls;

import java.util.List;

import com.vgsoftware.android.polisen.dataabstraction.Category;
import com.vgsoftware.android.polisen.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.polisen.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryListView extends ListView
{
	private CategoryAdapter _categoryAdapter = null;

	public CategoryListView(Context context)
	{
		super(context);
		setupControl();
	}

	public CategoryListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupControl();
	}

	public CategoryListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setupControl();
	}

	private void setupControl()
	{
		loadCategories();
	}

	public void loadCategories()
	{
		FeedDroidDB db = new FeedDroidDB();
		_categoryAdapter = new CategoryAdapter(getContext(), R.layout.feed_row_active, db.listCategories());
		setAdapter(_categoryAdapter);
	}

	public Category getCategoryAtPosition(int position)
	{
		Object item = getItemAtPosition(position);
		if (item instanceof Category)
		{
			return (Category) item;
		}
		return null;
	}

	public void refreshCategoryList()
	{
		_categoryAdapter.refresh();
	}

	public Category getSelectedCategory()
	{
		Object item = getSelectedItem();
		if (item instanceof Category)
		{
			return (Category) item;
		}
		return null;
	}

	private class CategoryAdapter extends ArrayAdapter<Category>
	{
		private List<Category> _items = null;

		public CategoryAdapter(Context context, int textViewResourceId, List<Category> items)
		{
			super(context, textViewResourceId, items);
			_items = items;
		}

		public void refresh()
		{
			CategoryListView.this.post(new Runnable()
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
			Category item = _items.get(position);
			View view = convertView;
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (item.getActive())
			{
				view = layoutInflater.inflate(R.layout.category_row_active, null);
			}
			else
			{
				view = layoutInflater.inflate(R.layout.category_row_inactive, null);
			}
			if (item != null)
			{
				TextView nameTextView = (TextView) view.findViewById(R.id.FeedRow_Name);
				if (item.getActive())
				{
					nameTextView.setText(item.getName() + " - Aktiv");
				}
				else
				{
					nameTextView.setText(item.getName() + " - Inaktiv");
				}
			}
			return view;
		}
	}
}
