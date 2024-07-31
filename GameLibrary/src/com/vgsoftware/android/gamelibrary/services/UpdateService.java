package com.vgsoftware.android.gamelibrary.services;

import java.util.Date;
import java.util.List;

import com.vgsoftware.android.gamelibrary.LogManager;
import com.vgsoftware.android.gamelibrary.Settings;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.GiantBombService;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Genre;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Platform;
import com.vgsoftware.android.gamelibrary.model.DataStore;
import com.vgsoftware.android.vglib.DateDiff;
import com.vgsoftware.android.vglib.DateUtility;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateService extends Service
{
	private GiantBombService _giantBombService = null;

	@Override
	public void onCreate()
	{
		super.onCreate();
		_giantBombService = new GiantBombService();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				DateDiff diff = DateUtility.dateDiff(Settings.getInstance().getLastUpdate(), new Date(System.currentTimeMillis()));
				if (diff.getDays() > 7)
				{
					LogManager.info("Time to update genres and platforms");
					int genreCount = DataStore.getInstance().listGenres().size();
					if (genreCount <= 0)
					{
						List<Genre> genres = _giantBombService.listGenres();
						for (Genre genre : genres)
						{
							DataStore.getInstance().saveGenre(new com.vgsoftware.android.gamelibrary.model.Genre(genre), false);
						}
					}
					int platformCount = DataStore.getInstance().listPlatforms().size();
					if (platformCount <= 0)
					{
						List<Platform> platforms = _giantBombService.listPlatforms();
						for (Platform platform : platforms)
						{
							DataStore.getInstance().savePlatform(new com.vgsoftware.android.gamelibrary.model.Platform(platform), false);
						}
					}
					Settings.getInstance().setLastUpdated(System.currentTimeMillis());
				}
				else
				{
					LogManager.info("Not time to update genras and platforms");
				}
				UpdateService.this.stopSelf();
			}
		}.start();
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}
