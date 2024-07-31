package com.vgsoftware.android.polisen.ui;

import java.net.URI;
import java.net.URL;

import com.vgsoftware.android.polisen.Log;
import com.vgsoftware.android.polisen.Utility;
import com.vgsoftware.android.polisen.dataabstraction.Feed;
import com.vgsoftware.android.polisen.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.polisen.ui.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FeedSettings extends Activity implements OnFocusChangeListener
{
	private TextView _urlTextView;
	private EditText _urlEditText;
	private TextView _nameTextView;
	private EditText _nameEditText;
	private Button _saveButton;
	private Button _cancelButton;
	private Feed _feed;
	private CheckBox _activeCheckBox;
	private FeedDroidDB _database;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedsettings);
		setupActivity();
	}

	private void setupActivity()
	{
		setupControls();
		setupListeners();

		_database = new FeedDroidDB();
		_feed = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			_feed = _database.loadFeed(extras.getInt("FeedId"));
			if (_feed != null)
			{
				_nameEditText.setText(_feed.getName());
				_urlEditText.setText(_feed.getUrl());
				_activeCheckBox.setChecked(_feed.isActive());
			}
		}
	}

	private void setupControls()
	{
		_nameTextView = (TextView)findViewById(R.id.NameTextView);
		_nameEditText = (EditText) findViewById(R.id.NameEditText);
		_urlTextView = (TextView)findViewById(R.id.UrlTextView);
		_urlEditText = (EditText) findViewById(R.id.UrlEditText);
		_activeCheckBox = (CheckBox) findViewById(R.id.ActiveCheckBox);
		_saveButton = (Button) findViewById(R.id.SaveButton);
		_cancelButton = (Button) findViewById(R.id.CancelButton);
		
		if(!Utility.canAddFeeds())
		{
			_nameTextView.setVisibility(View.GONE);
			_nameEditText.setVisibility(View.GONE);
			_urlTextView.setVisibility(View.GONE);
			_urlEditText.setVisibility(View.GONE);
		}
	}

	private void setupListeners()
	{
		_nameEditText.setOnFocusChangeListener(this);
		_urlEditText.setOnFocusChangeListener(this);

		_saveButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (isValid(true))
				{
					if (_feed == null)
					{
						_feed = new Feed();
					}
					_feed.setName(_nameEditText.getText().toString());
					_feed.setUrl(_urlEditText.getText().toString());
					_feed.setActive(_activeCheckBox.isChecked());
					_database.saveFeed(_feed);
					setResult(RESULT_OK);
					finish();
				}
			}
		});
		_cancelButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	private boolean isValid(boolean validateEmptyUrl)
	{
		boolean isValid = true;
		String url = _urlEditText.getText().toString();
		if (TextUtils.isEmpty(_nameEditText.getText()))
		{
			Toast.makeText(FeedSettings.this, getString(R.string.FeedSettings_InvalidName), Toast.LENGTH_SHORT).show();
			isValid = false;
		}
		if (validateEmptyUrl || !TextUtils.isEmpty(url))
		{
			if (!validateUrl(url))
			{
				Toast.makeText(FeedSettings.this, getString(R.string.FeedSettings_InvalidUrl), Toast.LENGTH_SHORT).show();
				if (!url.startsWith("http") && !url.contains("://"))
				{
					Toast.makeText(this, getString(R.string.FeedSettings_InvalidUrl_Hint), Toast.LENGTH_LONG).show();
				}
				isValid = false;
			}
		}
		return isValid;
	}

	private boolean validateUrl(String url)
	{
		try
		{
			new URL(url);
			new URI(url);
			return true;
		}
		catch (Exception ex)
		{
			Log.warn("Invalid url: '" + _urlEditText.getText() + "'", ex);
		}
		return false;
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus)
	{
		if (!hasFocus)
		{
			isValid(false);
		}
	}
}
