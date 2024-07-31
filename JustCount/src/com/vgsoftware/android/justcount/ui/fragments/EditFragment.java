package com.vgsoftware.android.justcount.ui.fragments;

import java.util.Calendar;
import java.util.Date;

import com.vgsoftware.android.justcount.R;
import com.vgsoftware.android.justcount.dataabstraction.Count;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class EditFragment extends Fragment implements DatePickerDialog.OnDateSetListener
{
	private TextView _endDateTextView = null;
	private Date _selectedDate = null;
	private Button _clearDateButton = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
		final Button dateEndButton = (Button) rootView.findViewById(R.id.endDateButton);
		final Button saveButton = (Button) rootView.findViewById(R.id.saveButton);
		final CheckBox countdownCheckBox = (CheckBox) rootView.findViewById(R.id.countdownCheckbox);
		final EditText startEditView = (EditText) rootView.findViewById(R.id.startEditView);
		final EditText nameEditView = (EditText) rootView.findViewById(R.id.nameEditView);
		final EditText targetEditView = (EditText) rootView.findViewById(R.id.targetEditView);

		_endDateTextView = (TextView) rootView.findViewById(R.id.endDateTextView);
		_clearDateButton = (Button) rootView.findViewById(R.id.clearDateButton);

		_clearDateButton.setEnabled(false);
		startEditView.setVisibility(View.GONE);

		dateEndButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				new DatePickerDialog(getActivity(), EditFragment.this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		_clearDateButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				_endDateTextView.setText("");
				_selectedDate = null;
				_clearDateButton.setEnabled(false);
			}
		});

		countdownCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				targetEditView.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				startEditView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Count counter = new Count();
				counter.setName(nameEditView.getText().toString());
				counter.setCountdown(countdownCheckBox.isChecked());
				if (counter.getCountdown())
				{
					counter.setCounter(Integer.parseInt(startEditView.getText().toString()));
				}
				else
				{
					counter.setTarget(Integer.parseInt(targetEditView.getText().toString()));
				}
				counter.setStartDate(new Date(System.currentTimeMillis()));
				if (_selectedDate != null)
				{
					counter.setEndDate(_selectedDate);
				}
			}
		});

		return rootView;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	{
		monthOfYear++;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		_selectedDate = cal.getTime();
		_endDateTextView.setText(year + "-" + (monthOfYear >= 10 ? monthOfYear : "0" + monthOfYear) + "-" + (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth));
		_clearDateButton.setEnabled(true);
	}
}
