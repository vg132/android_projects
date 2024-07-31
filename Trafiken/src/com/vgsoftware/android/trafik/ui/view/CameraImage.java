package com.vgsoftware.android.trafik.ui.view;

import java.io.IOException;
import java.net.URL;

import com.vgsoftware.android.trafik.ImageRepository;
import com.vgsoftware.android.trafik.Log;
import com.vgsoftware.android.trafik.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CameraImage extends ImageView
{
	private URL _imageUrl = null;
	private Context _context = null;

	public CameraImage(Context context)
	{
		super(context);
		_context = context;		
	}

	public CameraImage(Context context, AttributeSet atts)
	{
		super(context,atts);
	}

	public CameraImage(Context context, AttributeSet atts, int defStyle)
	{
		super(context,atts,defStyle);
	}

	public CameraImage(Context context, URL imageUrl)
	{
		super(context);
		_context = context;
		setImageUrl(imageUrl);
	}

	public void setImageUrl(URL imageUrl)
	{
		_imageUrl = imageUrl;
		Bitmap bitmap = ImageRepository.getInstance(_context).getImage(_imageUrl);
		if (bitmap != null)
		{
			setImageBitmap(bitmap);
		}
		else if (ImageRepository.getInstance(_context).isNotFound(_imageUrl))
		{
			CameraImage.this.setImageResource(R.drawable.notfound);
		}
		else
		{
			new TrafficImageUpdateTask().execute();
		}
	}

	class TrafficImageUpdateTask extends AsyncTask<Void, Void, Bitmap>
	{
		@Override
		protected Bitmap doInBackground(Void... params)
		{
			try
			{
				Bitmap bitmap = BitmapFactory.decodeStream(_imageUrl.openConnection().getInputStream());
				ImageRepository.getInstance(_context).addImage(_imageUrl, bitmap);
				return bitmap;
			}
			catch (IOException io)
			{
				Log.error("IO Exception for file '" + _imageUrl.toString() + "'", io);
				ImageRepository.getInstance(_context).addNotFound(_imageUrl);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			if (bitmap != null)
			{
				CameraImage.this.setImageBitmap(bitmap);
			}
			else
			{
				CameraImage.this.setImageResource(R.drawable.notfound);
			}
		}
	}
}
