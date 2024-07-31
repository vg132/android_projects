package com.vgsoftware.android.vglib.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class MapAdapter<K, V> extends BaseAdapter
{
	@SuppressWarnings("rawtypes")
	private List<IMapItem> _mapItems = null;
	private int _headingResourceId = 0;
	private int _itemResourceId = 0;
	private LayoutInflater _inflater = null;
	private int _headingTextResourceId = 0;
	private int _itemTextResourceId = 0;

	public MapAdapter(Context context, Map<K, List<V>> data)
	{
		init(context, data, android.R.layout.simple_list_item_1, android.R.layout.simple_list_item_1, android.R.id.text1, android.R.id.text1);
	}

	public MapAdapter(Context context, Map<K, List<V>> data, int headingResourceId, int itemResourceId)
	{
		init(context, data, headingResourceId, itemResourceId, android.R.id.text1, android.R.id.text1);
	}

	public MapAdapter(Context context, Map<K, List<V>> data, int headingResourceId, int itemResourceId, int headingTextResourceId, int itemTextResourceId)
	{
		init(context, data, headingResourceId, itemResourceId, headingTextResourceId, itemTextResourceId);
	}

	@SuppressWarnings("rawtypes")
	private void init(Context context, Map<K, List<V>> data, int headingResourceId, int itemResourceId, int headingTextResourceId, int itemTextResourceId)
	{
		_mapItems = new ArrayList<IMapItem>();
		_headingResourceId = headingResourceId;
		_itemResourceId = itemResourceId;
		_headingTextResourceId = headingTextResourceId;
		_itemTextResourceId = itemTextResourceId;
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int keys = data.keySet().size();
		for (K key : data.keySet())
		{
			if (!hideHeadings() && (!hideSingleHeading() || keys > 1))
			{
				_mapItems.add(new MapHeading(key));
			}
			for (V value : data.get(key))
			{
				_mapItems.add(new MapValue(key, value));
			}
		}
	}

	public boolean hideHeadings()
	{
		return false;
	}

	public boolean hideSingleHeading()
	{
		return true;
	}

	@Override
	public int getCount()
	{
		return _mapItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _mapItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		@SuppressWarnings("unchecked")
		IMapItem<K, V> mapItem = (IMapItem<K, V>) getItem(position);
		if (mapItem instanceof MapAdapter.MapHeading)
		{
			return getHeadingView(position, mapItem.getKey(), convertView, parent);
		}
		else if (mapItem instanceof MapAdapter.MapValue)
		{
			return getItemView(position, mapItem.getKey(), mapItem.getValue(), convertView, parent);
		}
		return null;
	}

	public View getHeadingView(int position, K heading, View convertView, ViewGroup parent)
	{
		return createViewFromResource(position, convertView, parent, _headingResourceId, _headingTextResourceId);
	}

	public View getItemView(int position, K key, V item, View convertView, ViewGroup parent)
	{
		return createViewFromResource(position, convertView, parent, _itemResourceId, _itemTextResourceId);
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, int layoutResourceId, int textResourceId)
	{
		View view = null;
		TextView text = null;

		if (convertView == null || convertView.getId() != layoutResourceId)
		{
			view = _inflater.inflate(layoutResourceId, parent, false);
		}
		else
		{
			view = convertView;
		}

		try
		{
			if (textResourceId != 0)
			{
				text = (TextView) view.findViewById(textResourceId);
			}
		}
		catch (ClassCastException e)
		{
			Log.e("MapAdapter", "You must supply a resource ID for a TextView");
			throw new IllegalStateException("MapAdapter requires the resource ID to be a TextView", e);
		}

		if (text != null)
		{
			@SuppressWarnings("unchecked")
			IMapItem<K, V> item = (IMapItem<K, V>) getItem(position);
			if (item instanceof MapAdapter.MapHeading)
			{
				setText(text, item.getKey());
			}
			else
			{
				setText(text, item.getValue());
			}
		}
		return view;
	}

	private void setText(TextView textView, Object item)
	{
		if (item instanceof CharSequence)
		{
			textView.setText((CharSequence) item);
		}
		else
		{
			textView.setText(item.toString());
		}
	}

	interface IMapItem<K, V>
	{
		K getKey();

		V getValue();
	}

	class MapHeading implements IMapItem<K, V>
	{
		private K _key = null;

		public MapHeading(K key)
		{
			_key = key;
		}

		public K getKey()
		{
			return _key;
		}

		public V getValue()
		{
			return null;
		}
	}

	class MapValue implements IMapItem<K, V>
	{
		private K _key = null;
		private V _value = null;

		public MapValue(K key, V value)
		{
			_key = key;
			_value = value;
		}

		public K getKey()
		{
			return _key;
		}

		public V getValue()
		{
			return _value;
		}
	}
}
