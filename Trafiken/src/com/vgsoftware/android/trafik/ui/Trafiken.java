package com.vgsoftware.android.trafik.ui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.vgsoftware.android.trafik.FeedSync;
import com.vgsoftware.android.trafik.Log;
import com.vgsoftware.android.trafik.Preferences;
import com.vgsoftware.android.trafik.R;
import com.vgsoftware.android.trafik.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.trafik.dataabstraction.Feed;
import com.vgsoftware.android.trafik.ui.adapters.TrafficPagerAdapter;
import com.vgsoftware.android.vglib.GoogleAnalytics;
import com.vgsoftware.android.vglib.NetworkUtility;
import com.viewpagerindicator.TitlePageIndicator;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class Trafiken extends OrmLiteBaseActivity<DatabaseHelper>
{
	private final static int MENU_GROUP_FEED = 10;
	private final static long UPDATE_INTERVAL = 1800000;

	private ViewPager _mainViewPager = null;
	private TitlePageIndicator _mainTitleIndicator = null;
	private TrafficPagerAdapter _trafficPagerAdapter = null;
	private Feed _feed = null;
	private Timer _timer = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafiken);

		_feed = getHelper().getFeedDao().queryForId(Preferences.getInstance(this).getSelectedFeedId());
		if (_feed == null)
		{
			List<Feed> feeds = getHelper().getFeedDao().queryForAll();
			if (feeds.size() > 0)
			{
				_feed = feeds.get(0);
			}
		}
		_mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
		if (_feed != null)
		{
			_trafficPagerAdapter = new TrafficPagerAdapter(this, _feed);
			_mainViewPager.setAdapter(_trafficPagerAdapter);
		}
		_mainTitleIndicator = (TitlePageIndicator) findViewById(R.id.mainTitlePageIndicator);
		_mainTitleIndicator.setViewPager(_mainViewPager);

		// updateFeed();

		_timer = new Timer();
		_timer.schedule(new UpdateTimerTask(), 0, Trafiken.UPDATE_INTERVAL);

		// Intent myIntent = new Intent(this, CameraGallery.class);
		// Intent myIntent = new Intent(this, AreaList.class);
		// startActivity(myIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_trafiken, menu);
		for (Feed feed : getHelper().getFeedDao().queryForAll())
		{
			menu.add(Trafiken.MENU_GROUP_FEED, feed.getId(), feed.getId(), feed.getName());
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		if (item.getItemId() == R.id.menu_refresh)
		{
			updateFeed();
		}
		else
		{
			Feed selectedFeed = getHelper().getFeedDao().queryForId(item.getItemId());
			if (selectedFeed != null)
			{
				_feed = selectedFeed;
				_trafficPagerAdapter = new TrafficPagerAdapter(this, _feed);
				_mainViewPager.setAdapter(_trafficPagerAdapter);
				_mainTitleIndicator = (TitlePageIndicator) findViewById(R.id.mainTitlePageIndicator);
				_mainTitleIndicator.setViewPager(_mainViewPager);
				updateFeed();
				Preferences.getInstance(this).setSelectedFeedId(item.getItemId());
			}
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void updateFeed()
	{
		setProgressBarIndeterminateVisibility(true);
		new FeedUpdateTask().execute(this);
	}

	class UpdateTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			Looper.prepare();
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					Trafiken.this.updateFeed();
				}
			});
		}
	}

	class FeedUpdateTask extends AsyncTask<Context, Void, Void>
	{
		@Override
		protected Void doInBackground(Context... params)
		{
			if (_feed != null)
			{
				FeedSync feedSync = new FeedSync(params[0]);
				feedSync.addFeedToSync(_feed);
				Log.debug("Run Start");
				feedSync.run();
				Log.debug("Run End");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			Log.debug("onPostExecute");
			setProgressBarIndeterminateVisibility(false);
			_trafficPagerAdapter.update();
			if (NetworkUtility.isOnline(Trafiken.this))
			{
				GoogleAnalytics.getInstance(Trafiken.this).dispatch();
			}
			super.onPostExecute(result);
		}
	}
}
