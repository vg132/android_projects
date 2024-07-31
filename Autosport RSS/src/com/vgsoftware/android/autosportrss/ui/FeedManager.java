package com.vgsoftware.android.autosportrss.ui;

import com.vgsoftware.android.autosportrss.ui.R;
import com.vgsoftware.android.autosportrss.dataabstraction.Feed;
import com.vgsoftware.android.autosportrss.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.autosportrss.ui.controls.FeedListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class FeedManager extends Activity implements OnItemClickListener
{
	private FeedListView _feedListView;
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
		Toast.makeText(this,"Click on a feed to activate/deactivate it",Toast.LENGTH_LONG).show();
	}

	private void setupControls()
	{
		_feedListView = (FeedListView) findViewById(R.id.FeedListView);
		_feedListView.setOnItemClickListener(this);
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Feed feed = _feedListView.getFeedAtPosition(position);
		if (feed != null)
		{
			FeedDroidDB db=new FeedDroidDB();
			feed.setActive(!feed.isActive());
			db.saveFeed(feed);
			_feedListView.refresh();
			_changed=true;
		}
	}
}