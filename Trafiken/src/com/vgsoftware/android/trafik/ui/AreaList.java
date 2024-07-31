package com.vgsoftware.android.trafik.ui;

import java.util.List;

import com.vgsoftware.android.trafik.R;
import com.vgsoftware.android.trafik.dataabstraction.DatabaseHelper;
import com.vgsoftware.android.trafik.dataabstraction.Feed;
import com.vgsoftware.android.trafik.ui.adapters.AreaItemAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AreaList extends Activity
{
	private ListView _areaList = null;
	private AreaItemAdapter _areaItemAdapter = null;
	private Feed _feed = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_area_list);

		_areaList = (ListView) findViewById(R.id.areaListView);

		List<Feed> feeds = DatabaseHelper.getHelper(this).getFeedDao().queryForEq("name", "Stockholm");
		if (feeds.size() > 0)
		{
			_feed = feeds.get(0);
			_areaItemAdapter = new AreaItemAdapter(this, _feed);
			_areaList.setAdapter(_areaItemAdapter);
		}

		_areaList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				Intent intent = new Intent(AreaList.this, CameraGallery.class);

				intent.putExtra(CameraGallery.EXTRA_FEED_ID, _areaItemAdapter.getFeed().getId());
				intent.putExtra(CameraGallery.EXTRA_AREA_NAME, (String) _areaItemAdapter.getItem(position));

				startActivity(intent);
			}
		});
	}
}
