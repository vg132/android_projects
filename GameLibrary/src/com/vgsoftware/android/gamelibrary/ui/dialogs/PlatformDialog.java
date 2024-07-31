package com.vgsoftware.android.gamelibrary.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import com.vgsoftware.android.gamelibrary.model.DataStore;
import com.vgsoftware.android.gamelibrary.model.Platform;
import com.vgsoftware.android.vglib.ui.dialogs.IDialogFragmentAction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class PlatformDialog extends DialogFragment
{
	private IDialogFragmentAction _onFragmentDialogAction = null;
	private List<Platform> _platforms = null;
	private List<Platform> _selectedPlatforms = null;

	public PlatformDialog(List<Platform> selectedPlatforms)
	{
		_platforms = DataStore.getInstance().listPlatforms();
		_selectedPlatforms = selectedPlatforms;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final boolean[] selectedPlatformNames = new boolean[_platforms.size()];
		final List<String> platformNames = new ArrayList<String>();
		for (Platform platform : _selectedPlatforms)
		{
			platformNames.add(platform.getName());
			selectedPlatformNames[platformNames.size() - 1] = true;
		}
		for (Platform platform : _platforms)
		{
			if (!platformNames.contains(platform.getName()))
			{
				platformNames.add(platform.getName());
				selectedPlatformNames[platformNames.size() - 1] = false;
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title

		builder.setTitle("Select Platforms").setMultiChoiceItems((String[]) platformNames.toArray(new String[platformNames.size()]), selectedPlatformNames, new DialogInterface.OnMultiChoiceClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked)
			{
				String platformName = platformNames.get(which);
				if (isChecked)
				{
					for (Platform platform : _platforms)
					{
						if (platform.getName().equals(platformName))
						{
							_selectedPlatforms.add(platform);
							break;
						}
					}
				}
				else
				{
					Platform unselectedPlatform = null;
					for (Platform platform : _selectedPlatforms)
					{
						if (platform.getName().equals(platformName))
						{
							unselectedPlatform = platform;
							break;
						}
					}
					if (unselectedPlatform != null)
					{
						_selectedPlatforms.remove(unselectedPlatform);
					}
				}
			}
		}).setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				if (_onFragmentDialogAction != null)
				{
					_onFragmentDialogAction.onPositiveAction(PlatformDialog.this, _selectedPlatforms);
				}
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				if (_onFragmentDialogAction != null)
				{
					_onFragmentDialogAction.onNegativeAction(PlatformDialog.this, null);
				}
			}
		});
		return builder.create();
	}

	public void setOnAction(IDialogFragmentAction action)
	{
		_onFragmentDialogAction = action;
	}
}
