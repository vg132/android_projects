package com.vgsoftware.android.gamelibrary.ui;

import android.app.PendingIntent;
import android.content.Intent;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.vgsoftware.android.gamelibrary.model.DatabaseHelper;
import com.vgsoftware.android.gamelibrary.services.ActionService;
import com.vgsoftware.android.gamelibrary.services.ActionService.ActionType;

public abstract class ActionBaseActivity extends OrmLiteBaseActivity<DatabaseHelper>
{
	protected void startActionService(ActionType sendType, Intent extras)
	{
		PendingIntent pendingResult = createPendingResult(ActionService.ACTION_SERVICE_ID, new Intent(this, ActionBaseActivity.class), 0);
		extras.putExtra(ActionService.ACTION_TYPE, sendType);
		extras.putExtra(ActionService.PENDING_RESULT, pendingResult);
		startService(extras);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ActionService.ACTION_SERVICE_ID)
		{
			if (resultCode == ActionService.CODE_OK)
			{
				ActionType actionPerformed = (ActionType) data.getSerializableExtra(ActionService.WORK_DONE);
				processServiceReturn(actionPerformed, data);
			}
		}
	}
	
	protected void processServiceReturn(ActionType actionType, Intent extras)
	{
		switch(actionType)
		{
			case ACTION_LOOKUP_ITEM:
				processGameLookup(extras);
				break;
			case ACTION_SEARCH_GAME:
				processGamesList(extras);
				break;
			case ACTION_LIST_PLATFORMS:
				processGamesList(extras);
				break;
		}
	}
	
	protected void processPlatformList(Intent extras)
	{
	}

	protected void processGamesList(Intent extras)
	{
	}

	protected void processGameLookup(Intent extras)
	{
	}
}