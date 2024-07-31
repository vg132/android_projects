package com.vgsoftware.android.gamelibrary.ui.controls;

import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Game;
import com.vgsoftware.android.gamelibrary.ui.adapters.GameTitleSuggestionAdapter;
import com.vgsoftware.android.vglib.ui.controls.DelayAutoCompleteTextView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class GameTitleAutoCompleteTextView extends DelayAutoCompleteTextView implements OnItemClickListener
{
	private Context _context = null;
	private Game _selectedGame = null;

	public GameTitleAutoCompleteTextView(Context context)
	{
		super(context);
		setupControl(context);
	}

	public GameTitleAutoCompleteTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupControl(context);
	}

	public GameTitleAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setupControl(context);
	}

	private void setupControl(Context context)
	{
		_context = context;	
		setThreshold(2);
		setAdapter(new GameTitleSuggestionAdapter(_context));
		setOnItemClickListener(this);
	}

	public Game getSelectedGame()
	{
		return _selectedGame;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		_selectedGame = (Game) getAdapter().getItem(position);
		if (_selectedGame != null)
		{
			setText(_selectedGame.name);
		}
	}
}
