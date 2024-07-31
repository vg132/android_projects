package com.vgsoftware.android.gamelibrary.ui.adapters;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.vgsoftware.android.gamelibrary.R;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.GiantBombService;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Game;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Platform;
import com.vgsoftware.android.vglib.StringUtility;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class GameTitleSuggestionAdapter extends BaseAdapter implements Filterable
{
	private List<Game> _games = null;
	private GiantBombService _giantBombService = null;
	private LayoutInflater _inflater = null;

	public GameTitleSuggestionAdapter(Context context)
	{
		super();
		_giantBombService = new GiantBombService();
		_games = new ArrayList<Game>();
		_inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
		return position;
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
			if (game.image != null && !StringUtils.isEmpty(game.image.thumb_url))
			{
				UrlImageViewHelper.setUrlDrawable(holder.thumbnail, game.image.thumb_url);
			}
			holder.gameTitle.setText(game.name);
			List<String> platforms = new ArrayList<String>();
			if (game.platforms != null)
			{
				for (Platform platform : game.platforms)
				{
					platforms.add(platform.abbreviation);
				}
			}
			holder.platforms.setText(StringUtils.join(platforms, ", "));
			return convertView;
		}
		return null;
	}

	@Override
	public Filter getFilter()
	{
		Filter filter = new Filter()
		{
			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				FilterResults filterResult = new FilterResults();
				if (!StringUtility.isNullOrEmpty(constraint))
				{
					_games = _giantBombService.listGames(constraint.toString());
					filterResult.values = _games;
					filterResult.count = _games.size();
				}
				return filterResult;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
			{
				if (results.count > 0)
				{
					notifyDataSetChanged();
				}
				else
				{
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}
}
