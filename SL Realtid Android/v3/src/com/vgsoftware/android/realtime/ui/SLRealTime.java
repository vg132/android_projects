package com.vgsoftware.android.realtime.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.ads.AdView;
import com.vgsoftware.android.realtime.CachedResult;
import com.vgsoftware.android.realtime.LogManager;
import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.RealTimeException;
import com.vgsoftware.android.realtime.Settings;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.UserPreferences;
import com.vgsoftware.android.realtime.Utilities;
import com.vgsoftware.android.realtime.data.CatalogEntry;
import com.vgsoftware.android.realtime.data.CatalogEntry.Managed;
import com.vgsoftware.android.realtime.dataabstraction.Donation;
import com.vgsoftware.android.realtime.dataabstraction.Favorite;
import com.vgsoftware.android.realtime.dataabstraction.Database;
import com.vgsoftware.android.realtime.dataabstraction.Station;
import com.vgsoftware.android.realtime.dataabstraction.TransportationType;
import com.vgsoftware.android.realtime.parse.Departure;
import com.vgsoftware.android.realtime.parse.ParserBase;
import com.vgsoftware.android.realtime.parse.RealTimeParser;
import com.vgsoftware.android.realtime.parse.Site;
import com.vgsoftware.android.realtime.parse.SiteSearchParser;
import com.vgsoftware.android.realtime.parse.TrafficSituation;
import com.vgsoftware.android.realtime.parse.TrafficSituationParser;
import com.vgsoftware.android.realtime.ui.adapters.DepartureAdapter;
import com.vgsoftware.android.realtime.ui.adapters.FavoriteAdapter;
import com.vgsoftware.android.realtime.ui.adapters.SiteAdapter;
import com.vgsoftware.android.realtime.ui.adapters.TrafficSituationAdapter;
import com.vgsoftware.android.realtime.ui.controls.ActionBar;
import com.vgsoftware.android.realtime.ui.controls.OnSearchListener;
import com.vgsoftware.android.realtime.ui.controls.OnTransportationTypeChangedListener;
import com.vgsoftware.android.vglib.billing.BillingService;
import com.vgsoftware.android.vglib.billing.BillingService.RequestPurchase;
import com.vgsoftware.android.vglib.billing.BillingService.RestoreTransactions;
import com.vgsoftware.android.vglib.billing.Constants.PurchaseState;
import com.vgsoftware.android.vglib.billing.Constants.ResponseCode;
import com.vgsoftware.android.vglib.billing.PurchaseObserver;
import com.vgsoftware.android.vglib.billing.ResponseHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SLRealTime extends Activity implements OnSearchListener, OnTransportationTypeChangedListener
{
	public static final String INTENT_EXTRA_FAVORITE = "_FAVORITE_";
	public static final String INTENT_EXTRA_STATION = "_STATION_";
	public static final String INTENT_EXTRA_TRANSPORTATION_TYPE = "__TRANSPORTATION_TYPE__";
	public static final String INTENT_EXTRA_SHOW_SAVED = "_SHOW_SAVED_";
	public static final String INTENT_EXTRA_SHOW_CLOSE_TO_ME = "_SHOW_CLOSE_TO_ME_";

	public static final int HANDLER_MESSAGE_SL_REAL_TIME = 100;
	public static final int HANDLER_MESSAGE_TRAFFIC_SITUATION = 200;
	public static final int HANDLER_MESSAGE_SITE_SEARCH = 300;

	public static final int PROGRESS_DIALOG = 0;
	private static final int PREFERENCES_DIALOG = 3;
	private static final int FAVORITES_DIALOG = 4;
	private static final int CLOSE_TO_ME_DIALOG = 5;

	public static final CatalogEntry FREE_PRODUCT = new CatalogEntry("free", Managed.MANAGED);
	public static final CatalogEntry[] DONATION_PRODUCTS = new CatalogEntry[] { new CatalogEntry("donation_7", Managed.MANAGED), new CatalogEntry("donation_10", Managed.MANAGED), new CatalogEntry("donation_15", Managed.MANAGED), new CatalogEntry("donation_20", Managed.MANAGED), new CatalogEntry("donation_25", Managed.MANAGED), new CatalogEntry("donation_30", Managed.MANAGED), };

	private ListView _realTimeListView = null;
	private AdView _bannerAdView = null;
	private ActionBar _actionBar = null;
	private TextView _textView = null;

	private ProgressDialog _progressDialog = null;
	private ParserBase _baseParser = null;

	private HashMap<String, CachedResult> _departureCache = new HashMap<String, CachedResult>();
	private CachedResult _trafficSituationCache = null;

	private Station _currentStation = null;

	private static LogManager _log = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		_log = new LogManager(this);
		_log.debug("onCreate");
		super.onCreate(savedInstanceState);

		Settings.getInstance().init(this);
		Database.getInstance().init(this);
		UserPreferences.getInstance().init(this);

		setContentView(R.layout.realtime);

		setupLocalVariables();
		setupDefaultSettings();
		setupBilling();
	}

	private void showChangesDialog()
	{
		_log.debug("showChangesDialog");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.ChangesDialogTitle);
		builder.setMessage(R.string.ChangesDialogMessage);

		builder.setPositiveButton(R.string.CommonOk, null);

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	@Override
	protected void onStart()
	{
		_log.debug("onStart");
		super.onStart();
		Tracking.activityStart(this, this);
		ResponseHandler.register(_realTimePurchaseObserver);
		UserPreferences.getInstance().setStarts(UserPreferences.getInstance().getStarts() + 1);
		UserPreferences.getInstance().save();

		if (Database.getInstance().listDonation().size() > 0)
		{
			_bannerAdView.setVisibility(View.GONE);
		}
		if (UserPreferences.getInstance().getLastVersionUsed() != Utilities.getVersion(this))
		{
			UserPreferences.getInstance().setLastVersionUsed(Utilities.getVersion(this));
			UserPreferences.getInstance().save();
			showChangesDialog();
		}
	}

	@Override
	protected void onPause()
	{
		_log.debug("onPause");
		super.onPause();
		_departureCache.clear();
	}

	@Override
	protected void onStop()
	{
		_log.debug("onStop");
		super.onStop();

		Tracking.activityStop(this, this);
		ResponseHandler.unregister(_realTimePurchaseObserver);
		_departureCache.clear();
	}

	@Override
	protected void onResume()
	{
		_log.debug("onResume");
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		_log.debug("onDestroy");
		_billingService.unbind();
		Database.getInstance().close();
		super.onDestroy();
	}

	private void setupDefaultSettings()
	{
		_log.debug("setupDefaultSettings");
		switch (Settings.getInstance().getStartupScreen())
		{
			case 1:
				Intent closeToMeIntent = new Intent(SLRealTime.this, CloseToMe.class);
				startActivityForResult(closeToMeIntent, SLRealTime.CLOSE_TO_ME_DIALOG);
				break;
			case 2:
				showFavoritesDialog();
				break;
		}
		_actionBar.setTransportationType(Settings.getInstance().getDefaultTransportationType());
	}

	private void setupLocalVariables()
	{
		_log.debug("setupLocalVariables");
		_realTimeListView = (ListView) findViewById(R.id.listView);
		_bannerAdView = (AdView) findViewById(R.id.adView);
		_actionBar = (ActionBar) findViewById(R.id.actionBar);
		_textView = (TextView)findViewById(R.id.textView);
		_actionBar.setOnSearchListener(this);
		_actionBar.setOnTransportationTypeChangeListener(this);
	}

	@SuppressWarnings("unchecked")
	private void doTrafficSituationSearch()
	{
		_log.debug("doTrafficSituationSearch");
		Tracking.sendView(this, "/TrafficStatus");
		if (_progressDialog == null)
		{
			if (_trafficSituationCache != null && _trafficSituationCache.getCacheTimeoutTime() > System.currentTimeMillis())
			{
				showTrafficSituationList((ArrayList<TrafficSituation>) _trafficSituationCache.getData());
			}
			else
			{
				showProgressDialog();
				_baseParser = new TrafficSituationParser(parserHandler);
				_baseParser.start();
			}
		}
	}

	public void onSearch(TransportationType transportationType, String stationName)
	{
		if (transportationType.getId() == 5)
		{
			doSiteSearch();
		}
		else
		{
			Toast.makeText(this, R.string.StationNotFound, Toast.LENGTH_SHORT).show();
			_actionBar.expandTransportationTypeSelector();
		}
	}

	@SuppressWarnings("unchecked")
	public void onSearch(TransportationType transportationType, Station station)
	{
		if (_progressDialog == null)
		{
			_log.debug("Search with station: " + station.getName() + ", type: " + station.getTransportationTypeId());
			_currentStation = station;

			String cacheKey = station.getId() + "-" + station.getTransportationTypeId();
			if (_departureCache.containsKey(cacheKey) && _departureCache.get(cacheKey).getCacheTimeoutTime() > System.currentTimeMillis())
			{
				_log.debug("Search result found in cache, using cached result");
				setupResultList((List<Departure>) _departureCache.get(cacheKey).getData());
			}
			else
			{
				_log.debug("Search result not in cache, loading from network.");
				showProgressDialog();
				_baseParser = new RealTimeParser(this, parserHandler, station);
				_baseParser.start();
			}
		}
	}

	private void doSiteSearch()
	{
		_log.debug("doSiteSearch");
		String stationName = _actionBar.getText();
		if (!TextUtils.isEmpty(stationName) && _progressDialog == null)
		{
			showProgressDialog();
			_baseParser = new SiteSearchParser(this, parserHandler, stationName);
			_baseParser.start();
		}
	}

	private void showProgressDialog()
	{
		_log.debug("showProgressDialog");
		_progressDialog = new ProgressDialog(this);
		_progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_progressDialog.setMessage(getResources().getString(R.string.ProgressDialogText));
		_progressDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		_log.debug("onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		_log.debug("onPrepareOptionsMenu");
		MenuItem item = menu.findItem(R.id.MenuItemCloseToMeViewId);
		if (item != null)
		{
			item.setVisible(Settings.getInstance().isMyLocationEnabled());
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		_log.debug("onOptionsItemSelected");
		if (item.getItemId() == R.id.MenuItemSaveSearchId)
		{
			saveFavorite();
		}
		else if (item.getItemId() == R.id.MenuItemPreferencesId)
		{
			showPreferencesDialog();
		}
		else if (item.getItemId() == R.id.MenuItemFavoriteViewId)
		{
			showFavoritesDialog();
		}
		else if (item.getItemId() == R.id.MenuItemCloseToMeViewId)
		{
			showCloseToMeActivity();
		}
		else if (item.getItemId() == R.id.MenuItemTrafficSituationId)
		{
			doTrafficSituationSearch();
		}
		return false;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{
		_log.debug("onMenuOpened");
		if (menu != null)
		{
			MenuItem item = menu.findItem(R.id.MenuItemSaveSearchId);
			if (item != null)
			{
				item.setEnabled(_currentStation != null);
			}
		}
		return true;
	}

	private void showPreferencesDialog()
	{
		_log.debug("showPreferencesDialog");
		Intent preferencesIntent = new Intent(SLRealTime.this, Preferences.class);
		startActivityForResult(preferencesIntent, SLRealTime.PREFERENCES_DIALOG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		_log.debug("onActivityResult");
		if (requestCode == SLRealTime.PREFERENCES_DIALOG && resultCode == RESULT_OK)
		{
			// Do nothing
		}
		else if (requestCode == SLRealTime.FAVORITES_DIALOG && resultCode == RESULT_OK && data != null && data.getExtras() != null)
		{
			if (data.getExtras().getBoolean(SLRealTime.INTENT_EXTRA_SHOW_CLOSE_TO_ME))
			{
				showCloseToMeActivity();
			}
			else
			{
				loadFavorite(data.getExtras().getInt(SLRealTime.INTENT_EXTRA_FAVORITE));
			}
		}
		else if (requestCode == SLRealTime.CLOSE_TO_ME_DIALOG && resultCode == RESULT_OK && data != null && data.getExtras() != null)
		{
			if (data.getExtras().getBoolean(SLRealTime.INTENT_EXTRA_SHOW_SAVED))
			{
				showFavoritesDialog();
			}
			else
			{
				loadStation(data.getExtras().getInt(SLRealTime.INTENT_EXTRA_STATION));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showFavoritesDialog()
	{
		_log.debug("showFavoritesDialog");
		final List<Favorite> favorites = Database.getInstance().listFavorites();
		Collections.sort(favorites);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.FavoriteDialogTitle);

		View layout = inflater.inflate(R.layout.favorites_dialog, (ViewGroup) findViewById(R.id.favoriteContainer));
		builder.setView(layout);

		final ListView listView = (ListView) layout.findViewById(R.id.favoriteListView);
		listView.setAdapter(new FavoriteAdapter(this, R.layout.dialog_item, android.R.id.text1, favorites));

		builder.setPositiveButton(R.string.FavoriteDialogEditButtonText, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				editFavorites();
			}
		});
		final Dialog dialog = builder.create();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (dialog != null)
				{
					dialog.dismiss();
				}
				loadFavorite(favorites.get(position).getId());
			}
		});

		dialog.show();
	}

	private void editFavorites()
	{
		_log.debug("editFavorites");
		final List<Favorite> favorites = Database.getInstance().listFavorites();
		Collections.sort(favorites);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.EditFavoriteDialogTitle);

		View layout = inflater.inflate(R.layout.favorites_dialog, (ViewGroup) findViewById(R.id.favoriteContainer));
		builder.setView(layout);

		final ListView listView = (ListView) layout.findViewById(R.id.favoriteListView);

		listView.setAdapter(new FavoriteAdapter(this, R.layout.list_item_multiple_choice, android.R.id.text1, favorites));
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		builder.setPositiveButton(R.string.EditFavoriteDialogDeleteButtonText, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				for (long id : listView.getCheckItemIds())
				{
					Database.getInstance().deleteFavorite((int) id);
				}
			}
		});

		builder.create().show();
	}

	private void showCloseToMeActivity()
	{
		_log.debug("showCloseToMeActivity");
		Intent closeToMeIntent = new Intent(SLRealTime.this, CloseToMe.class);
		startActivityForResult(closeToMeIntent, SLRealTime.CLOSE_TO_ME_DIALOG);
	}

	private void loadFavorite(int id)
	{
		_log.debug("loadFavorite: " + id);
		Favorite favorite = Database.getInstance().loadFavorite(id);
		if (favorite != null)
		{
			try
			{
				_actionBar.setTransportationType(favorite.getTransportationTypeId());
				Station station = Database.getInstance().loadStation(favorite.getStationId());
				if (station != null)
				{
					_actionBar.setText(station.getName());
				}
				Tracking.sendView(this, "favoriteLoaded");
				_actionBar.doSearch();
			}
			catch (Exception ex)
			{
				_log.error("Unable to load favorite", ex);
			}
		}
		else
		{
			_log.warn("Favorite with id " + id + " not found");
		}
	}

	private void loadStation(int id)
	{
		_log.debug("loadStation");
		Station station = Database.getInstance().loadStation(id);
		if (station != null)
		{
			try
			{
				_actionBar.setTransportationType(station.getTransportationTypeId());
				_actionBar.setText(station.getName());
				_actionBar.doSearch();
			}
			catch (Exception ex)
			{
				_log.error("Unable to load station with id: " + id, ex);
			}
		}
		else
		{
			_log.warn("Station with id " + id + " not found.");
		}
	}

	private void saveFavorite()
	{
		_log.debug("saveFavorite");
		if (_currentStation != null)
		{
			boolean alreadySaved = false;
			for (Favorite favorite : Database.getInstance().listFavorites())
			{
				if (favorite.getStationId() == _currentStation.getId() && favorite.getTransportationTypeId() == _actionBar.getTransportationType().getId())
				{
					alreadySaved = true;
				}
			}
			if (!alreadySaved)
			{
				Favorite favorite = new Favorite(_currentStation.getId());
				favorite.setTransportationTypeId(_actionBar.getTransportationType().getId());
				Database.getInstance().saveFavorite(favorite);
				Toast.makeText(this, R.string.FavoriteSaved, Toast.LENGTH_SHORT).show();
				Tracking.sendView(this, "FavoriteSaved");
			}
			else
			{
				Toast.makeText(this, R.string.FavoriteExists, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler parserHandler = new Handler()
	{
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg)
		{
			_log.debug("handleMessage, what: " + msg.what);
			if (_baseParser != null && _baseParser.getException() == null)
			{
				if (msg.what == SLRealTime.HANDLER_MESSAGE_SITE_SEARCH)
				{
					showSiteSearchResultList((ArrayList<Site>) _baseParser.getData());
				}
				else if (msg.what == SLRealTime.HANDLER_MESSAGE_SL_REAL_TIME)
				{
					Station station = _currentStation;
					String cacheKey = station.getId() + "-" + station.getTransportationTypeId();
					if (_departureCache.containsKey(cacheKey))
					{
						_departureCache.remove(cacheKey);
					}
					if (_baseParser.getData().size() > 0)
					{
						_departureCache.put(cacheKey, new CachedResult(_baseParser.getData()));
					}
					setupResultList((List<Departure>) _baseParser.getData());
				}
				else if (msg.what == SLRealTime.HANDLER_MESSAGE_TRAFFIC_SITUATION)
				{
					_trafficSituationCache = new CachedResult(_baseParser.getData());
					showTrafficSituationList((ArrayList<TrafficSituation>) _trafficSituationCache.getData());
				}
			}
			else if (_baseParser.getException() instanceof RealTimeException)
			{
				Toast.makeText(SLRealTime.this, ((RealTimeException) _baseParser.getException()).getResourceId(), Toast.LENGTH_SHORT).show();
			}
			if (_progressDialog != null)
			{
				try
				{
					_progressDialog.dismiss();
				}
				catch (Exception ex)
				{
				}
				_progressDialog = null;
			}
		}
	};

	private void showTrafficSituationList(ArrayList<TrafficSituation> trafficSituations)
	{
		_log.debug("showTrafficSituationList");
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.expandable_view_dialog, (ViewGroup) findViewById(R.id.container));

		ExpandableListView expandableListView = (ExpandableListView) layout.findViewById(R.id.ExpandableViewDialogExpandableListView);
		expandableListView.setAdapter(new TrafficSituationAdapter(this, trafficSituations));

		builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		builder.setTitle(R.string.TrafficSituationDialogTitle);
		alertDialog = builder.create();
		alertDialog.show();
	}

	private void showSiteSearchResultList(ArrayList<Site> sites)
	{
		_log.debug("showSiteSearchResultList");
		if (sites != null)
		{
			for (Site site : sites)
			{
				if (Database.getInstance().loadStation(site.getSiteId(), _actionBar.getTransportationType().getId()) == null)
				{
					Database.getInstance().saveSiteAsStation(site, _actionBar.getTransportationType().getId());
				}
			}
			_actionBar.reloadStationList();

			final Dialog dialog = new Dialog(SLRealTime.this);
			dialog.setContentView(R.layout.expandable_view_dialog);
			dialog.setTitle(R.string.SiteSearchDialogTitle);
			ExpandableListView expandableListView = (ExpandableListView) dialog.findViewById(R.id.ExpandableViewDialogExpandableListView);
			expandableListView.setAdapter(new SiteAdapter(this, sites));
			expandableListView.expandGroup(0);
			expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
			{
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
				{
					Site site = (Site) ((SiteAdapter) parent.getExpandableListAdapter()).getChild(groupPosition, childPosition);
					if (site != null)
					{
						Station station = Database.getInstance().loadStationBySiteId(site.getSiteId(), _actionBar.getTransportationType().getId());
						if (station != null)
						{
							_actionBar.setText(station.toString());
							_actionBar.doSearch();
						}
					}
					dialog.cancel();
					return true;
				}
			});
			dialog.setCancelable(true);
			dialog.show();
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(SLRealTime.this);
			builder.setMessage(R.string.NoStationsFound).setCancelable(true).setPositiveButton(R.string.CommonOk, null);
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	public void onTransportationTypeChanged(TransportationType transportationType)
	{
		// _actionBar.setText("");
		// Toast.makeText(this,transportationType.getName(),Toast.LENGTH_SHORT).show();
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		_log.debug("onCreateDialog");
		switch (id)
		{
			case SLRealTime.FAVORITES_DIALOG:
				break;
		}
		return super.onCreateDialog(id);
	}

	private void setupResultList(List<Departure> departures)
	{
		_log.debug("setupResultList");
		if (departures != null && departures.size() > 0)
		{
			_textView.setVisibility(View.GONE);
			if (_currentStation != null)
			{
				Toast.makeText(this, String.format(getString(R.string.RealTimeInformationHeading), _currentStation.getName()), Toast.LENGTH_LONG).show();
			}
			_realTimeListView.setAdapter(new DepartureAdapter(this, R.layout.realtime_result_row, departures, _actionBar.getTransportationType().getId()));
		}
		else
		{
			_textView.setVisibility(View.VISIBLE);
			Toast.makeText(this, R.string.RealTimeInformationError, Toast.LENGTH_LONG).show();
			_realTimeListView.setAdapter(new DepartureAdapter(this, R.layout.realtime_result_row, new ArrayList<Departure>(), _actionBar.getTransportationType().getId()));
		}
	}

	/********************** Billing ***************************/

	private SLRealTimePurchaseObserver _realTimePurchaseObserver = null;
	private Handler _paymentHandler = null;
	private BillingService _billingService = null;

	/** An array of product list entries for the products that can be purchased. */

	private void setupBilling()
	{
		_paymentHandler = new Handler();
		_realTimePurchaseObserver = new SLRealTimePurchaseObserver(_paymentHandler);
		_billingService = new BillingService();
		_billingService.setContext(this);

		// Check if billing is supported.
		ResponseHandler.register(_realTimePurchaseObserver);

		if (UserPreferences.getInstance().getRestoreTransactions())
		{
			_billingService.restoreTransactions();
		}
		// _billingSupported = _billingService.checkBillingSupported();
	}

	private class SLRealTimePurchaseObserver extends PurchaseObserver
	{
		public SLRealTimePurchaseObserver(Handler handler)
		{
			super(SLRealTime.this, handler);
		}

		@Override
		public void onBillingSupported(boolean supported)
		{
			// _billingSupported = supported;
		}

		@Override
		public void onPurchaseStateChange(PurchaseState purchaseState, String itemId, int quantity, long purchaseTime, String developerPayload)
		{
			_log.info("PurchaseState: " + purchaseState.toString() + ", Item id: " + itemId);
			Donation donation = Database.getInstance().loadDonation(itemId);
			if (purchaseState == PurchaseState.PURCHASED)
			{
				if (donation == null)
				{
					donation = new Donation(itemId);
				}
				donation.setStatus(purchaseState.toString());
				Database.getInstance().saveDonation(donation);
				_bannerAdView.setVisibility(View.GONE);
				Toast.makeText(SLRealTime.this, R.string.BillingPurchasePurchased, Toast.LENGTH_LONG);
			}
			else if (purchaseState == PurchaseState.CANCELED)
			{
				if (donation != null)
				{
					Database.getInstance().deleteDonation(donation.getId());
					_bannerAdView.setVisibility(View.VISIBLE);
				}
				Toast.makeText(SLRealTime.this, R.string.BillingPurchaseCancelled, Toast.LENGTH_LONG);
			}
			else if (purchaseState == PurchaseState.REFUNDED)
			{
				if (donation != null)
				{
					Database.getInstance().deleteDonation(donation.getId());
					_bannerAdView.setVisibility(View.VISIBLE);
				}
				Toast.makeText(SLRealTime.this, R.string.BillingPurchaseRefunded, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onRequestPurchaseResponse(RequestPurchase request, ResponseCode responseCode)
		{
		}

		@Override
		public void onRestoreTransactionsResponse(RestoreTransactions request, ResponseCode responseCode)
		{
			_log.info("Restore Transactions Response: " + responseCode);
			if (responseCode == ResponseCode.RESULT_OK)
			{
				UserPreferences.getInstance().setRestoreTransactions(false);
				UserPreferences.getInstance().save();
			}
		}
	}
}