package com.vgsoftware.android.fastcheckin.dataabstraction;

import org.json.JSONException;
import org.json.JSONObject;

import com.vgsoftware.android.fastcheckin.GeoLocation;

public class Place
{
	private String _id = null;
	private String _name = null;
	private String _category = null;
	private GeoLocation _location = null;

	public Place()
	{
	}
	
	public Place(JSONObject data)
	throws JSONException
	{
		if(data!=null)
		{
			setName(data.getString("name"));
			setId(data.getString("id"));
			setCategory(data.getString("category"));
			JSONObject location = data.getJSONObject("location");
			if(location!=null)
			{
				GeoLocation geoLocation=new GeoLocation();
				geoLocation.setLatitude(location.getDouble("latitude"));
				geoLocation.setLongitude(location.getDouble("longitude"));
			}
		}
	}
	
	public String getId()
	{
		return _id;
	}
	public void setId(String id)
	{
		this._id = id;
	}
	public String getName()
	{
		return _name;
	}
	public void setName(String name)
	{
		this._name = name;
	}
	public String getCategory()
	{
		return _category;
	}
	public void setCategory(String category)
	{
		this._category = category;
	}
	public GeoLocation getGeoLocation()
	{
		return _location;
	}
	public void setGeoLocation(GeoLocation location)
	{
		this._location = location;
	}
}
