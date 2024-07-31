package com.vgsoftware.android.wallpaper.winter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class Main extends Activity implements AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory
{
	private final static Integer[] _thumbs = { R.drawable.wallpaper_1_thumb, R.drawable.wallpaper_2_thumb, R.drawable.wallpaper_3_thumb, R.drawable.wallpaper_4_thumb, R.drawable.wallpaper_5_thumb, R.drawable.wallpaper_6_thumb, R.drawable.wallpaper_7_thumb, R.drawable.wallpaper_8_thumb };
	private final static Integer[] _wallpapers = { R.drawable.wallpaper_1, R.drawable.wallpaper_2, R.drawable.wallpaper_3, R.drawable.wallpaper_4, R.drawable.wallpaper_5, R.drawable.wallpaper_6, R.drawable.wallpaper_7, R.drawable.wallpaper_8 };

	private Button _setAsWallpaper = null;
	private ImageSwitcher _switcher;
	private int _currentWallpaper = -1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		_setAsWallpaper = (Button) findViewById(R.id.setAsWallpaper);
		_switcher = (ImageSwitcher) findViewById(R.id.switcher);
		_switcher.setFactory(this);
		_switcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		_switcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setOnItemSelectedListener(this);

		_setAsWallpaper.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final ProgressDialog dialog = ProgressDialog.show(Main.this,null,"Changing wallpaper...",true);
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								Bitmap wallpaper = BitmapFactory.decodeStream(getResources().openRawResource(_wallpapers[_currentWallpaper]));
								try
								{
									getApplicationContext().setWallpaper(wallpaper);
									runOnUiThread(new Runnable()
									{
										@Override
										public void run()
										{
											Toast.makeText(Main.this, "Wallpaper changed", Toast.LENGTH_LONG).show();
										}
									});
								}
								catch (Exception ex)
								{
									runOnUiThread(new Runnable()
									{
										@Override
										public void run()
										{
											Toast.makeText(Main.this, "Unable to change Wallpaper", Toast.LENGTH_LONG).show();
										}
									});
								}
								runOnUiThread(new Runnable()
								{
									@Override
									public void run()
									{
										dialog.dismiss();		
									}
								});
							}
						}).start();
					}
				});
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public void onItemSelected(AdapterView parent, View v, int position, long id)
	{
		_currentWallpaper = position;
		_switcher.setImageResource(_wallpapers[position]);
	}

	@SuppressWarnings("rawtypes")
	public void onNothingSelected(AdapterView parent)
	{
	}

	public View makeView()
	{
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}

	public class ImageAdapter extends BaseAdapter
	{
		private Context _context = null;

		public ImageAdapter(Context context)
		{
			_context = context;
		}

		public int getCount()
		{
			return _thumbs.length;
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView = new ImageView(_context);

			imageView.setImageResource(_thumbs[position]);
			imageView.setAdjustViewBounds(true);
			imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			imageView.setBackgroundResource(R.drawable.picture_frame);
			return imageView;
		}
	}
}