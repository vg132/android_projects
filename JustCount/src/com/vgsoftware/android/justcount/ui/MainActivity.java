package com.vgsoftware.android.justcount.ui;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.vgsoftware.android.justcount.R;
import com.vgsoftware.android.justcount.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.justcount.ui.fragments.EditFragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> implements ActionBar.OnNavigationListener
{
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(
						actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1,
						new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3),
						}),
				this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM))
		{
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id)
	{
		Fragment fragment = new EditFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.container, fragment, "Tag");
		ft.commit();
		return true;
	}
}
