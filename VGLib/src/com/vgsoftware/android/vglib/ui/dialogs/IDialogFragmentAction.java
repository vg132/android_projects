package com.vgsoftware.android.vglib.ui.dialogs;

import android.app.DialogFragment;

public interface IDialogFragmentAction
{
	void onPositiveAction(DialogFragment sender, Object data);
	void onNegativeAction(DialogFragment sender, Object data);
}