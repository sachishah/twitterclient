package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {
	
	private long maxId = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getTweets();
	}

	public void getTweets() {
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {			
				ArrayList<Tweet> tweets = new ArrayList<Tweet>();
				Tweet tweet = (Tweet) getActivity().getIntent().getSerializableExtra("tweet");
				if (tweet != null)
					tweets.add(tweet);
				tweets.addAll(Tweet.fromJson(jsonTweets));
				getAdapter().addAll(tweets);
				maxId = tweets.get(tweets.size() - 1).getId() - 1;
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject obj) {
				Toast.makeText(getActivity(), obj.toString(), Toast.LENGTH_SHORT).show();
			}
		}, maxId);
	}

	@Override
	public void loadMoreTweets() {
		getTweets();
	}
}
