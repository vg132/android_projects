package com.vgsoftware.android.realtime.ui.controls;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.realtime.Tracking;
import com.vgsoftware.android.realtime.Utilities;
import com.vgsoftware.android.realtime.dataabstraction.Database;
import com.vgsoftware.android.realtime.dataabstraction.Station;
import com.vgsoftware.android.realtime.dataabstraction.TransportationType;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnFocusChangeListener;

public class ActionBar extends TableLayout implements OnEditorActionListener, OnFocusChangeListener
{
	private Context _context = null;
	private LayoutInflater _layoutInflater = null;
	private TableLayout _actionBar = null;
	private Button _transportationTypeButton = null;
	private ImageButton _searchImageButton = null;
	private StationAutoCompleteTextView _stationTextView = null;

	private TransportationType _selectedTransportationType = null;
	private QuickAction _quickAction = null;

	private OnSearchListener _searchListener = null;
	private OnTransportationTypeChangedListener _transportationTypeChangedListener = null;

	public ActionBar(Context context)
	{
		super(context);
		_context = context;
		setupActionBar();
	}

	public ActionBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		_context = context;
		setupActionBar();
	}

	private void setupActionBar()
	{
		_layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_actionBar = (TableLayout) _layoutInflater.inflate(R.layout.action_bar, null);
		addView(_actionBar);

		_transportationTypeButton = (Button) _actionBar.findViewById(R.id.transportationTypeButton);
		_stationTextView = (StationAutoCompleteTextView) _actionBar.findViewById(R.id.stationTextView);
		_searchImageButton = (ImageButton) _actionBar.findViewById(R.id.searchButton);

		setupQuickActionBar();
		setupListeners();

		_stationTextView.setOnFocusChangeListener(this);

		_stationTextView.clearFocus();
	}

	private void setupListeners()
	{
		_transportationTypeButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				_stationTextView.clearFocus();
				_quickAction.show(view);
			}
		});

		_quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener()
		{
			public void onItemClick(QuickAction source, int pos, int actionId)
			{
				ActionItem item = _quickAction.getActionItem(pos);
				if (item != null && _selectedTransportationType.getId() != item.getActionId())
				{
					setTransportationType(item.getActionId());
					_stationTextView.requestFocus();

					InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(_stationTextView, InputMethodManager.SHOW_IMPLICIT);

					if (_transportationTypeChangedListener != null)
					{
						_transportationTypeChangedListener.onTransportationTypeChanged(_selectedTransportationType);
					}
				}
			}
		});

		_searchImageButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				_actionBar.requestFocus();
				Station station = _stationTextView.getSelectedStation();
				if (_searchListener != null)
				{
					if (station != null)
					{
						Tracking.sendView(_context, "Search " + station.getName());
						setTransportationType(station.getTransportationTypeId());
						InputMethodManager inputMethodManager = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(_stationTextView.getWindowToken(), 0);
						_searchListener.onSearch(_selectedTransportationType, station);
					}
					else
					{
						Tracking.sendView(_context, "Search Failed: " + getText());
						_searchListener.onSearch(_selectedTransportationType, getText());
					}
				}
			}
		});
		_stationTextView.setOnEditorActionListener(this);
	}

	private void setupQuickActionBar()
	{
		_quickAction = new QuickAction(_context);
		for (TransportationType type : Database.getInstance().listTransportationTypes())
		{
			int resourceId = Utilities.getTransportationTypeDrawable(type.getId());
			_quickAction.addActionItem(new ActionItem(type.getId(), type.getName(), getResources().getDrawable(resourceId)));
		}
	}

	public void setText(String text)
	{
		_stationTextView.setText(text);
	}

	public String getText()
	{
		return _stationTextView.getText().toString();
	}

	public void setTransportationType(int id)
	{
		setTransportationType(Database.getInstance().loadTransportationType(id));
	}

	public void setTransportationType(TransportationType transportationType)
	{
		_selectedTransportationType = transportationType;
		int resourceId = Utilities.getTransportationTypeDrawable(_selectedTransportationType.getId());
		_transportationTypeButton.setCompoundDrawablesWithIntrinsicBounds(resourceId, 0, 0, 0);
		changeTransportationTypeDisplay(!_stationTextView.hasFocus());
		_stationTextView.setTransportationType(transportationType.getId());
		if (_transportationTypeChangedListener != null)
		{
			_transportationTypeChangedListener.onTransportationTypeChanged(_selectedTransportationType);
		}
	}

	public TransportationType getTransportationType()
	{
		return _selectedTransportationType;
	}

	public void setOnTransportationTypeChangeListener(OnTransportationTypeChangedListener transportationTypeChangedListener)
	{
		_transportationTypeChangedListener = transportationTypeChangedListener;
	}

	public void setOnSearchListener(OnSearchListener searchListener)
	{
		_searchListener = searchListener;
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	{
		if ((event == null || event.getAction() == KeyEvent.ACTION_UP) && actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
		{
			doSearch();
			return true;
		}
		return false;
	}

	public void doSearch()
	{
		_searchImageButton.performClick();
	}

	public void reloadStationList()
	{
		_stationTextView.reload();
	}

	public void onFocusChange(View v, boolean hasFocus)
	{
		changeTransportationTypeDisplay(!hasFocus);
	}

	private void changeTransportationTypeDisplay(boolean expand)
	{
		if (expand && _selectedTransportationType != null)
		{
			_transportationTypeButton.setCompoundDrawablePadding(10);
			_transportationTypeButton.setText(_selectedTransportationType.getName());
		}
		else
		{
			_transportationTypeButton.setCompoundDrawablePadding(0);
			_transportationTypeButton.setText("");
		}
	}

	public void expandTransportationTypeSelector()
	{
		_transportationTypeButton.performClick();
	}
}
