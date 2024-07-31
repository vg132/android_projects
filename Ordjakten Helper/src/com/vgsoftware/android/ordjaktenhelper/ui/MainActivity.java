package com.vgsoftware.android.ordjaktenhelper.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vgsoftware.android.ordjaktenhelper.R;
import com.vgsoftware.android.ordjaktenhelper.data.DatabaseHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity
{
	private DatabaseHelper _databaseHelper;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_databaseHelper = new DatabaseHelper(this);
		if (!_databaseHelper.doesDatabaseExist())
		{
			final ProgressDialog myProgressDialog = ProgressDialog.show(this, "Vänta", "Installerar databasen...", true);
			new Thread()
			{
				public void run()
				{
					try
					{
						_databaseHelper.getReadableDatabase();
					}
					catch (Exception e)
					{
					}
					myProgressDialog.dismiss();
				}
			}.start();
		}
		final ListView listView = (ListView) findViewById(R.id.listView);
		final Button button = (Button) findViewById(R.id.buttonView);
		final EditText editText = (EditText) findViewById(R.id.editView);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				char[] letters = editText.getText().toString().toLowerCase().toCharArray();
				if (letters.length != 3)
				{
					Toast.makeText(MainActivity.this, "Måste fylla i tre bokstäver", Toast.LENGTH_LONG).show();
					return;
				}
				String queryWord = letters[0] + "%" + letters[1] + "%" + letters[2] + "%";
				Cursor cursor = null;
				SQLiteDatabase database = null;
				List<Map<String, String>> words = new ArrayList<Map<String, String>>();
				try
				{
					database = _databaseHelper.getReadableDatabase();
					cursor = database.rawQuery("SELECT Word FROM Words WHERE Word LIKE ? ORDER BY LENGTH(word) DESC", new String[] { queryWord });
					while (cursor.moveToNext())
					{
						Map<String, String> word = new HashMap<String, String>(2);
						word.put("word", cursor.getString(0));
						word.put("points", "Poäng: " + cursor.getString(0).length());
						words.add(word);
					}
					cursor.close();
				}
				catch (Exception ex)
				{
				}
				finally
				{
					if (cursor != null)
					{
						cursor.close();
					}
					if (database != null)
					{
						database.close();
					}
				}
				SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, words, android.R.layout.simple_list_item_2, new String[] { "word", "points" }, new int[] { android.R.id.text1, android.R.id.text2 });
				listView.setAdapter(adapter);
			}
		});
	}
}
