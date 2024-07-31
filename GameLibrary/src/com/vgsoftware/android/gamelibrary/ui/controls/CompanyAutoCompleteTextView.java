package com.vgsoftware.android.gamelibrary.ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Company;
import com.vgsoftware.android.gamelibrary.ui.adapters.CompanySuggestionAdapter;
import com.vgsoftware.android.vglib.ui.controls.DelayAutoCompleteTextView;

public class CompanyAutoCompleteTextView extends DelayAutoCompleteTextView implements OnItemClickListener
{
	public CompanyAutoCompleteTextView(Context context)
	{
		super(context);
		setupControl(context);
	}

	public CompanyAutoCompleteTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupControl(context);
	}

	public CompanyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setupControl(context);
	}

	private void setupControl(Context context)
	{
		setThreshold(2);
		setAdapter(new CompanySuggestionAdapter(context));
		setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Company company = (Company) getAdapter().getItem(position);
		if (company != null)
		{
			setText(company.name);
		}
	}
}
