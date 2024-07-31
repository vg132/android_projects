package com.vgsoftware.android.gamelibrary.ui;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.vgsoftware.android.gamelibrary.OrmLiteBaseFragmentActivity;
import com.vgsoftware.android.gamelibrary.R;
import com.vgsoftware.android.gamelibrary.model.DatabaseHelper;
import com.vgsoftware.android.gamelibrary.services.UpdateService;
import com.vgsoftware.android.gamelibrary.ui.adapters.SectionsPagerAdapter;
import com.vgsoftware.android.gamelibrary.ui.dialogs.AboutDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class GameLibraryActivity extends OrmLiteBaseFragmentActivity<DatabaseHelper>
{
	private final static int RESULT_ADD_GAME = 1001;

	private SectionsPagerAdapter _sectionsPagerAdapter = null;
	private ViewPager _viewPager = null;
	private AdView _adView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_sectionsPagerAdapter);

		_adView = (AdView) findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice("9345A4D195F8F029D0CF8BF6E17C7E63").addTestDevice("02A243F1CC9458AA931E6D964C1AA1C5").build();
		_adView.loadAd(adRequest);

		startService(new Intent(this, UpdateService.class));
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_add_game:
				Intent addGameIntent = new Intent(this, AddGameActivity.class);
				startActivityForResult(addGameIntent, GameLibraryActivity.RESULT_ADD_GAME);
				break;
			case R.id.menu_about:
				new AboutDialog().show(getFragmentManager(), null);
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
