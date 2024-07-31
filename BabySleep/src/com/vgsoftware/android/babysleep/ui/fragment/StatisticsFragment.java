package com.vgsoftware.android.babysleep.ui.fragment;

import com.vgsoftware.android.babysleep.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatisticsFragment extends Fragment implements IFragmentName
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_start, container, false);
		TextView dummyTextView = (TextView) rootView.findViewById(android.R.id.text1);
		dummyTextView.setText("This is Statistics");
		return rootView;
	}

	@Override
	public String getName()
	{
		return "Statistics";
	}
}
