package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	
	private static long maxId = 0;
	private static ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	private ListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		setActionBar();
		getTweets();
	}

	public void getTweets() {
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				int index = 1;
				if (maxId == 0) {
					lvTweets.addFooterView(getButton());
					index--;
				}
				
				ArrayList<Tweet> newTweets = Tweet.fromJson(jsonTweets);

				Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");
				if (tweet != null)
					tweets.add(tweet);

				while (index < newTweets.size()) {
					tweets.add(newTweets.get(index));
					index++;
				}
				
				int currentPosition = lvTweets.getFirstVisiblePosition();
				lvTweets.setAdapter(new TweetsAdapter(getBaseContext(), tweets));
				lvTweets.setSelectionFromTop(currentPosition + 1, 0);
				maxId = tweets.get(tweets.size() - 1).getId();
			}
		}, maxId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@SuppressLint("InlinedApi")
	private Button getButton() {
		Button btnMore = new Button(this, null, android.R.attr.buttonStyleSmall);
		btnMore.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
		btnMore.setText("Load More Tweets");
		btnMore.setTextColor(getResources().getColor(android.R.color.white));
		btnMore.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        getTweets();
		    }
		});
		return btnMore;
	}

	private void setActionBar() {
		MyTwitterApp.getRestClient().getUser(new JsonHttpResponseHandler() {
			@SuppressLint("NewApi") @Override
			public void onSuccess(JSONObject object) {
				User user = User.fromJson(object);
				ActionBar bar = getActionBar();
				bar.setTitle("@" + user.getScreenName());
				bar.setDisplayHomeAsUpEnabled(true);
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_compose:
				Intent intent = new Intent(this, ComposeActivity.class);
				startActivity(intent);
				return true;
		}
	    return false;
	}
}
