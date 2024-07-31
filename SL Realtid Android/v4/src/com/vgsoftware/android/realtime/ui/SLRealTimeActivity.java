package com.vgsoftware.android.realtime.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.vgsoftware.android.realtime.Constants;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Settings;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.billing.IabHelper;
import com.vgsoftware.android.realtime.billing.IabResult;
import com.vgsoftware.android.realtime.billing.Inventory;
import com.vgsoftware.android.realtime.billing.Purchase;
import com.vgsoftware.android.realtime.model.DataStore;
import com.vgsoftware.android.realtime.model.DatabaseHelper;
import com.vgsoftware.android.realtime.model.Donation;
import com.vgsoftware.android.realtime.model.ISite;
import com.vgsoftware.android.realtime.model.Site;
import com.vgsoftware.android.realtime.search.StationContentProvider;
import com.vgsoftware.android.realtime.ui.fragments.CloseToMeFragment;
import com.vgsoftware.android.realtime.ui.fragments.FavoritesFragment;
import com.vgsoftware.android.realtime.ui.fragments.TrafficStatusFragment;
import com.vgsoftware.android.realtime.ui.fragments.dialogs.AboutDialogFragment;
import com.vgsoftware.android.realtime.ui.fragments.dialogs.DonationDialogFragment;
import com.vgsoftware.android.realtime.ui.widget.DepartureProvider;
import com.vgsoftware.android.vglib.NetworkUtility;
import com.vgsoftware.android.vglib.RuntimeUtility;
import com.vgsoftware.android.vglib.ui.dialogs.IDialogFragmentAction;

public class SLRealTimeActivity extends OrmLiteBaseActivity<DatabaseHelper> implements IDialogFragmentAction
{
	private final static int InAppBillingCode = 1234;

	public static final String[] ACTIVE_PRODUCTS = new String[] { "donation_10", "donation_15", "donation_20", "donation_25", "donation_30"

	};

	public static final String[] ALL_PRODUCTS = new String[] { "donation_7", "donation_10", "donation_15", "donation_20", "donation_25", "donation_30" };

	private DataStore _dataStore = null;
	private Settings _settings = null;
	private SearchView _searchView = null;
	private SearchManager _searchManager = null;
	private MenuItem _searchMenuItem = null;
	private AdView _adView = null;
	private IabHelper _iabHelper = null;
	private InterstitialAd _interstitial = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_adView = (AdView) findViewById(R.id.ad_view);

		_settings = new Settings(this);
		_dataStore = new DataStore(this);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		actionBar.addTab(actionBar.newTab().setText(R.string.tab_favorites).setTabListener(new TabListener<FavoritesFragment>(this, "favorite", FavoritesFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_closetome).setTabListener(new TabListener<CloseToMeFragment>(this, "closetome", CloseToMeFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_trafficstatus).setTabListener(new TabListener<TrafficStatusFragment>(this, "trafficstatus", TrafficStatusFragment.class)));

		actionBar.selectTab(actionBar.getTabAt(_settings.getStartTab()));

		actionBar.setDisplayShowTitleEnabled(true);
		setTitle(R.string.title_activity_slrealtime);
		actionBar.setTitle(R.string.title_activity_slrealtime);

		setupBillingV3();

		handleIntent(getIntent());

		if (savedInstanceState != null && savedInstanceState.containsKey(Constants.INTENT_EXTRA_SELECTED_TAB))
		{
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt(Constants.INTENT_EXTRA_SELECTED_TAB));
		}
	}

	private void setupWidgets()
	{
		ComponentName favoriteComponent = new ComponentName(this, DepartureProvider.class);
		if (_settings.isFreeEdition())
		{
			getPackageManager().setComponentEnabledSetting(favoriteComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 1);
		}
		else
		{
			getPackageManager().setComponentEnabledSetting(favoriteComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 1);
		}
	}

	private void setupBillingV3()
	{
		String key1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAonC28e1HvVX2UQi1Ez8MbGptlHgHGEUGFGTpxKgpvfw1gFd5BFvLr";
		String key2 = "+AnSg0mB3l6QCyXMDnvRwZB1ELEcLeP8BfCyQ/+crxAeuiaUJpDb4xa/yNXxu4HPx5N8JTOduwO0FgTyDCsUC5lCDnMoUMiHIhCcAP6HyDTTLh7bSUfl68a5j6tBSZ";
		String key3 = "/CzKQxCsl1qwwEPxDFw6Ta6WwswrVR6u03gZUDBNRncVOC+XXPYdAWuAuljlb4h7jiWvWsem8CJXoCIz2t0QfQNi3HaEP8ndez9QhLLg6U2pA22PFkXEs6cuvENnbkykuES55K+5aOtb2dL81e94LqapblcS/eDZKYQIDAQAB";

		_iabHelper = new IabHelper(this, key1 + key2 + key3);

		_iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
		{
			@Override
			public void onIabSetupFinished(IabResult result)
			{
				if (!result.isSuccess())
				{
					LogManager.error("Problem setting up In-app billing: " + result);
				}
				else
				{
					LogManager.info("In-app billing setup successfully");
					_iabHelper.queryInventoryAsync(_inventoryListener);
				}
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putInt(Constants.INTENT_EXTRA_SELECTED_TAB, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	protected void onStart()
	{
		Tracking.activityStart(this, this);
		_settings.setStarts(_settings.getStarts() + 1);

		if (_settings.getLastVersionUsed() != RuntimeUtility.getVersion(this))
		{
			_settings.setLastVerionUsed(RuntimeUtility.getVersion(this));
			showAboutDialog();
		}

		if (_settings.isFreeEdition())
		{
			AdRequest adRequest = new AdRequest.Builder().addTestDevice("9345A4D195F8F029D0CF8BF6E17C7E63").addTestDevice("02A243F1CC9458AA931E6D964C1AA1C5").build();
			_adView.loadAd(adRequest);
			if (_settings.getStarts() > (_settings.getLastFullScreenAd() + 70))
			{
				_interstitial = new InterstitialAd(this);
				_interstitial.setAdUnitId(getString(R.string.GoogleAdMobUnitId));
				_interstitial.setAdListener(new AdListener()
				{
					public void onAdLoaded()
					{
						if (_interstitial != null && _interstitial.isLoaded())
						{
							_settings.setLastFullScreenAd(_settings.getStarts());
							_interstitial.show();
							Tracking.sendEvent(SLRealTimeActivity.this, Tracking.CATEGORY_ADMOB, "Full Screen", "Received");
						}
					}
				});
				adRequest = new AdRequest.Builder().addTestDevice("02A243F1CC9458AA931E6D964C1AA1C5").build();
				_interstitial.loadAd(adRequest);
			}
		}
		else
		{
			_adView.setVisibility(View.GONE);
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
	protected void onNewIntent(Intent intent)
	{
		handleIntent(intent);
	}

	private void handleIntent(final Intent intent)
	{
		Bundle extras = intent.getExtras();
		if (extras != null && extras.containsKey(Constants.INTENT_EXTRA_SELECT_TAB))
		{
			int selectedTabId = extras.getInt(Constants.INTENT_EXTRA_SELECT_TAB);
			if (selectedTabId < getActionBar().getTabCount())
			{
				getActionBar().getTabAt(selectedTabId).select();
			}
		}
		else
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
						final Site site = new Site();
						site.setSiteId(siteCursor.getInt(siteCursor.getColumnIndex(BaseColumns._ID)));
						site.setName(siteCursor.getString(siteCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)));
						site.setArea(siteCursor.getString(siteCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2)));
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								showDepartures(site);
							}
						});
					}
					catch (Exception ex)
					{
						String message = "Unable to make search";
						try
						{
							int siteId = siteCursor.getInt(siteCursor.getColumnIndex(BaseColumns._ID));
							message += ". Site id: '" + siteId + "'";
						}
						catch (Exception e)
						{
						}
						LogManager.error(message, ex);
					}
				}
			}
		}
	}

	public void showDepartures(ISite site)
	{
		if (site != null && NetworkUtility.isOnline(this))
		{
			Tracking.sendView(this, "/Departures/" + site.getName());
			Intent intent = new Intent(this, DepartureActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(StationContentProvider.BuildViewUri(site));
			startActivity(intent);
			_searchView.setIconified(true);
			_searchMenuItem.collapseActionView();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (_iabHelper != null)
		{
			_iabHelper.dispose();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_slrealtime, menu);
		_searchMenuItem = menu.findItem(R.id.menu_search);
		_searchView = (SearchView) _searchMenuItem.getActionView();
		_searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		_searchView.setSearchableInfo(_searchManager.getSearchableInfo(getComponentName()));
		_searchView.setIconifiedByDefault(false);
		_searchView.setSoundEffectsEnabled(true);

		if (!_settings.isFreeEdition())
		{
			menu.findItem(R.id.menu_donation).setVisible(false);
		}
		if (!RuntimeUtility.isDebug(this))
		{
			menu.findItem(R.id.menu_debug_buy).setVisible(false);
			menu.findItem(R.id.menu_debug_return).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_donation)
		{
			Tracking.sendEvent(this, Tracking.CATEGORY_DONATION, "ShowDialog", "User selected");
			showDonationDialog();
		}
		else if (item.getItemId() == R.id.menu_settings)
		{
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
		}
		else if (item.getItemId() == R.id.menu_about)
		{
			showAboutDialog();
		}
		else if (RuntimeUtility.isDebug(this) && item.getItemId() == R.id.menu_debug_buy)
		{
			Donation donation = new Donation("DEBUG");
			_dataStore.saveDonation(donation);
			_adView.setVisibility(View.GONE);
			setupWidgets();
		}
		else if (RuntimeUtility.isDebug(this) && item.getItemId() == R.id.menu_debug_return)
		{
			Donation donation = _dataStore.getDonation("DEBUG");
			if (donation != null)
			{
				_dataStore.deleteDonation(donation);
				_adView.setVisibility(View.VISIBLE);
				setupWidgets();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDonationDialog()
	{
		Tracking.sendView(this, "/DonationDialog");
		DonationDialogFragment donationDialog = new DonationDialogFragment();
		donationDialog.setOnAction(this);
		donationDialog.show(getFragmentManager(), "dialog");
	}

	private void showAboutDialog()
	{
		Tracking.sendView(this, "/AboutDialog");
		AboutDialogFragment aboutDialog = new AboutDialogFragment();
		aboutDialog.show(getFragmentManager(), "aboutDialog");
	}

	@Override
	public void onPositiveAction(DialogFragment sender, Object data)
	{
		if (sender instanceof DonationDialogFragment && data != null)
		{
			int pos = Integer.parseInt(data.toString());
			if (pos >= 0 && pos < ACTIVE_PRODUCTS.length)
			{
				Tracking.sendEvent(this, Tracking.CATEGORY_DONATION, "UserInput", "Positive");
				_iabHelper.launchPurchaseFlow(this, ACTIVE_PRODUCTS[pos], SLRealTimeActivity.InAppBillingCode, _purchaseFinishedListener);
			}
		}
	}

	@Override
	public void onNegativeAction(DialogFragment sender, Object data)
	{
		Tracking.sendEvent(this, Tracking.CATEGORY_DONATION, "UserInput", "Negative");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (!_iabHelper.handleActivityResult(requestCode, resultCode, data))
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	IabHelper.QueryInventoryFinishedListener _inventoryListener = new IabHelper.QueryInventoryFinishedListener()
	{
		public void onQueryInventoryFinished(IabResult result, Inventory inventory)
		{
			if (result.isFailure())
			{
				LogManager.error("Inventory query failed: " + result.getMessage());
			}
			else
			{
				LogManager.info("Inventory query successful");
				for (String sku : ALL_PRODUCTS)
				{
					Donation donation = _dataStore.getDonation(sku);
					if (inventory.hasPurchase(sku))
					{
						LogManager.info("Has purchased: " + sku);
						if (donation == null)
						{
							donation = new Donation(sku);
							donation.setStatus("OK");
							_dataStore.saveDonation(donation);
						}
					}
					else if (donation != null)
					{
						LogManager.info("Item has been removed: " + sku);
						_dataStore.deleteDonation(donation);
					}
					else
					{
						LogManager.info("Does not own: " + sku);
					}
				}
				if (_dataStore.listDonations().size() > 0)
				{
					_adView.setVisibility(View.GONE);
					setupWidgets();
				}
				else
				{
					_adView.setVisibility(View.VISIBLE);
					setupWidgets();
				}
			}
		}
	};

	IabHelper.OnIabPurchaseFinishedListener _purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener()
	{
		public void onIabPurchaseFinished(IabResult result, Purchase purchase)
		{
			if (result.isFailure())
			{
				LogManager.error("Error purchasing: " + result);
				Toast.makeText(SLRealTimeActivity.this, IabHelper.getResponseDesc(result.getResponse()), Toast.LENGTH_LONG).show();
				return;
			}
			else if (result.isSuccess())
			{
				Donation donation = _dataStore.getDonation(purchase.getSku());
				if (donation == null)
				{
					donation = new Donation(purchase.getSku());
				}
				donation.setStatus(Integer.toString(purchase.getPurchaseState()));
				_dataStore.saveDonation(donation);
				_adView.setVisibility(View.GONE);
				setupWidgets();
				Toast.makeText(SLRealTimeActivity.this, R.string.dialog_donation_purchased, Toast.LENGTH_LONG).show();
			}
		}
	};
}
