package com.vgsoftware.android.polisen.dataabstraction;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Region
{
	@DatabaseField(id = true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private boolean active;

	public Region()
	{
	}

	public Region(int id, String name, boolean active)
	{
		this.id = id;
		this.name = name;
		this.active = active;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public boolean getActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}
}
