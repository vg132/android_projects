package com.vgsoftware.android.realtime.ui.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

@SuppressWarnings("deprecation")
public class StationListAdapter extends CursorAdapter
{
	private ContentResolver _contentResolver = null;

	public StationListAdapter(Context context, Cursor cursor, int flags)
	{
		super(context, cursor, flags);
		_contentResolver = context.getContentResolver();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		final LayoutInflater inflater = LayoutInflater.from(context);
		final TwoLineListItem view = (TwoLineListItem) inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
		return view;
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint)
	{
		String searchQuery = constraint == null ? "" : constraint.toString();
		return _contentResolver.query(Uri.parse("content://com.vgsoftware.android.realtime.search.StationContentProvider/search"), null, null, new String[] { searchQuery }, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		((TextView) view.findViewById(android.R.id.text1)).setText(cursor.getString(1));
		((TextView) view.findViewById(android.R.id.text2)).setText(cursor.getString(2));
	}

	@Override
	public CharSequence convertToString(Cursor cursor)
	{
		return cursor.getString(1);
	}
}
