package com.vgsoftware.android.realtime;

import java.util.Comparator;
import java.util.Date;

import com.vgsoftware.android.realtime.model.Departure;

public class DepartureComparator implements Comparator<Departure>
{
	@Override
	public int compare(Departure lhs, Departure rhs)
	{
		int value = 0;
		if (lhs.getTimeTabledDateTime() != null && rhs.getTimeTabledDateTime() != null)
		{
			Date lhsCompareDate = lhs.getExpectedDateTime() == null || lhs.getTimeTabledDateTime().getTime() == lhs.getExpectedDateTime().getTime() ? lhs.getTimeTabledDateTime() : lhs.getExpectedDateTime();
			Date rhsComparedate = rhs.getExpectedDateTime() == null || rhs.getTimeTabledDateTime().getTime() == rhs.getExpectedDateTime().getTime() ? rhs.getTimeTabledDateTime() : rhs.getExpectedDateTime();
			value = lhsCompareDate.compareTo(rhsComparedate);
		}
		return value;
	}
}
