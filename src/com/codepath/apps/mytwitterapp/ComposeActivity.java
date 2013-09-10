package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	
	EditText etTweet;
	Button btnCancel;
	Button btnTweet;
	TextView tvUser;
	ImageView ivUser;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		etTweet = (EditText) findViewById(R.id.etTweet);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnTweet = (Button) findViewById(R.id.btnTweet);
		btnTweet.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
		btnTweet.setTextColor(getResources().getColor(android.R.color.white));
		setTextAndImageViews();
		ActionBar bar = getActionBar();
		bar.setTitle("Compose Tweet");
		bar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public void setTextAndImageViews() {
		tvUser = (TextView) findViewById(R.id.tvUser);
		ivUser = (ImageView) findViewById(R.id.ivUser);
		MyTwitterApp.getRestClient().getUser(new JsonHttpResponseHandler() {
			@SuppressLint("NewApi") @Override
			public void onSuccess(JSONObject object) {
				User user = User.fromJson(object);
				String screenName = "@" + user.getScreenName();
				tvUser.setText(Html.fromHtml(screenName));
				ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivUser);
			}
		});
	}
	
	public void onCancel(View v) {
		finish();
	}
	
	public void onTweet(View v) {
		String tweet = etTweet.getText().toString();
    	if (tweet.matches("")) {
    		Toast.makeText(this, "Please enter a tweet", Toast.LENGTH_SHORT).show();
    		return;
    	}
	    Toast.makeText(this, "Tweeting..", Toast.LENGTH_SHORT).show();
	    
	    MyTwitterApp.getRestClient().postStatusesUpdate(new JsonHttpResponseHandler() {
	    	@Override
			public void onSuccess(JSONObject object) {
	    		Tweet tweet = Tweet.fromJson(object);
	    		Intent i = new Intent(getBaseContext(), TimelineActivity.class);
	    		i.putExtra("tweet", tweet);
				startActivity(i);
				finish();
	    	}
		}, tweet);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
	    return false;
	}
}
