package com.vgsoftware.android.gamelibrary.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.vgsoftware.android.gamelibrary.R;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.GiantBombService;
import com.vgsoftware.android.gamelibrary.integration.giantbomb.models.Company;
import com.vgsoftware.android.vglib.StringUtility;

public class CompanySuggestionAdapter extends BaseAdapter implements Filterable
{
	private List<Company> _companies = null;
	private GiantBombService _giantBombService = null;
	private LayoutInflater _inflater = null;

	public CompanySuggestionAdapter(Context context)
	{
		super();
		_giantBombService = new GiantBombService();
		_companies = new ArrayList<Company>();
		_inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return _companies.size();
	}

	@Override
	public Company getItem(int position)
	{
		return _companies.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	private class ViewHolder
	{
		public ImageView thumbnail;
		public TextView name;
		public TextView aliases;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Company company = (Company) getItem(position);
		if (company != null)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				convertView = _inflater.inflate(R.layout.list_item_game, null);
				holder = new ViewHolder();
				holder.thumbnail = (ImageView) convertView.findViewById(android.R.id.icon);
				holder.name = (TextView) convertView.findViewById(android.R.id.text1);
				holder.aliases = (TextView) convertView.findViewById(android.R.id.text2);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (company.image != null && !StringUtility.isNullOrEmpty(company.image.thumb_url))
			{
				UrlImageViewHelper.setUrlDrawable(holder.thumbnail, company.image.thumb_url);
			}
			holder.name.setText(company.name);
			if (!StringUtility.isNullOrEmpty(company.aliases))
			{
				holder.aliases.setText(StringUtils.join(StringUtils.split(company.aliases, '\n'), ", "));
			}
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
					_companies = _giantBombService.listCompanies(constraint.toString());
					filterResult.values = _companies;
					filterResult.count = _companies.size();
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
