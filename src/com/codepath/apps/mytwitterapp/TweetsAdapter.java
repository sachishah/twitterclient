package com.codepath.apps.mytwitterapp;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet>{

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.tweet_item, null);
		}
		
		Tweet tweet = getItem(position);
		User user = tweet.getUser();
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), imageView);
		
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String screenName = "<font color='#33b5e5'> @" + user.getScreenName() + "</small></font>";
		String formattedName = "<b>" + user.getName() + "</b><small>" + screenName + "</small>";
		nameView.setText(Html.fromHtml(formattedName));
		
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		
		TextView timeView = (TextView) view.findViewById(R.id.tvTimestamp);
		String hours = "<small><font color='#777777'>" + tweet.getTimestamp() + "</small></font>";
		timeView.setText(Html.fromHtml(hours));

		return view;
	}

}
