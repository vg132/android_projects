package com.vgsoftware.android.gamelibrary.ui.dialogs;

import com.vgsoftware.android.gamelibrary.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class AboutDialog extends DialogFragment
{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_about_title)
				.setMessage(R.string.dialog_about_message)
				.setPositiveButton(android.R.string.ok, null);
		return builder.create();
	}
}
