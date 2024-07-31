package com.vgsoftware.android.vglib.billing;

import com.vgsoftware.android.vglib.billing.Constants.PurchaseState;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PurchaseDatabase
{
	private static final String TAG = "PurchaseDatabase";
	private static final String DATABASE_NAME = "purchase.db";
	private static final int DATABASE_VERSION = 1;
	private static final String PURCHASE_HISTORY_TABLE_NAME = "history";
	private static final String PURCHASED_ITEMS_TABLE_NAME = "purchased";

	static final String HISTORY_ORDER_ID_COL = "_id";
	static final String HISTORY_STATE_COL = "state";
	static final String HISTORY_PRODUCT_ID_COL = "productId";
	static final String HISTORY_PURCHASE_TIME_COL = "purchaseTime";
	static final String HISTORY_DEVELOPER_PAYLOAD_COL = "developerPayload";

	private static final String[] HISTORY_COLUMNS = { HISTORY_ORDER_ID_COL, HISTORY_PRODUCT_ID_COL, HISTORY_STATE_COL, HISTORY_PURCHASE_TIME_COL, HISTORY_DEVELOPER_PAYLOAD_COL };

	static final String PURCHASED_PRODUCT_ID_COL = "_id";
	static final String PURCHASED_QUANTITY_COL = "quantity";

	private static final String[] PURCHASED_COLUMNS = { PURCHASED_PRODUCT_ID_COL, PURCHASED_QUANTITY_COL };

	private SQLiteDatabase mDb;
	private DatabaseHelper mDatabaseHelper;

	public PurchaseDatabase(Context context)
	{
		mDatabaseHelper = new DatabaseHelper(context);
		mDb = mDatabaseHelper.getWritableDatabase();
	}

	public void close()
	{
		mDatabaseHelper.close();
	}

	private void insertOrder(String orderId, String productId, PurchaseState state, long purchaseTime, String developerPayload)
	{
		ContentValues values = new ContentValues();
		values.put(HISTORY_ORDER_ID_COL, orderId);
		values.put(HISTORY_PRODUCT_ID_COL, productId);
		values.put(HISTORY_STATE_COL, state.ordinal());
		values.put(HISTORY_PURCHASE_TIME_COL, purchaseTime);
		values.put(HISTORY_DEVELOPER_PAYLOAD_COL, developerPayload);
		mDb.replace(PURCHASE_HISTORY_TABLE_NAME, null /* nullColumnHack */, values);
	}

	private void updatePurchasedItem(String productId, int quantity)
	{
		if (quantity == 0)
		{
			mDb.delete(PURCHASED_ITEMS_TABLE_NAME, PURCHASED_PRODUCT_ID_COL + "=?", new String[] { productId });
			return;
		}
		ContentValues values = new ContentValues();
		values.put(PURCHASED_PRODUCT_ID_COL, productId);
		values.put(PURCHASED_QUANTITY_COL, quantity);
		mDb.replace(PURCHASED_ITEMS_TABLE_NAME, null /* nullColumnHack */, values);
	}

	public synchronized int updatePurchase(String orderId, String productId, PurchaseState purchaseState, long purchaseTime, String developerPayload)
	{
		insertOrder(orderId, productId, purchaseState, purchaseTime, developerPayload);
		Cursor cursor = mDb.query(PURCHASE_HISTORY_TABLE_NAME, HISTORY_COLUMNS, HISTORY_PRODUCT_ID_COL + "=?", new String[] { productId }, null, null, null, null);
		if (cursor == null)
		{
			return 0;
		}
		int quantity = 0;
		try
		{
			// Count the number of times the product was purchased
			while (cursor.moveToNext())
			{
				int stateIndex = cursor.getInt(2);
				PurchaseState state = PurchaseState.valueOf(stateIndex);
				if (state == PurchaseState.PURCHASED || state == PurchaseState.REFUNDED)
				{
					quantity += 1;
				}
			}
			updatePurchasedItem(productId, quantity);
		}
		finally
		{
			cursor.close();
		}
		return quantity;
	}

	public Cursor queryAllPurchasedItems()
	{
		return mDb.query(PURCHASED_ITEMS_TABLE_NAME, PURCHASED_COLUMNS, null, null, null, null, null);
	}

	private class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			createPurchaseTable(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			if (newVersion != DATABASE_VERSION)
			{
				Log.w(TAG, "Database upgrade from old: " + oldVersion + " to: " + newVersion);
				db.execSQL("DROP TABLE IF EXISTS " + PURCHASE_HISTORY_TABLE_NAME);
				db.execSQL("DROP TABLE IF EXISTS " + PURCHASED_ITEMS_TABLE_NAME);
				createPurchaseTable(db);
				return;
			}
		}

		private void createPurchaseTable(SQLiteDatabase db)
		{
			db.execSQL("CREATE TABLE " + PURCHASE_HISTORY_TABLE_NAME + "(" + HISTORY_ORDER_ID_COL + " TEXT PRIMARY KEY, " + HISTORY_STATE_COL + " INTEGER, " + HISTORY_PRODUCT_ID_COL + " TEXT, " + HISTORY_DEVELOPER_PAYLOAD_COL + " TEXT, " + HISTORY_PURCHASE_TIME_COL + " INTEGER)");
			db.execSQL("CREATE TABLE " + PURCHASED_ITEMS_TABLE_NAME + "(" + PURCHASED_PRODUCT_ID_COL + " TEXT PRIMARY KEY, " + PURCHASED_QUANTITY_COL + " INTEGER)");
		}
	}
}
