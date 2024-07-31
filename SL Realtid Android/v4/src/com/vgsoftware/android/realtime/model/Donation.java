package com.vgsoftware.android.realtime.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Donation")
public class Donation
{
	@DatabaseField(generatedId = true, columnName = "Id")
	private int id;
	@DatabaseField(columnName = "ProductId", columnDefinition = "VARCHAR(100)")
	private String productId = null;
	@DatabaseField(columnName = "Status", columnDefinition = "VARCHAR(100)")
	private String status = null;

	public Donation()
	{
	}

	public Donation(String productId)
	{
		this.productId = productId;
	}

	public int getId()
	{
		return id;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}
