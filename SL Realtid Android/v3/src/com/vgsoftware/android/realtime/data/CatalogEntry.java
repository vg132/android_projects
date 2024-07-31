package com.vgsoftware.android.realtime.data;

public class CatalogEntry
{
	public enum Managed
	{
		MANAGED, UNMANAGED
	}

	private String _sku;
	private Managed _managed;

	public CatalogEntry(String sku, Managed managed)
	{
		this._sku = sku;
		this._managed = managed;
	}

	public String getSKU()
	{
		return _sku;
	}

	public Managed getManaged()
	{
		return _managed;
	}
}
