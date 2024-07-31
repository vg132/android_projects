package com.vgsoftware.android.fastcheckin.ui.controls;

import java.util.List;

import com.vgsoftware.android.fastcheckin.R;
import com.vgsoftware.android.fastcheckin.dataabstraction.Place;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PlacesListView extends ListView
{
	private PlaceAdapter _placeAdapter = null;
	
	public PlacesListView(Context context)
	{
		super(context);
		
		setBackgroundColor(Color.TRANSPARENT);
		drawableStateChanged();
	}

	public PlacesListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		setBackgroundColor(Color.TRANSPARENT);
		drawableStateChanged();
	}

	public PlacesListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		setBackgroundColor(Color.TRANSPARENT);
		drawableStateChanged();
	}

	public void setPlaces(List<Place> places)
	{
		_placeAdapter = new PlaceAdapter(getContext(), R.layout.place_list_item, places);
		setAdapter(_placeAdapter);
	}
	
	public Place getPlaceAtPosition(int position)
	{
		Object item = getItemAtPosition(position);
		if (item instanceof Place)
		{
			return (Place) item;
		}
		return null;
	}

	public Place getSelectedPlace()
	{
		Object item = getSelectedItem();
		if (item instanceof Place)
		{
			return (Place) item;
		}
		return null;
	}
	
	public void refresh()
	{
		if (_placeAdapter != null)
		{
			_placeAdapter.refresh();
		}
	}
	
	private class PlaceAdapter extends ArrayAdapter<Place>
	{
		private List<Place> _items = null;
		
		public PlaceAdapter(Context context, int textViewResourceId, List<Place> items)
		{
			super(context, textViewResourceId, items);
			
			_items = items;
		}
		
		public void refresh()
		{
			PlacesListView.this.post(new Runnable()
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
			Place item = _items.get(position);
			View view = convertView;
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.place_list_item, null);

			if (item != null)
			{
				TextView nameTextView = (TextView) view.findViewById(R.id.PlaceListItem_Name);
				nameTextView.setText(item.getName());

				TextView categoryTextView = (TextView) view.findViewById(R.id.PlaceListItem_Category);				
				categoryTextView.setText(item.getCategory());
			}
			return view;
		}
	}
}
