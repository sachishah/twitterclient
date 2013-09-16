package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimelineFragment extends TweetsListFragment {
	
	private static long maxId = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getTweets();
	}
	
	public void getTweets() {
		MyTwitterApp.getRestClient().getMentionsTimeline(new JsonHttpResponseHandler() {
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
		}, maxId);
	}
	
	@Override
	public void loadMoreTweets() {
		getTweets();
	}
}
