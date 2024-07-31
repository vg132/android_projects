package com.vgsoftware.android.realtime.ui.controls;

import com.vgsoftware.android.realtime.Utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TransportationTypeImageView extends ImageView
{
	public TransportationTypeImageView(Context context)
	{
		super(context);
	}

	public TransportationTypeImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TransportationTypeImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void setTransportationTypeId(int typeId)
	{
		setTransportationTypeId(typeId, "-1");
	}

	public void setTransportationTypeId(int typeId, String lineId)
	{
		setImageDrawable(getContext().getResources().getDrawable(Utilities.getTransportationTypeDrawable(typeId, lineId)));
	}
}
