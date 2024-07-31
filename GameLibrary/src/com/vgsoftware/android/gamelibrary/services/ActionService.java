package com.vgsoftware.android.gamelibrary.services;

import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.gamelibrary.LogManager;
import com.vgsoftware.android.gamelibrary.integration.amazon.AmazonService;
import com.vgsoftware.android.gamelibrary.integration.amazon.models.GameInfo;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.GiantBombService;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Game;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ActionService extends IntentService
{
	public final static int ACTION_SERVICE_ID = 111;

	public final static String PENDING_RESULT = "PENDING_RESULT";
	public final static String ACTION_TYPE = "ACTION_TYPE";
	public final static String RESULT_CODE = "RESULT_CODE";
	public final static String WORK_DONE = "WORK_DONE";

	public final static String EAN_CODE = "ean_code";
	public final static String GAME_INFO = "game_info";
	public final static String GAME_LIST = "game_list";
	public final static String SEARCH_QUERY = "search_query";

	public final static int CODE_OK = 0;
	public final static int CODE_ERROR = 1;

	private AmazonService _amazonService = null;
	private GiantBombService _giantBombService = null;
	private Context _context = null;

	public enum ActionType
	{
		ACTION_LOOKUP_ITEM, ACTION_SEARCH_GAME, ACTION_LIST_PLATFORMS
	};

	public ActionService()
	{
		super("ActionService");
		_context = this;

		_amazonService = new AmazonService();
		_giantBombService = new GiantBombService();
	}

	@Override
	protected void onHandleIntent(final Intent incomingIntent)
	{
		new Thread()
		{
			public void run()
			{
				doWork(incomingIntent);
			};
		}.start();
	}

	private void doWork(final Intent incomingIntent)
	{
		ActionType workToDo = (ActionType) incomingIntent.getSerializableExtra(ActionService.ACTION_TYPE);
		PendingIntent pendingResult = incomingIntent.getParcelableExtra(ActionService.PENDING_RESULT);
		Intent returnIntent = new Intent();

		switch (workToDo)
		{
			case ACTION_LOOKUP_ITEM:
				returnIntent = doLookupGame(incomingIntent);
				break;
			case ACTION_SEARCH_GAME:
				returnIntent = doSearchGame(incomingIntent);
				break;
			case ACTION_LIST_PLATFORMS:
				break;
		}
		try
		{
			returnIntent.putExtra(WORK_DONE, workToDo);
			int statusCode = returnIntent != null ? ActionService.CODE_OK : ActionService.CODE_ERROR;
			pendingResult.send(_context, statusCode, returnIntent);
		}
		catch (PendingIntent.CanceledException e)
		{
			LogManager.error("Pending intent was canceled", e);
		}
	}

	private Intent doLookupGame(final Intent incomingIntent)
	{
		Intent returnIntent = new Intent();
		String eanCode = incomingIntent.getStringExtra(ActionService.EAN_CODE);
		GameInfo gameInfo = _amazonService.getTitle(eanCode);
		returnIntent.putExtra(ActionService.GAME_INFO, gameInfo);
		returnIntent.putExtra(ActionService.EAN_CODE, eanCode);
		return returnIntent;
	}

	private Intent doSearchGame(final Intent incomingIntent)
	{
		Intent returnIntent = new Intent();
		String query = incomingIntent.getStringExtra(ActionService.SEARCH_QUERY);
		List<Game> games = _giantBombService.listGames(query);
		returnIntent.putParcelableArrayListExtra(ActionService.GAME_LIST, new ArrayList<Game>(games));
		returnIntent.putExtra(ActionService.SEARCH_QUERY, query);
		return returnIntent;
	}
}
