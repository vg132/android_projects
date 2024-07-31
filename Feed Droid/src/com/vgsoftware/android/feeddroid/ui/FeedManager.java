package com.vgsoftware.android.feeddroid.ui;

import com.vgsoftware.android.feeddroid.dataabstraction.Feed;
import com.vgsoftware.android.feeddroid.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.feeddroid.ui.controls.FeedListView;
import com.vgsoftware.android.feeddroid.R;
import com.vgsoftware.android.feeddroid.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class FeedManager extends Activity implements OnItemClickListener
{
	public static final int DIALOG_FEEDSETTINGS = 100;
	public static final int MENU_ADD_FEED = 1000;

	private FeedListView _feedItemListView;
	private boolean _changed;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedmanager);
		setupActivity();
	}

	private void setupActivity()
	{
		setupControls();

		registerForContextMenu(_feedItemListView);
	}

	private void setupControls()
	{
		_feedItemListView = (FeedListView) findViewById(R.id.FeedItemListView);
		_feedItemListView.setOnItemClickListener(this);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		if (info != null)
		{
			Feed selectedFeed = _feedItemListView.getFeedAtPosition(info.position);
			if (selectedFeed != null)
			{
				if (item.getItemId() == FeedListView.CONTEXTMENU_DELETE)
				{
					deleteFeed(selectedFeed);
				}
				else if (item.getItemId() == FeedListView.CONTEXTMENU_EDIT)
				{
					showFeedEdit(selectedFeed);
				}
			}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, FeedManager.MENU_ADD_FEED, 0, R.string.Menu_FeedManager_AddFeed);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == FeedManager.MENU_ADD_FEED)
		{
			Intent feedSettingsIntent = new Intent(FeedManager.this, FeedSettings.class);
			startActivityForResult(feedSettingsIntent, FeedManager.DIALOG_FEEDSETTINGS);
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && _changed == true)
		{
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return (super.onKeyDown(keyCode, event));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == FeedManager.DIALOG_FEEDSETTINGS && resultCode == RESULT_OK)
		{
			_feedItemListView.loadFeeds();
			_changed = true;
		}
		else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Feed selectedFeed = _feedItemListView.getFeedAtPosition(position);
		if (selectedFeed != null)
		{
			showFeedEdit(selectedFeed);
		}
	}

	private void showFeedEdit(Feed feed)
	{
		if (feed != null)
		{
			Intent feedSettingsIntent = new Intent(FeedManager.this, FeedSettings.class);
			feedSettingsIntent.putExtra("FeedId", feed.getId());
			startActivityForResult(feedSettingsIntent, FeedManager.DIALOG_FEEDSETTINGS);
		}
	}

	private void deleteFeed(final Feed feed)
	{
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
			 .setTitle(getString(R.string.Dialog_ConfirmDelete_Title))
			 .setMessage(Utility.stringFormat(getString(R.string.Dialog_ConfirmDelete_Text),feed.getName()))
			 .setPositiveButton(getString(R.string.Common_OK),
					 new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				FeedDroidDB db=new FeedDroidDB();
				db.deleteFeed(feed);
				_feedItemListView.loadFeeds();
				_changed = true;
			}
		}).setNegativeButton(getString(R.string.Common_Cancel), null).show();
	}
}