package com.vgsoftware.android.gamelibrary.ui.fragments;

import java.util.List;

import com.vgsoftware.android.gamelibrary.R;
import com.vgsoftware.android.gamelibrary.model.DataStore;
import com.vgsoftware.android.gamelibrary.model.Game;
import com.vgsoftware.android.gamelibrary.model.IOnDataChanged;
import com.vgsoftware.android.gamelibrary.model.Platform;
import com.vgsoftware.android.gamelibrary.ui.adapters.GameListAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

public class GameListFragment extends Fragment implements IOnDataChanged
{
	private List<Game> _games = null;
	private ListView _gameList = null;
	private Platform _platform = null;
	private GameListAdapter _gameListAdapter = null;

	public GameListFragment(Platform platform)
	{
		super();

		DataStore.getInstance().registerOnDataChangedListener(this);
		_platform = platform;
		_games = DataStore.getInstance().listGames(_platform);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_gamelist, container, false);
		if (_games != null)
		{
			_gameList = (ListView) view.findViewById(android.R.id.list);
			_gameListAdapter = new GameListAdapter(getActivity(), _games);
			_gameList.setAdapter(_gameListAdapter);
			_gameList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			_gameList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener()
			{
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu)
				{
					return true;
				}

				@Override
				public void onDestroyActionMode(ActionMode mode)
				{
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu)
				{
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.menu_context, menu);
					return true;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem)
				{
					switch (menuItem.getItemId())
					{
						case R.id.menu_delete:
							SparseBooleanArray checked = _gameList.getCheckedItemPositions();
							List<Game> games = _games;
							for (int i = 0; i < games.size(); i++)
							{
								if (checked.get(i))
								{
									DataStore.getInstance().deleteGame(games.get(i));
								}
							}
							mode.finish();
							return true;
						case R.id.menu_select_all:
							for (int i = 0; i < _games.size(); i++)
							{
								_gameList.setItemChecked(i, true);
							}
							return true;
						default:
							return false;
					}
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
				{
					mode.setTitle(_gameList.getCheckedItemCount() + " selected");
				}
			});

		}
		return view;
	}

	@Override
	public void onDataChanged()
	{
		_games = DataStore.getInstance().listGames(_platform);
		if (_gameListAdapter != null)
		{
			_gameListAdapter.setGames(_games);
			_gameListAdapter.notifyDataSetChanged();
		}
	}
}