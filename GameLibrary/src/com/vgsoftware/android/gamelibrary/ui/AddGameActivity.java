package com.vgsoftware.android.gamelibrary.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vgsoftware.android.gamelibrary.R;
import com.vgsoftware.android.gamelibrary.integration.amazon.models.GameInfo;
import com.vgsoftware.android.gamelibrary.model.DataStore;
import com.vgsoftware.android.gamelibrary.model.Game;
import com.vgsoftware.android.gamelibrary.model.Platform;
import com.vgsoftware.android.gamelibrary.services.ActionService;
import com.vgsoftware.android.gamelibrary.services.ActionService.ActionType;
import com.vgsoftware.android.gamelibrary.ui.controls.CompanyAutoCompleteTextView;
import com.vgsoftware.android.gamelibrary.ui.controls.GameTitleAutoCompleteTextView;
import com.vgsoftware.android.gamelibrary.ui.dialogs.PlatformDialog;
import com.vgsoftware.android.vglib.ui.dialogs.IDialogFragmentAction;

public class AddGameActivity extends ActionBaseActivity
{
	private AdView _adView = null;
	private final ScheduledExecutorService _worker = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> _scheduledFuture = null;
	private TextView _platformList = null;
	private GameTitleAutoCompleteTextView _gameTitleTextView = null;
	private CompanyAutoCompleteTextView _publisherTextView = null;
	private CompanyAutoCompleteTextView _developerTextView = null;
	private ActionBar _actionBar = null;
	private TextView _commentTextView = null;
	private List<Platform> _platforms = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addgame);

		_platforms = new ArrayList<Platform>();

		// Set up the action bar.
		_actionBar = getActionBar();
		_actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		_actionBar.setDisplayShowTitleEnabled(true);
		_actionBar.setTitle(R.string.addgame_title);

		_actionBar.setDisplayShowHomeEnabled(true);
		_actionBar.setDisplayHomeAsUpEnabled(true);

		_developerTextView = (CompanyAutoCompleteTextView) findViewById(R.id.autocomplete_developer);
		_publisherTextView = (CompanyAutoCompleteTextView) findViewById(R.id.autocomplete_publisher);
		_commentTextView = (TextView) findViewById(R.id.edit_comment);

		_adView = (AdView) findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice("9345A4D195F8F029D0CF8BF6E17C7E63").addTestDevice("02A243F1CC9458AA931E6D964C1AA1C5").build();
		_adView.loadAd(adRequest);

		_gameTitleTextView = (GameTitleAutoCompleteTextView) findViewById(R.id.autocomplete_game_title);
		_gameTitleTextView.setLoadingIndicator((ProgressBar) findViewById(android.R.id.progress));

		_platformList = (TextView) findViewById(android.R.id.text1);
		_platformList.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PlatformDialog dialog = new PlatformDialog(_platforms);
				dialog.setOnAction(new IDialogFragmentAction()
				{
					@SuppressWarnings("unchecked")
					@Override
					public void onPositiveAction(DialogFragment sender, Object data)
					{
						_platforms = (List<Platform>) data;
						List<String> platformNames = new ArrayList<String>();
						for (Platform platform : _platforms)
						{
							platformNames.add(platform.getName());
						}
						Collections.sort(platformNames);
						_platformList.setText(StringUtils.join(platformNames, ", "));
					}

					@Override
					public void onNegativeAction(DialogFragment sender, Object data)
					{
					}
				});
				dialog.show(getFragmentManager(), "Platforms");
			}
		});
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (_adView != null)
		{
			_adView.pause();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (_adView != null)
		{
			_adView.resume();
		}
	}

	@Override
	protected void onDestroy()
	{
		if (_adView != null)
		{
			_adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			this.finish();
			return true;
		}
		else if (item.getItemId() == R.id.menu_save_game)
		{
			Game game = new Game();
			game.setTitle(_gameTitleTextView.getText().toString());
			game.setDeveloper(_developerTextView.getText().toString());
			game.setPublisher(_publisherTextView.getText().toString());
			game.setComment(_commentTextView.getText().toString());
			game.setPlatforms(_platforms);
			DataStore.getInstance().saveGame(game);

			Toast.makeText(this, R.string.toast_game_saved, Toast.LENGTH_SHORT).show();
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_addgame, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		if (item.getItemId() == R.id.menu_scan_game)
		{
			IntentIntegrator integrator = new IntentIntegrator(AddGameActivity.this);
			integrator.initiateScan();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == IntentIntegrator.REQUEST_CODE)
		{
			final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			if (scanResult != null)
			{
				Intent extras = new Intent(this, ActionService.class);
				extras.putExtra(ActionService.EAN_CODE, scanResult.getContents());
				startActionService(ActionType.ACTION_LOOKUP_ITEM, extras);
			}
		}
	}

	@Override
	protected void processGameLookup(Intent extras)
	{
		final GameInfo gameInfo = (GameInfo) extras.getParcelableExtra(ActionService.GAME_INFO);
		if (gameInfo == null)
		{
			Toast.makeText(this, R.string.toast_game_not_found, Toast.LENGTH_SHORT).show();
		}
		else
		{
			_publisherTextView.setText("");
			_platforms.clear();
			_platformList.setText("");
			_commentTextView.setText("");

			_gameTitleTextView.setText(gameInfo.Title);
			if (gameInfo.Publisher != null)
			{
				_publisherTextView.setText(gameInfo.Publisher);
			}
			if (gameInfo.Platform != null)
			{
				Platform platform = DataStore.getInstance().loadPlatform(gameInfo.Platform);
				if (platform != null)
				{
					_platforms.add(platform);
					_platformList.setText(platform.getName());
				}
			}
		}
	}

	public void afterTextChanged(final Editable s)
	{
		Runnable task = new Runnable()
		{
			public void run()
			{
				Intent extras = new Intent(AddGameActivity.this, ActionService.class);
				extras.putExtra(ActionService.SEARCH_QUERY, s.toString());
				startActionService(ActionType.ACTION_SEARCH_GAME, extras);
			}
		};
		if (_scheduledFuture != null && !_scheduledFuture.isDone())
		{
			_scheduledFuture.cancel(false);
		}
		_scheduledFuture = _worker.schedule(task, 1000, TimeUnit.MILLISECONDS);
	}
}
