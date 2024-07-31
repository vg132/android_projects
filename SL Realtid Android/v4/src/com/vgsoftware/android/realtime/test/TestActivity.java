package com.vgsoftware.android.realtime.test;

//import com.vgsoftware.android.realtime.R;

import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.realtime.R;

import android.app.Activity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class TestActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main_activity);
		RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
		recList.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recList.setLayoutManager(llm);
		ContactAdapter ca = new ContactAdapter(createList(30));
		recList.setAdapter(ca);
	}

	private List<ContactInfo> createList(int size)
	{
		List<ContactInfo> result = new ArrayList<ContactInfo>();
		for (int i = 1; i <= size; i++)
		{
			ContactInfo ci = new ContactInfo();
			ci.name = ContactInfo.NAME_PREFIX + i;
			ci.surname = ContactInfo.SURNAME_PREFIX + i;
			ci.email = ContactInfo.EMAIL_PREFIX + i + "@test.com";

			result.add(ci);
		}

		return result;
	}

	// private RecyclerView mRecyclerView;
	// @SuppressWarnings("rawtypes")
	// private RecyclerView.Adapter mAdapter;
	// private RecyclerView.LayoutManager mLayoutManager;
	//
	// @Override
	// protected void onCreate(Bundle savedInstanceState)
	// {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.test_main_activity);
	// mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
	//
	// // use this setting to improve performance if you know that changes
	// // in content do not change the layout size of the RecyclerView
	// mRecyclerView.setHasFixedSize(true);
	//
	// // use a linear layout manager
	// mLayoutManager = new LinearLayoutManager(this);
	// mRecyclerView.setLayoutManager(mLayoutManager);
	//
	// // specify an adapter (see also next example)
	// String[] myDataset = new String[] { "Viktor", "Gars" };
	// mAdapter = new MyAdapter(myDataset);
	// mRecyclerView.setAdapter(mAdapter);
	// }
	//
	// class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
	// {
	// private String[] mDataset;
	//
	// // Provide a reference to the views for each data item
	// // Complex data items may need more than one view per item, and
	// // you provide access to all the views for a data item in a view holder
	// public class ViewHolder extends RecyclerView.ViewHolder
	// {
	// // each data item is just a string in this case
	// public TextView mTextView;
	//
	// public ViewHolder(View v)
	// {
	// super(v);
	// mTextView = (TextView)v;
	// }
	// }
	//
	// // Provide a suitable constructor (depends on the kind of dataset)
	// public MyAdapter(String[] myDataset)
	// {
	// mDataset = myDataset;
	// }
	//
	// // Create new views (invoked by the layout manager)
	// @Override
	// public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int
	// viewType)
	// {
	// // create a new view
	// View v =
	// LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view,
	// parent, false);
	// // set the view's size, margins, paddings and layout parameters
	// ViewHolder vh = new ViewHolder(v);
	// return vh;
	// }
	//
	// // Replace the contents of a view (invoked by the layout manager)
	// @Override
	// public void onBindViewHolder(ViewHolder holder, int position)
	// {
	// // - get element from your dataset at this position
	// // - replace the contents of the view with that element
	// holder.mTextView.setText(mDataset[position]);
	//
	// }
	//
	// // Return the size of your dataset (invoked by the layout manager)
	// @Override
	// public int getItemCount()
	// {
	// return mDataset.length;
	// }
	// }
}
