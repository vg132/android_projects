package com.vgsoftware.android.vglib.ui.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

public class DelayAutoCompleteTextView extends AutoCompleteTextView
{
	private static final int MESSAGE_TEXT_CHANGED = 100;
	private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;

	private int _autoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
	private ProgressBar _loadingIndicator;

	public DelayAutoCompleteTextView(Context context)
	{
		super(context);
	}

	public DelayAutoCompleteTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public DelayAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@SuppressLint("HandlerLeak")
	private final Handler _handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
		}
	};

	public void setLoadingIndicator(ProgressBar progressBar)
	{
		_loadingIndicator = progressBar;
	}

	public void setAutoCompleteDelay(int autoCompleteDelay)
	{
		_autoCompleteDelay = autoCompleteDelay;
	}

	@Override
	protected void performFiltering(CharSequence text, int keyCode)
	{
		if (_loadingIndicator != null)
		{
			_loadingIndicator.setVisibility(View.VISIBLE);
		}
		_handler.removeMessages(MESSAGE_TEXT_CHANGED);
		_handler.sendMessageDelayed(_handler.obtainMessage(MESSAGE_TEXT_CHANGED, text), _autoCompleteDelay);
	}

	@Override
	public void onFilterComplete(int count)
	{
		if (_loadingIndicator != null)
		{
			_loadingIndicator.setVisibility(View.GONE);
		}
		super.onFilterComplete(count);
	}
}
