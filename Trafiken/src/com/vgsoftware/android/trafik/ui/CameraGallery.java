package com.vgsoftware.android.trafik.ui;

import java.net.URL;

import com.vgsoftware.android.trafik.R;
import com.vgsoftware.android.trafik.ui.adapters.GalleryImageAdapter;
import com.vgsoftware.android.trafik.ui.view.CameraImage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class CameraGallery extends Activity
{
	public final static String EXTRA_FEED_ID = "FeedIdKey";
	public final static String EXTRA_AREA_NAME = "AreaNameKey";

	private Gallery _gallery = null;
	private CameraImage _cameraPreviewImage = null;
	private GalleryImageAdapter _galleryImageAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		_cameraPreviewImage = (CameraImage) findViewById(R.id.cameraPreview);
		_gallery = (Gallery) findViewById(R.id.cameraGallery);

		Bundle extra = getIntent().getExtras();
		if (extra != null)
		{
			_galleryImageAdapter = new GalleryImageAdapter(this, extra.getInt(CameraGallery.EXTRA_FEED_ID), extra.getString(CameraGallery.EXTRA_AREA_NAME));
			_gallery.setAdapter(_galleryImageAdapter);
		}

		_gallery.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				URL imageUrl = (URL) _galleryImageAdapter.getItem(position);
				if (imageUrl != null)
				{
					_cameraPreviewImage.setImageUrl(imageUrl);
				}
			}
		});
	}
}
