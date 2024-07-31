package com.vgsoftware.android.vglib.billing;

import com.android.vending.billing.IMarketBillingService;
import com.vgsoftware.android.vglib.billing.Constants.ResponseCode;
import com.vgsoftware.android.vglib.billing.Security.VerifiedPurchase;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class BillingService extends Service implements ServiceConnection
{
	private static final String TAG = "BillingService";

	private static IMarketBillingService _service;
	private static LinkedList<BillingRequest> _pendingRequests = new LinkedList<BillingRequest>();
	private static HashMap<Long, BillingRequest> _sentRequests = new HashMap<Long, BillingRequest>();

	public abstract class BillingRequest
	{
		private final int _startId;
		protected long _requestId;

		public BillingRequest(int startId)
		{
			_startId = startId;
		}

		public int getStartId()
		{
			return _startId;
		}

		public boolean runRequest()
		{
			if (runIfConnected())
			{
				return true;
			}

			if (bindToMarketBillingService())
			{
				_pendingRequests.add(this);
				return true;
			}
			return false;
		}

		public boolean runIfConnected()
		{
			if (_service != null)
			{
				try
				{
					_requestId = run();
					if (_requestId >= 0)
					{
						_sentRequests.put(_requestId, this);
					}
					return true;
				}
				catch (RemoteException e)
				{
					onRemoteException(e);
				}
			}
			return false;
		}

		protected void onRemoteException(RemoteException e)
		{
			Log.w(TAG, "remote billing service crashed");
			_service = null;
		}

		abstract protected long run() throws RemoteException;

		protected void responseCodeReceived(ResponseCode responseCode)
		{
		}

		protected Bundle makeRequestBundle(String method)
		{
			Bundle request = new Bundle();
			request.putString(Constants.BILLING_REQUEST_METHOD, method);
			request.putInt(Constants.BILLING_REQUEST_API_VERSION, 1);
			request.putString(Constants.BILLING_REQUEST_PACKAGE_NAME, getPackageName());
			return request;
		}

		protected void logResponseCode(String method, Bundle response)
		{
			// ResponseCode responseCode =
			// ResponseCode.valueOf(response.getInt(Constants.BILLING_RESPONSE_RESPONSE_CODE));
		}
	}

	public class CheckBillingSupported extends BillingRequest
	{
		public CheckBillingSupported()
		{
			super(-1);
		}

		@Override
		protected long run() throws RemoteException
		{
			Bundle request = makeRequestBundle("CHECK_BILLING_SUPPORTED");
			Bundle response = _service.sendBillingRequest(request);
			int responseCode = response.getInt(Constants.BILLING_RESPONSE_RESPONSE_CODE);
			boolean billingSupported = (responseCode == ResponseCode.RESULT_OK.ordinal());
			ResponseHandler.checkBillingSupportedResponse(billingSupported);
			return Constants.BILLING_RESPONSE_INVALID_REQUEST_ID;
		}
	}

	/**
	 * Wrapper class that requests a purchase.
	 */
	public class RequestPurchase extends BillingRequest
	{
		public final String _productId;
		public final String _developerPayload;

		public RequestPurchase(String itemId)
		{
			this(itemId, null);
		}

		public RequestPurchase(String itemId, String developerPayload)
		{
			// This object is never created as a side effect of starting this
			// service so we pass -1 as the startId to indicate that we should
			// not stop this service after executing this request.
			super(-1);
			_productId = itemId;
			_developerPayload = developerPayload;
		}

		@Override
		protected long run() throws RemoteException
		{
			Bundle request = makeRequestBundle("REQUEST_PURCHASE");
			request.putString(Constants.BILLING_REQUEST_ITEM_ID, _productId);
			// Note that the developer payload is optional.
			if (_developerPayload != null)
			{
				request.putString(Constants.BILLING_REQUEST_DEVELOPER_PAYLOAD, _developerPayload);
			}
			Bundle response = _service.sendBillingRequest(request);
			PendingIntent pendingIntent = response.getParcelable(Constants.BILLING_RESPONSE_PURCHASE_INTENT);
			if (pendingIntent == null)
			{
				Log.e(TAG, "Error with requestPurchase");
				return Constants.BILLING_RESPONSE_INVALID_REQUEST_ID;
			}

			Intent intent = new Intent();
			ResponseHandler.buyPageIntentResponse(pendingIntent, intent);
			return response.getLong(Constants.BILLING_RESPONSE_REQUEST_ID, Constants.BILLING_RESPONSE_INVALID_REQUEST_ID);
		}

		@Override
		protected void responseCodeReceived(ResponseCode responseCode)
		{
			ResponseHandler.responseCodeReceived(BillingService.this, this, responseCode);
		}
	}

	/**
	 * Wrapper class that confirms a list of notifications to the server.
	 */
	public class ConfirmNotifications extends BillingRequest
	{
		private final String[] _notifyIds;

		public ConfirmNotifications(int startId, String[] notifyIds)
		{
			super(startId);
			_notifyIds = notifyIds;
		}

		@Override
		protected long run() throws RemoteException
		{
			Bundle request = makeRequestBundle("CONFIRM_NOTIFICATIONS");
			request.putStringArray(Constants.BILLING_REQUEST_NOTIFY_IDS, _notifyIds);
			Bundle response = _service.sendBillingRequest(request);
			logResponseCode("confirmNotifications", response);
			return response.getLong(Constants.BILLING_RESPONSE_REQUEST_ID, Constants.BILLING_RESPONSE_INVALID_REQUEST_ID);
		}
	}

	/**
	 * Wrapper class that sends a GET_PURCHASE_INFORMATION message to the server.
	 */
	public class GetPurchaseInformation extends BillingRequest
	{
		private long _nonce;
		private final String[] _notifyIds;

		public GetPurchaseInformation(int startId, String[] notifyIds)
		{
			super(startId);
			_notifyIds = notifyIds;
		}

		@Override
		protected long run() throws RemoteException
		{
			_nonce = Security.generateNonce();

			Bundle request = makeRequestBundle("GET_PURCHASE_INFORMATION");
			request.putLong(Constants.BILLING_REQUEST_NONCE, _nonce);
			request.putStringArray(Constants.BILLING_REQUEST_NOTIFY_IDS, _notifyIds);
			Bundle response = _service.sendBillingRequest(request);
			logResponseCode("getPurchaseInformation", response);
			return response.getLong(Constants.BILLING_RESPONSE_REQUEST_ID, Constants.BILLING_RESPONSE_INVALID_REQUEST_ID);
		}

		@Override
		protected void onRemoteException(RemoteException e)
		{
			super.onRemoteException(e);
			Security.removeNonce(_nonce);
		}
	}

	/**
	 * Wrapper class that sends a RESTORE_TRANSACTIONS message to the server.
	 */
	public class RestoreTransactions extends BillingRequest
	{
		private long _nonce;

		public RestoreTransactions()
		{
			// This object is never created as a side effect of starting this
			// service so we pass -1 as the startId to indicate that we should
			// not stop this service after executing this request.
			super(-1);
		}

		@Override
		protected long run() throws RemoteException
		{
			_nonce = Security.generateNonce();

			Bundle request = makeRequestBundle("RESTORE_TRANSACTIONS");
			request.putLong(Constants.BILLING_REQUEST_NONCE, _nonce);
			Bundle response = _service.sendBillingRequest(request);
			logResponseCode("restoreTransactions", response);
			return response.getLong(Constants.BILLING_RESPONSE_REQUEST_ID, Constants.BILLING_RESPONSE_INVALID_REQUEST_ID);
		}

		@Override
		protected void onRemoteException(RemoteException e)
		{
			super.onRemoteException(e);
			Security.removeNonce(_nonce);
		}

		@Override
		protected void responseCodeReceived(ResponseCode responseCode)
		{
			ResponseHandler.responseCodeReceived(BillingService.this, this, responseCode);
		}
	}

	public BillingService()
	{
		super();
	}

	public void setContext(Context context)
	{
		attachBaseContext(context);
	}

	/**
	 * We don't support binding to this service, only starting the service.
	 */
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		handleCommand(intent, startId);
	}

	/**
	 * The {@link BillingReceiver} sends messages to this service using intents.
	 * Each intent has an action and some extra arguments specific to that action.
	 * 
	 * @param intent
	 *          the intent containing one of the supported actions
	 * @param startId
	 *          an identifier for the invocation instance of this service
	 */
	public void handleCommand(Intent intent, int startId)
	{
		if (intent != null)
		{
			String action = intent.getAction();
			if (Constants.ACTION_CONFIRM_NOTIFICATION.equals(action))
			{
				String[] notifyIds = intent.getStringArrayExtra(Constants.NOTIFICATION_ID);
				confirmNotifications(startId, notifyIds);
			}
			else if (Constants.ACTION_GET_PURCHASE_INFORMATION.equals(action))
			{
				String notifyId = intent.getStringExtra(Constants.NOTIFICATION_ID);
				getPurchaseInformation(startId, new String[] { notifyId });
			}
			else if (Constants.ACTION_PURCHASE_STATE_CHANGED.equals(action))
			{
				String signedData = intent.getStringExtra(Constants.INAPP_SIGNED_DATA);
				String signature = intent.getStringExtra(Constants.INAPP_SIGNATURE);
				purchaseStateChanged(startId, signedData, signature);
			}
			else if (Constants.ACTION_RESPONSE_CODE.equals(action))
			{
				long requestId = intent.getLongExtra(Constants.INAPP_REQUEST_ID, -1);
				int responseCodeIndex = intent.getIntExtra(Constants.INAPP_RESPONSE_CODE, ResponseCode.RESULT_ERROR.ordinal());
				ResponseCode responseCode = ResponseCode.valueOf(responseCodeIndex);
				checkResponseCode(requestId, responseCode);
			}
		}
	}

	/**
	 * Binds to the MarketBillingService and returns true if the bind succeeded.
	 * 
	 * @return true if the bind succeeded; false otherwise
	 */
	private boolean bindToMarketBillingService()
	{
		try
		{
			boolean bindResult = bindService(new Intent(Constants.MARKET_BILLING_SERVICE_ACTION), this, Context.BIND_AUTO_CREATE);
			if (bindResult)
			{
				return true;
			}
			Log.e(TAG, "Could not bind to service.");
		}
		catch (SecurityException e)
		{
			Log.e(TAG, "Security exception: " + e);
		}
		return false;
	}

	/**
	 * Checks if in-app billing is supported.
	 * 
	 * @return true if supported; false otherwise
	 */
	public boolean checkBillingSupported()
	{
		return new CheckBillingSupported().runRequest();
	}

	/**
	 * Requests that the given item be offered to the user for purchase. When the
	 * purchase succeeds (or is canceled) the {@link BillingReceiver} receives an
	 * intent with the action {@link Consts#ACTION_NOTIFY}. Returns false if there
	 * was an error trying to connect to Android Market.
	 * 
	 * @param productId
	 *          an identifier for the item being offered for purchase
	 * @param developerPayload
	 *          a payload that is associated with a given purchase, if null, no
	 *          payload is sent
	 * @return false if there was an error connecting to Android Market
	 */
	public boolean requestPurchase(String productId, String developerPayload)
	{
		return new RequestPurchase(productId, developerPayload).runRequest();
	}

	/**
	 * Requests transaction information for all managed items. Call this only when
	 * the application is first installed or after a database wipe. Do NOT call
	 * this every time the application starts up.
	 * 
	 * @return false if there was an error connecting to Android Market
	 */
	public boolean restoreTransactions()
	{
		return new RestoreTransactions().runRequest();
	}

	/**
	 * Confirms receipt of a purchase state change. Each {@code notifyId} is an
	 * opaque identifier that came from the server. This method sends those
	 * identifiers back to the MarketBillingService, which ACKs them to the
	 * server. Returns false if there was an error trying to connect to the
	 * MarketBillingService.
	 * 
	 * @param startId
	 *          an identifier for the invocation instance of this service
	 * @param notifyIds
	 *          a list of opaque identifiers associated with purchase state
	 *          changes.
	 * @return false if there was an error connecting to Market
	 */
	private boolean confirmNotifications(int startId, String[] notifyIds)
	{
		return new ConfirmNotifications(startId, notifyIds).runRequest();
	}

	/**
	 * Gets the purchase information. This message includes a list of notification
	 * IDs sent to us by Android Market, which we include in our request. The
	 * server responds with the purchase information, encoded as a JSON string,
	 * and sends that to the {@link BillingReceiver} in an intent with the action
	 * {@link Consts#ACTION_PURCHASE_STATE_CHANGED}. Returns false if there was an
	 * error trying to connect to the MarketBillingService.
	 * 
	 * @param startId
	 *          an identifier for the invocation instance of this service
	 * @param notifyIds
	 *          a list of opaque identifiers associated with purchase state
	 *          changes
	 * @return false if there was an error connecting to Android Market
	 */
	private boolean getPurchaseInformation(int startId, String[] notifyIds)
	{
		return new GetPurchaseInformation(startId, notifyIds).runRequest();
	}

	/**
	 * Verifies that the data was signed with the given signature, and calls
	 * {@link ResponseHandler#purchaseResponse(Context, PurchaseState, String, String, long)}
	 * for each verified purchase.
	 * 
	 * @param startId
	 *          an identifier for the invocation instance of this service
	 * @param signedData
	 *          the signed JSON string (signed, not encrypted)
	 * @param signature
	 *          the signature for the data, signed with the private key
	 */
	private void purchaseStateChanged(int startId, String signedData, String signature)
	{
		ArrayList<Security.VerifiedPurchase> purchases;
		purchases = Security.verifyPurchase(signedData, signature);
		if (purchases == null)
		{
			return;
		}

		ArrayList<String> notifyList = new ArrayList<String>();
		for (VerifiedPurchase vp : purchases)
		{
			if (vp.notificationId != null)
			{
				notifyList.add(vp.notificationId);
			}
			ResponseHandler.purchaseResponse(this, vp.purchaseState, vp.productId, vp.orderId, vp.purchaseTime, vp.developerPayload);
		}
		if (!notifyList.isEmpty())
		{
			String[] notifyIds = notifyList.toArray(new String[notifyList.size()]);
			confirmNotifications(startId, notifyIds);
		}
	}

	private void checkResponseCode(long requestId, ResponseCode responseCode)
	{
		BillingRequest request = _sentRequests.get(requestId);
		if (request != null)
		{
			request.responseCodeReceived(responseCode);
		}
		_sentRequests.remove(requestId);
	}

	private void runPendingRequests()
	{
		int maxStartId = -1;
		BillingRequest request;
		while ((request = _pendingRequests.peek()) != null)
		{
			if (request.runIfConnected())
			{
				_pendingRequests.remove();
				if (maxStartId < request.getStartId())
				{
					maxStartId = request.getStartId();
				}
			}
			else
			{
				bindToMarketBillingService();
				return;
			}
		}

		if (maxStartId >= 0)
		{
			stopSelf(maxStartId);
		}
	}

	public void onServiceConnected(ComponentName name, IBinder service)
	{
		_service = IMarketBillingService.Stub.asInterface(service);
		runPendingRequests();
	}

	public void onServiceDisconnected(ComponentName name)
	{
		Log.w(TAG, "Billing service disconnected");
		_service = null;
	}

	public void unbind()
	{
		try
		{
			unbindService(this);
		}
		catch (IllegalArgumentException e)
		{
			// This might happen if the service was disconnected
		}
	}
}
