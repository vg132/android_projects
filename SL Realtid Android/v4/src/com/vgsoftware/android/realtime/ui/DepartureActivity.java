package com.vgsoftware.android.realtime.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.vgsoftware.android.realtime.Constants;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Settings;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.DatabaseHelper;
import com.vgsoftware.android.realtime.model.Departure;
import com.vgsoftware.android.realtime.model.ISite;
import com.vgsoftware.android.realtime.model.Site;
import com.vgsoftware.android.realtime.model.SiteSetting;
import com.vgsoftware.android.realtime.parse.DepartureParsedResponse;
import com.vgsoftware.android.realtime.parse.DepartureParser;
import com.vgsoftware.android.realtime.parse.IDepartureParsedListener;
import com.vgsoftware.android.realtime.parse.IDepartureParserError;
import com.vgsoftware.android.realtime.search.StationContentProvider;
import com.vgsoftware.android.realtime.ui.fragments.DepartureFragment;
import com.vgsoftware.android.realtime.ui.fragments.IDepartureInformationChangedListener;
import com.vgsoftware.android.realtime.ui.widget.FavoriteListProvider;

public class DepartureActivity extends OrmLiteBaseActivity<DatabaseHelper> implements IDepartureParsedListener, IDepartureParserError
{
	public final static String INTENT_EXTRA_SITE = "Site_Key";

	private Settings _settings = null;
	private SearchView _searchView = null;
	private SearchManager _searchManager = null;
	private MenuItem _searchMenuItem = null;
	private DepartureParser _departureParser = null;
	private RelativeLayout _loadingContainer = null;
	private LinearLayout _container = null;
	private ActionBar _actionBar = null;
	private AdView _adView = null;
	private TextView _loadingMessage = null;
	private TextView _errorMessage = null;
	private DataStore _dataStore = null;
	private SiteSetting _siteSetting = null;
	private ISite _site = null;
	private List<IDepartureInformationChangedListener> _departureInformationChangedListeners = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_departure);

		_settings = new Settings(this);
		_dataStore = new DataStore(this);

		if (_departureInformationChangedListeners == null)
		{
			_departureInformationChangedListeners = new ArrayList<IDepartureInformationChangedListener>();
		}
		_departureParser = new DepartureParser();
		_departureParser.setOnDepartureParsedListener(this);
		_departureParser.setOnDepartueParserError(this);

		_loadingContainer = (RelativeLayout) findViewById(R.id.container_loading);
		_loadingMessage = (TextView) findViewById(android.R.id.text1);
		_errorMessage = (TextView) findViewById(android.R.id.text2);
		_container = (LinearLayout) findViewById(R.id.container);
		_adView = (AdView) findViewById(R.id.ad_view);
		
		// Set up the action bar.
		_actionBar = getActionBar();
		_actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		_actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		_actionBar.setDisplayShowTitleEnabled(true);

		_actionBar.setDisplayShowHomeEnabled(true);
		_actionBar.setDisplayHomeAsUpEnabled(true);

		_container.setVisibility(View.GONE);
		_loadingContainer.setVisibility(View.VISIBLE);
		_actionBar.removeAllTabs();

		if (savedInstanceState != null && savedInstanceState.containsKey(DepartureActivity.INTENT_EXTRA_SITE))
		{
			_site = (ISite) savedInstanceState.getParcelable(DepartureActivity.INTENT_EXTRA_SITE);
		}
	}

	@Override
	protected void onStart()
	{
		Tracking.activityStart(this, this);
		if (_settings.isFreeEdition())
		{
			AdRequest adRequest = new AdRequest.Builder().addTestDevice("02A243F1CC9458AA931E6D964C1AA1C5").addTestDevice("9345A4D195F8F029D0CF8BF6E17C7E63").build();
			_adView.loadAd(adRequest);
		}
		else
		{
			_adView.setVisibility(View.GONE);
		}
		if (_site == null)
		{
			handleIntent(getIntent());
		}
		else
		{
			parseSite();
		}
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		Tracking.activityStop(this, this);
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			this.finish();
			return true;
		}
		else if (item.getItemId() == R.id.menu_save)
		{
			if (_site != null)
			{
				DataStore store = new DataStore(this);
				if (store.getFavorite(_site) == null)
				{
					store.saveFavorite(_site);
					Toast.makeText(this, R.string.toast_favorite_saved, Toast.LENGTH_SHORT).show();
					Tracking.sendView(this, "/Favorites/Save");
					Tracking.sendEvent(this, "Favorite", "Save", _site.getName());

					Intent intent = new Intent(this, FavoriteListProvider.class);
					intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
					// Use an array and EXTRA_APPWIDGET_IDS instead of
					// AppWidgetManager.EXTRA_APPWIDGET_ID,
					// since it seems the onUpdate() is only fired on that:
					int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, FavoriteListProvider.class));
					intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
					sendBroadcast(intent);
				}
			}
			else
			{
				Toast.makeText(this, R.string.toast_favorite_saved_error, Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		if (savedInstanceState != null && savedInstanceState.containsKey(DepartureActivity.INTENT_EXTRA_SITE))
		{
			_site = (ISite) savedInstanceState.getParcelable(DepartureActivity.INTENT_EXTRA_SITE);
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// _actionBar.removeAllTabs();
		if (_site != null)
		{
			outState.putParcelable(DepartureActivity.INTENT_EXTRA_SITE, _site);
		}
		super.onSaveInstanceState(outState);
	}

	public void parseSite()
	{
		Tracking.sendView(this, "/Departures/" + _site.getName());
		_siteSetting = _dataStore.getSiteSetting(_site.getSiteId());
		if (_siteSetting == null)
		{
			_siteSetting = new SiteSetting();
			_siteSetting.setSiteId(_site.getSiteId());
		}
		_siteSetting.setLastSearch(System.currentTimeMillis());
		_dataStore.saveSiteSetting(_siteSetting);
		_departureParser.parseDeparturesAsync(_site);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_departure, menu);
		_searchMenuItem = menu.findItem(R.id.menu_search);
		_searchView = (SearchView) _searchMenuItem.getActionView();
		_searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		_searchView.setSearchableInfo(_searchManager.getSearchableInfo(getComponentName()));
		_searchView.setIconifiedByDefault(false);
		_searchView.setSoundEffectsEnabled(true);

		return true;
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		handleIntent(intent);
	}

	private void handleIntent(final Intent intent)
	{
		final CountDownLatch latch = new CountDownLatch(1);
		final List<Cursor> cursorList = new ArrayList<Cursor>();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					if (Intent.ACTION_VIEW.equals(intent.getAction()))
					{
						cursorList.add(getContentResolver().query(intent.getData(), null, null, null, null));
					}
					else if (Intent.ACTION_SEARCH.equals(intent.getAction()))
					{
						String query = intent.getStringExtra(SearchManager.QUERY);
						cursorList.add(getContentResolver().query(StationContentProvider.CONTENT_URI, null, null, new String[] { query }, null));
					}
				}
				finally
				{
					latch.countDown();
				}
			}
		}).start();
		try
		{
			latch.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		if (cursorList.size() > 0)
		{
			Cursor siteCursor = cursorList.get(0);

			if (siteCursor != null && siteCursor.moveToFirst())
			{
				try
				{
					Site site = new Site();

					site.setSiteId(siteCursor.getInt(siteCursor.getColumnIndex(BaseColumns._ID)));
					site.setName(siteCursor.getString(siteCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)));
					site.setArea(siteCursor.getString(siteCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2)));
					_site = site;
					_container.setVisibility(View.GONE);
					_loadingContainer.setVisibility(View.VISIBLE);
					_actionBar.removeAllTabs();
					parseSite();
					if (_searchView != null)
					{
						_searchView.setIconified(true);
					}
					if (_searchMenuItem != null)
					{
						_searchMenuItem.collapseActionView();
					}
				}
				catch (Exception ex)
				{
					LogManager.error("Unable to make search", ex);
					Toast.makeText(this, R.string.toast_station_not_found, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public void setOnDepartueInformationChangedListener(IDepartureInformationChangedListener listener)
	{
		if (_departureInformationChangedListeners == null)
		{
			_departureInformationChangedListeners = new ArrayList<IDepartureInformationChangedListener>();
		}
		if (!_departureInformationChangedListeners.contains(listener))
		{
			_departureInformationChangedListeners.add(listener);
		}
	}

	@Override
	public void departuresParsed(final DepartureParsedResponse response)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				_actionBar.setTitle(_site.getName());
				if (_loadingContainer.getVisibility() == View.VISIBLE)
				{
					_loadingContainer.setVisibility(View.GONE);
					_container.setVisibility(View.VISIBLE);
				}
				if (response.getDepartures() != null && response.getDepartures().size() > 0)
				{
					Bundle bundle = new Bundle();
					bundle.putInt(Constants.INTENT_EXTRA_SITE_ID, _site.getSiteId());
					bundle.putInt(Constants.INTENT_EXTRA_TRANSPORTATIOIN_TYPE, response.getTransportationType());
					bundle.putParcelableArrayList(Constants.INTENT_EXTRA_DEPARTURE_LIST, new ArrayList<Departure>(response.getDepartures()));

					Tab tab = null;
					for (int i = 0; i < _actionBar.getTabCount(); i++)
					{
						if (_actionBar.getTabAt(i).getTag().equals(response.getTransportationType()))
						{
							tab = _actionBar.getTabAt(i);
						}
					}
					if (tab != null)
					{
						for (IDepartureInformationChangedListener listener : _departureInformationChangedListeners)
						{
							listener.departureInformationUpdated(response.getTransportationType(), response.getDepartures());
						}
					}
					else
					{
						if (isTransportationTypeActive(response.getTransportationType()))
						{
							try
							{
//								String name = Utilities.getTransportationTypeName(response.getTransportationType());
//								if (response.getTransportationType() == Constants.TRANSPORTATION_TYPE_TRAM && response.getDepartures().size() > 0)
//								{
//									name = response.getDepartures().get(0).getGroupOfLine();
//								}
								tab = _actionBar.newTab();
								tab.setTag(response.getTransportationType());
								tab.setTabListener(new TabListener<DepartureFragment>(DepartureActivity.this, null, DepartureFragment.class, bundle));
								if (response.getTransportationType() == Constants.TRANSPORTATION_TYPE_BUS)
								{
									tab.setIcon(R.drawable.buss_30);
								}
								else if (response.getTransportationType() == Constants.TRANSPORTATION_TYPE_METRO)
								{
									tab.setIcon(R.drawable.metro_30);
								}
								else if (response.getTransportationType() == Constants.TRANSPORTATION_TYPE_TRAIN)
								{
									tab.setIcon(R.drawable.train_30);
								}
								else if (response.getTransportationType() == Constants.TRANSPORTATION_TYPE_TRAM)
								{
									tab.setIcon(R.drawable.tram_30);
								}
								_actionBar.addTab(tab);
							}
							catch (Exception ex)
							{
							}
						}
					}
					if (tab != null && _siteSetting != null && _siteSetting.getSelectedTab() == response.getTransportationType())
					{
						LogManager.debug("Select saved tab: %d for site id: %d", response.getTransportationType(), _site.getSiteId());
						try
						{
							tab.select();
						}
						catch (Exception ex)
						{
							LogManager.info("Unable to select default tab", ex);
						}
					}
				}
			}
		});
	}

	private boolean isTransportationTypeActive(int type)
	{
		// switch (type)
		// {
		// case Constants.TRANSPORTATION_TYPE_BUS:
		// return _settings.getShowBus();
		// case Constants.TRANSPORTATION_TYPE_METRO:
		// return _settings.getShowSubway();
		// case Constants.TRANSPORTATION_TYPE_TRAIN:
		// return _settings.getShowTrain();
		// case Constants.TRANSPORTATION_TYPE_TRAM:
		// return _settings.getShowTram();
		// }
		return true;
	}

	@Override
	public void departureParserError()
	{
		DepartureActivity.this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				_loadingMessage.setVisibility(View.GONE);
				_errorMessage.setVisibility(View.VISIBLE);
			}
		});
	}
}
