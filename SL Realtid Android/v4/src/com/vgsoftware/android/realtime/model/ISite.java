package com.vgsoftware.android.realtime.model;

import android.os.Parcelable;

public interface ISite extends Parcelable
{
	int getSiteId();
	String getName();
	String getArea();
}
