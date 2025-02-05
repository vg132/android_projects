package com.vgsoftware.android.realtime.test;

import com.vgsoftware.android.realtime.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ContactViewHolder extends RecyclerView.ViewHolder
{
	protected TextView vName;
	protected TextView vSurname;
	protected TextView vEmail;
	protected TextView vTitle;

	public ContactViewHolder(View v)
	{
		super(v);
		vName = (TextView) v.findViewById(R.id.txtName);
		vSurname = (TextView) v.findViewById(R.id.txtSurname);
		vEmail = (TextView) v.findViewById(R.id.txtEmail);
		vTitle = (TextView) v.findViewById(R.id.title);
	}
}
