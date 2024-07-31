package com.vgsoftware.android.realtime.test;

import java.util.List;

import com.vgsoftware.android.realtime.R;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder>
{
	private List<ContactInfo> contactList;

	public ContactAdapter(List<ContactInfo> contactList)
	{
		this.contactList = contactList;
	}

	@Override
	public int getItemCount()
	{
		return contactList.size();
	}

	@Override
	public void onBindViewHolder(ContactViewHolder contactViewHolder, int i)
	{
		ContactInfo ci = contactList.get(i);
		contactViewHolder.vName.setText(ci.name);
		contactViewHolder.vSurname.setText(ci.surname);
		contactViewHolder.vEmail.setText(ci.email);
		contactViewHolder.vTitle.setText(ci.name + " " + ci.surname);
	}

	@Override
	public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
	{
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_cart_view, viewGroup, false);
		return new ContactViewHolder(itemView);
	}

}
