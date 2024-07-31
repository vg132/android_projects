package com.vgsoftware.android.polisen.ui;

import com.vgsoftware.android.polisen.Utility;
import com.vgsoftware.android.polisen.dataabstraction.Category;
import com.vgsoftware.android.polisen.dataabstraction.FeedDroidDB;
import com.vgsoftware.android.polisen.ui.controls.CategoryListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategorySelector extends Activity implements OnItemClickListener
{
	private CategoryListView _categoryListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_selector);
		setupActivity();
	}

	private void setupActivity()
	{
		setupControls();
		if(Utility.canAddFeeds())
		{
			registerForContextMenu(_categoryListView);
		}
		Toast.makeText(this,"Tryck på region för att aktivera/avaktivera",Toast.LENGTH_LONG).show();
	}

	private void setupControls()
	{
		_categoryListView = (CategoryListView) findViewById(R.id.CategoryListView);
		_categoryListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Category category=_categoryListView.getCategoryAtPosition(position);
		if(category!=null)
		{
			FeedDroidDB db=new FeedDroidDB();
			category.setActive(!category.getActive());
			db.saveCategory(category);
			//_categoryListView.loadCategories();
			_categoryListView.refreshCategoryList();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return (super.onKeyDown(keyCode, event));
	}
}
