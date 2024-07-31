package com.vgsoftware.android.polisen.ui;

import java.sql.SQLException;
import java.util.List;

import com.google.android.gcm.GCMRegistrar;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.vgsoftware.android.feedlib.Log;
import com.vgsoftware.android.polisen.FeedSync;
import com.vgsoftware.android.polisen.PolisenRSSApplication;
import com.vgsoftware.android.polisen.Preferences;
import com.vgsoftware.android.polisen.ui.R;
import com.vgsoftware.android.polisen.FeedSync.OnFeedSyncFinishedListener;
import com.vgsoftware.android.polisen.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.polisen.dataabstraction.FeedItem;
import com.vgsoftware.android.polisen.ui.adapters.FeedPagerAdapter;
import com.vgsoftware.android.vglib.RuntimeUtility;
import com.viewpagerindicator.TitlePageIndicator;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class PolisenRSS extends OrmLiteBaseActivity<DatabaseHelper> implements OnFeedSyncFinishedListener
{
	public static final String INTENT_UPDATE_DATA = "PolisenRSS_UpdateView";
	public static final int DIALOG_PREFERENCES = 102;

	private ViewPager _mainViewPager = null;
	private TitlePageIndicator _mainTitleIndicator = null;
	private FeedPagerAdapter _feedPagerAdapter = null;

	private DataUpdateReceiver _dataUpdateReceiver = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_polisen_rss);

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		final String regId = GCMRegistrar.getRegistrationId(PolisenRSS.this);
		if (regId.equals(""))
		{
			GCMRegistrar.register(PolisenRSS.this, GCMIntentService.SENDER_ID);
		}

		_mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
		_mainTitleIndicator = (TitlePageIndicator) findViewById(R.id.mainTitlePageIndicator);

		_feedPagerAdapter = new FeedPagerAdapter(this, getHelper().getActiveFeeds());
		_mainViewPager.setAdapter(_feedPagerAdapter);
		_mainTitleIndicator.setViewPager(_mainViewPager);
	}

	private void setupFeedPager()
	{
		_feedPagerAdapter = new FeedPagerAdapter(this, getHelper().getActiveFeeds());
		_mainViewPager.setAdapter(_feedPagerAdapter);
		_mainViewPager.refreshDrawableState();
		_mainTitleIndicator.setViewPager(_mainViewPager);
		_mainTitleIndicator.setCurrentItem(0);
	}

	@Override
	protected void onPause()
	{
		new Runnable()
		{
			public void run()
			{
				try
				{
					for (FeedItem feedItem : getHelper().getFeedItemDao().queryBuilder().where().eq("read", false).query())
					{
						feedItem.setRead(true);
						getHelper().getFeedItemDao().update(feedItem);
					}
				}
				catch (SQLException ex)
				{
					Log.error("Unable to mark feed items as read", ex);
				}
			}
		}.run();
		PolisenRSSApplication.activityPaused();
		if (_dataUpdateReceiver != null)
		{
			unregisterReceiver(_dataUpdateReceiver);
		}
		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		if (intent != null)
		{
			Bundle bundle = intent.getExtras();
			if (bundle != null)
			{
				int feedId = bundle.getInt("FeedId", -1);
				if (feedId >= 0)
				{
					int pos = _feedPagerAdapter.getFeedPosition(feedId);
					if (pos >= 0)
					{
						_mainViewPager.setCurrentItem(pos);
					}
				}
			}
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		PolisenRSSApplication.activityResumed();
		updateView();
		if (Preferences.getInstance(this).getOutOfSync() && Preferences.getInstance(this).getAutomaticUpdate() != Preferences.AUTOMATIC_UPDATE_NEVER)
		{
			updateFeed();
		}

		if (_dataUpdateReceiver == null)
		{
			_dataUpdateReceiver = new DataUpdateReceiver();
		}
		registerReceiver(_dataUpdateReceiver, new IntentFilter(PolisenRSS.INTENT_UPDATE_DATA));

		if (Preferences.getInstance(this).getLastVersionUsed() != RuntimeUtility.getVersion(this))
		{
			Preferences.getInstance(this).setLastVersionUsed(RuntimeUtility.getVersion(this));
			showChangesDialog();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_polisen_rss, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_refresh)
		{
			updateFeed();
		}
		else if (item.getItemId() == R.id.menu_settings)
		{
			Intent preferencesIntent = new Intent(PolisenRSS.this, com.vgsoftware.android.polisen.ui.Preferences.class);
			startActivityForResult(preferencesIntent, PolisenRSS.DIALOG_PREFERENCES);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == PolisenRSS.DIALOG_PREFERENCES)
		{
			setupFeedPager();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showChangesDialog()
	{
		updateFeed();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.activity_polisen_rss_changes_title);
		builder.setMessage(R.string.activity_polisen_rss_changes_summary);

		builder.setPositiveButton(R.string.common_ok, null);

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	private void updateFeed()
	{
		setProgressBarIndeterminateVisibility(true);
		new FeedUpdateTask().execute(this);
	}

	public void updateView()
	{
		_feedPagerAdapter.update();
	}

	@Override
	public void onFeedSyncFinished(List<FeedItem> newItems)
	{
		setProgressBarIndeterminateVisibility(false);
	}

	class FeedUpdateTask extends AsyncTask<Context, Void, Void>
	{
		@Override
		protected Void doInBackground(Context... params)
		{
			FeedSync feedSync = new FeedSync(params[0]);
			feedSync.run();
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			setProgressBarIndeterminateVisibility(false);
			_feedPagerAdapter.update();
			super.onPostExecute(result);
		}
	}

	class DataUpdateReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals(PolisenRSS.INTENT_UPDATE_DATA))
			{
				PolisenRSS.this.updateView();
			}
		}
	}
}
