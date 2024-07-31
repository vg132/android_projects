package com.vgsoftware.android.gamelibrary.ui.fragments;

import com.vgsoftware.android.gamelibrary.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmptyListFragment extends Fragment
{
	public EmptyListFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_emptylist, container, false);
		return view;
	}
}
