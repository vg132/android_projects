/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vgsoftware.android.fastcheckin.ui.controls;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.vgsoftware.android.fastcheckin.R;
import com.vgsoftware.android.fastcheckin.facebook.BaseRequestListener;
import com.vgsoftware.android.fastcheckin.facebook.SessionEvents;
import com.vgsoftware.android.fastcheckin.facebook.SessionEvents.AuthListener;
import com.vgsoftware.android.fastcheckin.facebook.SessionEvents.LogoutListener;
import com.vgsoftware.android.fastcheckin.facebook.SessionStore;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class LoginButton extends ImageButton
{

	private Facebook _facebook;
	private Handler _handler;
	private SessionListener _sessionListener = new SessionListener();
	private String[] _permissions;
	private Activity _activity;

	public LoginButton(Context context)
	{
		super(context);

		setBackgroundColor(Color.TRANSPARENT);
		setAdjustViewBounds(true);
		drawableStateChanged();
	}

	public LoginButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		setBackgroundColor(Color.TRANSPARENT);
		setAdjustViewBounds(true);
		drawableStateChanged();
	}

	public LoginButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		setBackgroundColor(Color.TRANSPARENT);
		setAdjustViewBounds(true);
		drawableStateChanged();
	}

	public void init(final Activity activity, final Facebook fb)
	{
		init(activity, fb, new String[] {});
	}

	public void init(final Activity activity, final Facebook fb, final String[] permissions)
	{
		_activity = activity;
		_facebook = fb;
		_permissions = permissions;
		_handler = new Handler();

		setImageResource(fb.isSessionValid() ? R.drawable.logout_button : R.drawable.login_button);
		drawableStateChanged();

		SessionEvents.addAuthListener(_sessionListener);
		SessionEvents.addLogoutListener(_sessionListener);
		setOnClickListener(new ButtonOnClickListener());
	}

	private final class ButtonOnClickListener implements OnClickListener
	{

		public void onClick(View arg0)
		{
			if (_facebook.isSessionValid())
			{
				SessionEvents.onLogoutBegin();
				AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(_facebook);
				asyncRunner.logout(getContext(), new LogoutRequestListener());
			}
			else
			{
				_facebook.authorize(_activity, _permissions, new LoginDialogListener());
			}
		}
	}

	private final class LoginDialogListener implements DialogListener
	{
		public void onComplete(Bundle values)
		{
			SessionEvents.onLoginSuccess();
		}

		public void onFacebookError(FacebookError error)
		{
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onError(DialogError error)
		{
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onCancel()
		{
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class LogoutRequestListener extends BaseRequestListener
	{
		public void onComplete(String response, final Object state)
		{
			// callback should be run in the original thread,
			// not the background thread
			_handler.post(new Runnable()
			{
				public void run()
				{
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}

	private class SessionListener implements AuthListener, LogoutListener
	{

		public void onAuthSucceed()
		{
			setImageResource(R.drawable.logout_button);
			SessionStore.save(_facebook, getContext());
		}

		public void onAuthFail(String error)
		{
		}

		public void onLogoutBegin()
		{
		}

		public void onLogoutFinish()
		{
			SessionStore.clear(getContext());
			setImageResource(R.drawable.login_button);
		}
	}

}
