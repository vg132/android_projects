package com.vgsoftware.android.vglib.billing;

import com.vgsoftware.android.vglib.billing.BillingService.RequestPurchase;
import com.vgsoftware.android.vglib.billing.BillingService.RestoreTransactions;
import com.vgsoftware.android.vglib.billing.Constants.PurchaseState;
import com.vgsoftware.android.vglib.billing.Constants.ResponseCode;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ResponseHandler
{
	private static final String TAG = "ResponseHandler";

	private static PurchaseObserver sPurchaseObserver;

	public static synchronized void register(PurchaseObserver observer)
	{
		Log.i(TAG, "Register observer");
		sPurchaseObserver = observer;
	}

	public static synchronized void unregister(PurchaseObserver observer)
	{
		Log.i(TAG, "Unregister observer");
		sPurchaseObserver = null;
	}

	public static void checkBillingSupportedResponse(boolean supported)
	{
		Log.i(TAG, "Billing supported");
		if (sPurchaseObserver != null)
		{
			Log.i(TAG, "Call observer billing supported");
			sPurchaseObserver.onBillingSupported(supported);
		}
	}

	public static void buyPageIntentResponse(PendingIntent pendingIntent, Intent intent)
	{
		Log.i(TAG, "BuyPageIntentResponse");
		if (sPurchaseObserver == null)
		{
			return;
		}
		Log.i(TAG, "Start BuyPageActivity");
		sPurchaseObserver.startBuyPageActivity(pendingIntent, intent);
	}

	public static void purchaseResponse(final Context context, final PurchaseState purchaseState, final String productId, final String orderId, final long purchaseTime, final String developerPayload)
	{
		Log.i(TAG, "Purchase response");
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Log.i(TAG, "Response thread started");
					PurchaseDatabase db = new PurchaseDatabase(context);
					int quantity = db.updatePurchase(orderId, productId, purchaseState, purchaseTime, developerPayload);
					db.close();
					synchronized (ResponseHandler.class)
					{
						if (sPurchaseObserver != null)
						{
							Log.i(TAG, "Call observer for purchase response change");
							sPurchaseObserver.postPurchaseStateChange(purchaseState, productId, quantity, purchaseTime, developerPayload);
						}
					}
				}
				catch (Exception ex)
				{
					Log.e(TAG, "Exception when handling purchase response", ex);
				}
			}
		}).start();
	}

	public static void responseCodeReceived(Context context, RequestPurchase request, ResponseCode responseCode)
	{
		Log.i(TAG, "responseCodeRecived: " + responseCode.name());
		if (sPurchaseObserver != null)
		{
			Log.i(TAG, "responseCodeReceived: sending to observer");
			sPurchaseObserver.onRequestPurchaseResponse(request, responseCode);
		}
	}

	public static void responseCodeReceived(Context context, RestoreTransactions request, ResponseCode responseCode)
	{
		Log.i(TAG, "responseCodeReceived: " + responseCode.name());
		if (sPurchaseObserver != null)
		{
			Log.i(TAG, "responseCodeReceived: sending to observer");
			sPurchaseObserver.onRestoreTransactionsResponse(request, responseCode);
		}
	}
}
