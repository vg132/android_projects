package com.vgsoftware.android.justcount.dataabstraction;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Count
{
	@DatabaseField(id = true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private Date startDate;
	@DatabaseField
	private Date endDate;
	@DatabaseField
	private int counter;
	@DatabaseField
	private boolean countdown;
	@DatabaseField
	private int target;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public int getCounter()
	{
		return counter;
	}

	public void setCounter(int counter)
	{
		this.counter = counter;
	}

	public boolean getCountdown()
	{
		return countdown;
	}

	public void setCountdown(boolean countdown)
	{
		this.countdown = countdown;
	}

	public int getTarget()
	{
		return target;
	}

	public void setTarget(int target)
	{
		this.target = target;
	}
}
