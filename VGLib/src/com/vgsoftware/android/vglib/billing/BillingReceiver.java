package com.vgsoftware.android.vglib.billing;

import com.vgsoftware.android.vglib.billing.Constants.ResponseCode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BillingReceiver extends BroadcastReceiver
{
	private static final String TAG = "BillingReceiver";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (Constants.ACTION_PURCHASE_STATE_CHANGED.equals(action))
		{
			String signedData = intent.getStringExtra(Constants.INAPP_SIGNED_DATA);
			String signature = intent.getStringExtra(Constants.INAPP_SIGNATURE);
			purchaseStateChanged(context, signedData, signature);
		}
		else if (Constants.ACTION_NOTIFY.equals(action))
		{
			String notifyId = intent.getStringExtra(Constants.NOTIFICATION_ID);
			notify(context, notifyId);
		}
		else if (Constants.ACTION_RESPONSE_CODE.equals(action))
		{
			long requestId = intent.getLongExtra(Constants.INAPP_REQUEST_ID, -1);
			int responseCodeIndex = intent.getIntExtra(Constants.INAPP_RESPONSE_CODE, ResponseCode.RESULT_ERROR.ordinal());
			checkResponseCode(context, requestId, responseCodeIndex);
		}
		else
		{
			Log.w(TAG, "unexpected action: " + action);
		}
	}

	private void purchaseStateChanged(Context context, String signedData, String signature)
	{
		Intent intent = new Intent(Constants.ACTION_PURCHASE_STATE_CHANGED);
		intent.setClass(context, BillingService.class);
		intent.putExtra(Constants.INAPP_SIGNED_DATA, signedData);
		intent.putExtra(Constants.INAPP_SIGNATURE, signature);
		context.startService(intent);
	}

	private void notify(Context context, String notifyId)
	{
		Intent intent = new Intent(Constants.ACTION_GET_PURCHASE_INFORMATION);
		intent.setClass(context, BillingService.class);
		intent.putExtra(Constants.NOTIFICATION_ID, notifyId);
		context.startService(intent);
	}

	private void checkResponseCode(Context context, long requestId, int responseCodeIndex)
	{
		Intent intent = new Intent(Constants.ACTION_RESPONSE_CODE);
		intent.setClass(context, BillingService.class);
		intent.putExtra(Constants.INAPP_REQUEST_ID, requestId);
		intent.putExtra(Constants.INAPP_RESPONSE_CODE, responseCodeIndex);
		context.startService(intent);
	}
}
