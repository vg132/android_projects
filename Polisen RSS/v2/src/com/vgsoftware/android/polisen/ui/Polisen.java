package com.vgsoftware.android.polisen.ui;

import java.util.List;

import com.vgsoftware.android.polisen.Log;
import com.vgsoftware.android.polisen.Utility;
import com.vgsoftware.android.polisen.dataabstraction.Category;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.polisen.dataabstraction.FeedItem;
import com.vgsoftware.android.polisen.service.UpdateService;
import com.vgsoftware.android.polisen.service.UpdateService.OnFeedsUpdated;
import com.vgsoftware.android.polisen.ui.R;
import com.vgsoftware.android.polisen.ui.controls.CategorySpinner;
import com.vgsoftware.android.polisen.ui.controls.FeedItemListView;
import com.vgsoftware.android.polisen.ui.controls.FeedSpinner;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

public class Polisen extends Activity implements OnFeedsUpdated
{
	private static final int DIALOG_PROGRESS = 100;
	private static final int DIALOG_PREFERENCES = 102;
	private static final int DIALOG_FEED_MANAGER = 103;
	private static final int DIALOG_CATEGORY_SELECTOR = 104;

	private static final int MENU_PREFERENCES = 200;
	private static final int MENU_FEED_MANAGER = 201;
	private static final int MENU_FORCE_UPDATE = 202;
	private static final int MENU_MARK_ALL_READ = 203;
	private static final int MENU_CATEGORY_SELECTOR = 204;

	private CategorySpinner _categorySpinner;
	private FeedSpinner _feedSpinner;
	private FeedItemListView _feedItemListView;
	private ProgressDialog _progressDialog;

	private UpdateService _updateService;
	private boolean _updateServiceBound;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupActivity();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService();
	}

	private void setupActivity()
	{
		Utility.Init(this);
		setContentView(R.layout.main);
		setupVariables();
		setupListeners();
		startService();
		bindService();
		
		SharedPreferences preferences=getSharedPreferences("Polisen",MODE_PRIVATE);
		
		if(preferences.getBoolean("first_run2",true))
		{
			SharedPreferences.Editor editor=preferences.edit();
			editor.putBoolean("first_run2",false);
			editor.commit();
			showCategorySelector();
			Toast.makeText(this,"Välj de regioner som du är intresserad av att följa.",Toast.LENGTH_LONG).show();
		}
	}

	private void setupVariables()
	{
		_categorySpinner = (CategorySpinner) findViewById(R.id.CategorySpinner);
		_feedSpinner = (FeedSpinner) findViewById(R.id.FeedSpinner);
		_feedItemListView = (FeedItemListView) findViewById(R.id.FeedItemListView);
	}

	private void setupListeners()
	{
		_categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id)
			{
				Log.info("category selected");
				Category category = _categorySpinner.getCategoryAtPosition(position);
				if (category != null)
				{
					Log.info("category not null");
					_feedSpinner.loadFeeds(category.getId());
				}
			}

			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		});
		_feedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id)
			{
				Feed feed = _feedSpinner.getFeedAtPosition(position);
				if (feed != null)
				{
					_feedItemListView.setCurrentFeed(feed);
				}
			}

			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		});
	}

	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case Polisen.DIALOG_PROGRESS:
			_progressDialog = new ProgressDialog(this);
			_progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			_progressDialog.setMessage(getResources().getString(R.string.ProgressDialogText));
			return _progressDialog;
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, Polisen.MENU_FORCE_UPDATE, 0, R.string.Menu_FeedDroid_ForceUpdate);
		menu.add(0, Polisen.MENU_MARK_ALL_READ, 1, R.string.Menu_FeedDroid_MarkAllRead);
		menu.add(0, Polisen.MENU_CATEGORY_SELECTOR, 2, "Välj aktiva regioner");
		if (Utility.canAddFeeds())
		{
			menu.add(0, Polisen.MENU_FEED_MANAGER, 2, R.string.Menu_FeedDroid_FeedManager);
		}
		menu.add(0, Polisen.MENU_PREFERENCES, 3, R.string.Menu_FeedDroid_Preferences);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == Polisen.MENU_PREFERENCES)
		{
			showPreferencesDialog();
		}
		else if (item.getItemId() == Polisen.MENU_FEED_MANAGER)
		{
			showFeedManager();
		}
		else if (item.getItemId() == Polisen.MENU_FORCE_UPDATE && _updateService != null)
		{
			_updateService.forceUpdate();
		}
		else if (item.getItemId() == Polisen.MENU_MARK_ALL_READ)
		{
			Feed feed = _feedSpinner.getSelectedFeed();
			if (feed != null)
			{
				FeedDroidDB db = new FeedDroidDB();
				for (FeedItem feedItem : db.listFeedItems(feed))
				{
					feedItem.setRead(true);
					db.saveFeedItem(feedItem);
				}
				_feedItemListView.setCurrentFeed(feed);
			}
		}
		else if (item.getItemId() == Polisen.MENU_CATEGORY_SELECTOR)
		{
			showCategorySelector();
		}
		return false;
	}

	private void showCategorySelector()
	{
		Intent categorySelectorIntent = new Intent(Polisen.this, CategorySelector.class);
		startActivityForResult(categorySelectorIntent, Polisen.DIALOG_CATEGORY_SELECTOR);
	}

	private void showFeedManager()
	{
		Intent feedManagerIntent = new Intent(Polisen.this, FeedManager.class);
		startActivityForResult(feedManagerIntent, Polisen.DIALOG_FEED_MANAGER);
	}

	private void showPreferencesDialog()
	{
		Intent preferencesIntent = new Intent(Polisen.this, Preferences.class);
		startActivityForResult(preferencesIntent, Polisen.DIALOG_PREFERENCES);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Polisen.DIALOG_FEED_MANAGER && resultCode == RESULT_OK)
		{
			// _feedSpinner.loadFeeds();
			if (_updateService != null)
			{
				_updateService.forceUpdate();
			}
		}
		else if(requestCode==Polisen.DIALOG_CATEGORY_SELECTOR && resultCode==RESULT_OK)
		{
			_categorySpinner.loadCategories();
			Category category=_categorySpinner.getSelectedCategory();
			if(category!=null)
			{
				_feedSpinner.loadFeeds(category.getId());
			}
			if(_updateService!=null)
			{
				_updateService.forceUpdate();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startService()
	{
		Utility.getInstance().getContext().startService(new Intent(this, UpdateService.class));
	}

	private void bindService()
	{
		bindService(new Intent(this, UpdateService.class), _serviceConnection, Context.BIND_AUTO_CREATE);
		_updateServiceBound = true;
	}

	void unbindService()
	{
		if (_updateServiceBound)
		{
			unbindService(_serviceConnection);
			_updateServiceBound = false;
		}
	}

	private ServiceConnection _serviceConnection = new ServiceConnection()
	{
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			_updateService = ((UpdateService.LocalBinder) service).getService();
			_updateService.setOnFeedsUpdated(Polisen.this);
		}

		public void onServiceDisconnected(ComponentName className)
		{
			_updateService = null;
		}
	};

	@Override
	public void FeedsUpdated(List<FeedItem> newFeedItems)
	{
		if (newFeedItems.size() > 0)
		{
			_feedItemListView.updateFeed();
		}
	}
}