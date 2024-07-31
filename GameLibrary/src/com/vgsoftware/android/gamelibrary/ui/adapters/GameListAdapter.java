package com.vgsoftware.android.gamelibrary.ui.adapters;

import java.util.List;

import com.vgsoftware.android.gamelibrary.GameLibraryApplication;
import com.vgsoftware.android.gamelibrary.R;
import com.vgsoftware.android.gamelibrary.model.Game;
import com.vgsoftware.android.vglib.DateUtility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameListAdapter extends BaseAdapter
{
	private List<Game> _games = null;
	private LayoutInflater _inflater = null;

	public GameListAdapter(Context context, List<Game> games)
	{
		super();
		_games = games;
		_inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	public void setGames(List<Game> games)
	{
		_games = games;
	}

	@Override
	public int getCount()
	{
		return _games.size();
	}

	@Override
	public Game getItem(int position)
	{
		return _games.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return _games.get(position).getId();
	}
	
	private class ViewHolder
	{
		public ImageView thumbnail;
		public TextView gameTitle;
		public TextView platforms;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Game game = (Game) getItem(position);
		if (game != null)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				convertView = _inflater.inflate(R.layout.list_item_game, null);
				holder = new ViewHolder();
				holder.thumbnail = (ImageView) convertView.findViewById(android.R.id.icon);
				holder.gameTitle = (TextView) convertView.findViewById(android.R.id.text1);
				holder.platforms = (TextView) convertView.findViewById(android.R.id.text2);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.thumbnail.setVisibility(View.GONE);
			holder.platforms.setText("Added: " + DateUtility.formatDate(game.getAdded(), GameLibraryApplication.getAppContext()));

			// /dateFormat = android.text.format.DateFormat.getDateFormat(getA);
			// /timeFormat = android.text.format.DateFormat.getTimeFormat(context);

			// if (game.image != null && !StringUtils.isEmpty(game.image.thumb_url))
			// {
			// UrlImageViewHelper.setUrlDrawable(holder.thumbnail,
			// game.image.thumb_url);
			// }
			holder.gameTitle.setText(game.getTitle());
			// List<String> platforms = new ArrayList<String>();
			// if (game.platforms != null)
			// {
			// for (Platform platform : game.platforms)
			// {
			// platforms.add(platform.abbreviation);
			// }
			// }
			// holder.platforms.setText(StringUtils.join(platforms, ", "));
			return convertView;
		}
		return null;
	}
}
