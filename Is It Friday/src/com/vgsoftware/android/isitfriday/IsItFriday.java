package com.vgsoftware.android.isitfriday;

import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IsItFriday extends Activity implements Runnable
{
	private LinearLayout _noLinearLayout;
	private LinearLayout _yesLinearLayout;
	private TextView _countdown;
	private TextView _messageTextView;
	private TextView _yesMessageTextView;
	private Thread _timerThread;
	private static Random _random = new Random();
	private final static int DAY = 6;

	@Override
	protected void onStop()
	{
		_timerThread.interrupt();
		_timerThread.stop();
		super.onStop();
	}

	@Override
	protected void onResume()
	{
		checkIfItIsFriday();

		_timerThread = new Thread(this);
		_timerThread.start();

		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		_yesLinearLayout = (LinearLayout) findViewById(R.id.YesLinearLayout);
		_noLinearLayout = (LinearLayout) findViewById(R.id.NoLinearLayout);
		_messageTextView = (TextView) findViewById(R.id.MessageTextView);
		_yesMessageTextView = (TextView) findViewById(R.id.YesMessageTextView);
		_countdown = (TextView) findViewById(R.id.Countdown);

		checkIfItIsFriday();
	}

	private long _lastMessageUpdate;
	private int _lastDay;

	private void checkIfItIsFriday()
	{
		Calendar calendar = Calendar.getInstance();
		boolean updateMessage=false;
		if ((_lastMessageUpdate < System.currentTimeMillis()) || calendar.get(Calendar.DAY_OF_WEEK) != _lastDay)
		{
			_lastMessageUpdate = System.currentTimeMillis() + 60000;
			_lastDay = calendar.get(Calendar.DAY_OF_WEEK);
			updateMessage=true;
		}
		if (isItFriday())
		{
			_yesLinearLayout.setVisibility(View.VISIBLE);
			if(updateMessage)
			{
				_yesMessageTextView.setText(getMessage());
			}
			_noLinearLayout.setVisibility(View.GONE);
			_countdown.setVisibility(View.GONE);
		}
		else
		{
			_yesLinearLayout.setVisibility(View.GONE);
			_noLinearLayout.setVisibility(View.VISIBLE);
			_countdown.setVisibility(View.VISIBLE);
			if(updateMessage)
			{
				_messageTextView.setText(getMessage());
			}
			_countdown.setText(formatCountdown());
			_noLinearLayout.invalidate();
		}
	}

	private String getMessage()
	{
		if (isItFriday())
		{
			switch (_random.nextInt(5))
			{
			case 1:
				return getString(R.string.Finally2);
			case 2:
				return getString(R.string.Finally3);
			case 3:
				return getString(R.string.Finally4);
			case 4:
				return getString(R.string.Finally5);
			default:
				return getString(R.string.Finally);
			}
		}
		else
		{
			Calendar calendar = Calendar.getInstance();
			if (_random.nextBoolean() && calendar.get(Calendar.DAY_OF_WEEK) == 2)
			{
				return getString(R.string.ItsMonday);
			}
			long time = getTimeLeft();
			if (time > 259200000)
			{
				return getString(R.string.NotEvenClose);
			}
			else
			{
				return getString(R.string.Almost);
			}
		}
	}

	private boolean isItFriday()
	{
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == IsItFriday.DAY;
	}

	private long getNextFriday()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day > IsItFriday.DAY)
		{
			calendar.add(Calendar.DAY_OF_WEEK, IsItFriday.DAY);
		}
		else
		{
			calendar.add(Calendar.DAY_OF_WEEK, IsItFriday.DAY - day);
		}
		return calendar.getTimeInMillis();
	}

	private long getTimeLeft()
	{
		return getNextFriday() - System.currentTimeMillis();
	}

	public CharSequence formatCountdown()
	{
		long endDate = getTimeLeft();
		TimeSpan ts = new TimeSpan(endDate);
		return ts.getDays() + " days, " + ts.getHours() + " hours, " + (ts.getMinutes() > 9 ? ts.getMinutes() : "0" + ts.getMinutes()) + " minutes and " + (ts.getSeconds() > 9 ? ts.getSeconds() : "0" + ts.getSeconds()) + " seconds until friday";
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			checkIfItIsFriday();
		}
	};

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				handler.sendEmptyMessage(0);
				Thread.sleep(1000);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}