package com.vgsoftware.android.realtime.ui.fragments.dialogs;

import com.vgsoftware.android.realtime.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AboutDialogFragment extends DialogFragment
{
	public AboutDialogFragment()
	{
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle(R.string.dialog_changes_title);
		alertDialogBuilder.setMessage(R.string.dialog_changes_message);
		alertDialogBuilder.setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		return alertDialogBuilder.create();
	}
}
