package com.vgsoftware.android.trafik.ui.adapters;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.trafik.Log;
import com.vgsoftware.android.trafik.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.trafik.R;
import com.vgsoftware.android.trafik.ui.view.CameraImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class GalleryImageAdapter extends BaseAdapter
{
	private static final int ITEM_WIDTH = 136;
	private static final int ITEM_HEIGHT = 88;

	private final int mGalleryItemBackground;
	private final Context _context;
	private final float _density;
	private List<URL> _cameraUrls = null;

	public GalleryImageAdapter(Context context, int feedId, String area)
	{
		_context = context;
		_cameraUrls = new ArrayList<URL>();

		try
		{
			for (com.vgsoftware.android.trafik.dataabstraction.Camera camera : DatabaseHelper.getHelper(context).getCameraDao().queryBuilder().where().eq("feedId",feedId).and().eq("area",area).query())
			{
				try
				{
					_cameraUrls.add(new URL(camera.getUrl()));
				}
				catch (MalformedURLException ex)
				{
					Log.debug("Malformed url: " + ex.getMessage());
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		TypedArray a =_context.obtainStyledAttributes(R.styleable.Gallery1);
		mGalleryItemBackground = a.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 0);
		a.recycle();
		_density = context.getResources().getDisplayMetrics().density;
	}

	public int getCount()
	{
		return _cameraUrls.size();
	}

	public Object getItem(int position)
	{
		return _cameraUrls.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		CameraImage imageView;
		if (convertView == null)
		{
			convertView = new CameraImage(_context, _cameraUrls.get(position));

			imageView = (CameraImage) convertView;
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams((int) (ITEM_WIDTH * _density + 0.5f), (int) (ITEM_HEIGHT * _density + 0.5f)));

			// The preferred Gallery item background
			imageView.setBackgroundResource(mGalleryItemBackground);
		}
		else
		{
			imageView = (CameraImage) convertView;
		}
		return imageView;
	}
}
