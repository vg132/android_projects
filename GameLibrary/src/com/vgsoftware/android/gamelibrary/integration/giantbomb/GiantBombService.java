package com.vgsoftware.android.gamelibrary.integration.giantbomb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Company;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Game;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Genre;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.ListCompanyResponse;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.ListGamesResponse;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.ListGenresResponse;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.ListPlatformsResponse;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Platform;
import com.vgsoftware.android.vglib.StringUtility;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

public class GiantBombService
{
	private IGiantBombService _service = null;
	private String _apiKey = null;
	private Map<String, List<Game>> _listGamesCache = new HashMap<String, List<Game>>();
	private Map<String, List<Company>> _listCompaniesCache = new HashMap<String, List<Company>>();

	public GiantBombService()
	{
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://www.giantbomb.com/api").build();
		_service = restAdapter.create(IGiantBombService.class);
		_apiKey = "";
	}

	public List<Game> listGames(String name)
	{
		if (StringUtility.isNullOrEmpty(name) || name.length() < 2)
		{
			return new ArrayList<Game>();
		}
		if (_listGamesCache.containsKey(name))
		{
			return _listGamesCache.get(name);
		}
		String filterValue = String.format("name:%s", name);
		ListGamesResponse response = _service.listGames(filterValue, _apiKey);
		_listGamesCache.put(name, response.results);
		return response.results != null ? response.results : new ArrayList<Game>();
	}

	public List<Company> listCompanies(String name)
	{
		if (StringUtility.isNullOrEmpty(name) || name.length() < 2)
		{
		}
		if (_listCompaniesCache.containsKey(name))
		{
			return _listCompaniesCache.get(name);
		}
		String filterValue = String.format("name:%s", name);
		ListCompanyResponse response = _service.listCompanies(filterValue, _apiKey);
		_listCompaniesCache.put(name, response.results);
		return response.results != null ? response.results : new ArrayList<Company>();
	}

	public List<Platform> listPlatforms()
	{
		List<Platform> platforms = new ArrayList<Platform>();
		ListPlatformsResponse response = null;
		int limit = 100;
		int offset = 0;
		do
		{
			response = _service.listPlatforms(limit, offset, _apiKey);
			platforms.addAll(response.results);
			offset += limit;
		} while ((response.offset + response.limit) < response.number_of_total_results);
		return platforms;
	}

	public List<Genre> listGenres()
	{
		List<Genre> genres = new ArrayList<Genre>();
		ListGenresResponse response = null;
		int limit = 100;
		int offset = 0;
		do
		{
			response = _service.listGenres(limit, offset, _apiKey);
			genres.addAll(response.results);
			offset += limit;
		} while ((response.offset + response.limit) < response.number_of_total_results);
		return genres;
	}

	public interface IGiantBombService
	{
		@GET("/games/?field_list=name,image,platforms,id&format=json")
		ListGamesResponse listGames(@Query("filter") String name, @Query("api_key") String apiKey);

		@GET("/companies/?filed_list=name,image,aliases&format=json")
		ListCompanyResponse listCompanies(@Query("filter") String name, @Query("api_key") String apiKey);

		@GET("/platforms/?field_list=abbreviation,name,id&format=json")
		ListPlatformsResponse listPlatforms(@Query("limit") int limit, @Query("offset") int offset, @Query("api_key") String apiKey);

		@GET("/genres/?field_list=id,name&format=json")
		ListGenresResponse listGenres(@Query("limit") int limit, @Query("offset") int offset, @Query("api_key") String apiKey);
	}
}
