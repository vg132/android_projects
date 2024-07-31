package com.vgsoftware.android.gamelibrary.integration.amazon;

import com.vgsoftware.android.gamelibrary.integration.amazon.models.GameInfo;
import com.vgsoftware.android.vglib.StringUtility;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

public class AmazonService
{
	private IAmazonService _service = null;

	public AmazonService()
	{
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://gamelibrary.vgsoftware.com:11000").build();
		_service = restAdapter.create(IAmazonService.class);
	}

	public GameInfo getTitle(String ean)
	{
		if (StringUtility.isNullOrEmpty(ean))
		{
			return null;
		}
		return _service.getTitle(ean);
	}

	public interface IAmazonService
	{
		@GET("/search/{ean}")
		GameInfo getTitle(@Path("ean") String ean);
	}
}
