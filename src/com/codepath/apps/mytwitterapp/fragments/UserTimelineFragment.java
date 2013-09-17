package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	
	private long maxId = 0;
	private User user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void getTweets() {
		MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {				
				ArrayList<Tweet> tweets = new ArrayList<Tweet>();
				Tweet tweet = (Tweet) getActivity().getIntent().getSerializableExtra("tweet");
				if (tweet != null)
					tweets.add(tweet);
				tweets.addAll(Tweet.fromJson(jsonTweets));
				getAdapter().addAll(tweets);
				maxId = tweets.get(tweets.size() - 1).getId() + 1;
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject obj) {
				Toast.makeText(getActivity(), obj.toString(), Toast.LENGTH_SHORT).show();
			}
		}, maxId, user);
	}
	
	@Override
	public void loadMoreTweets() {
		getTweets();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		getTweets();
	}
}
