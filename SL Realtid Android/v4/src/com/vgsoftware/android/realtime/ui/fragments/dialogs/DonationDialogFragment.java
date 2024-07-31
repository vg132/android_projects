package com.vgsoftware.android.realtime.ui.fragments.dialogs;

import com.vgsoftware.android.realtime.R;
import com.vgsoftware.android.vglib.ui.dialogs.IDialogFragmentAction;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DonationDialogFragment extends DialogFragment
{
	private IDialogFragmentAction _onFragmentDialogAction = null;

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_donation, null);

		final Spinner itemsSpinner = (Spinner) view.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dialog_donation_donations, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		itemsSpinner.setAdapter(adapter);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle(R.string.dialog_donation_title);
		alertDialogBuilder.setView(view);

		alertDialogBuilder.setPositiveButton(R.string.dialog_donation_button, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				int pos = itemsSpinner.getSelectedItemPosition();
				if (_onFragmentDialogAction != null)
				{
					_onFragmentDialogAction.onPositiveAction(DonationDialogFragment.this, pos);
				}
				dialog.dismiss();
			}
		});
		return alertDialogBuilder.create();
	}

	public void setOnAction(IDialogFragmentAction action)
	{
		_onFragmentDialogAction = action;
	}
}
