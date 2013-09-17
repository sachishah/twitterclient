package com.codepath.apps.mytwitterapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitterapp.fragments.UserTimelineFragment;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ActionBar bar = getActionBar();
		bar.setTitle("Profile");
		bar.setDisplayHomeAsUpEnabled(true);
		String userJsonString = getIntent().getStringExtra("user");

		JSONObject jsonObject;
		User user = null;
		if (userJsonString != "") {
			try {
				jsonObject = new JSONObject(userJsonString);
				user = User.fromJson(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		loadInfo(user);
		UserTimelineFragment fragment = (UserTimelineFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentUserTimeline);
		fragment.setUser(user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	public void loadInfo(User u) {
		if (u == null) {
			MyTwitterApp.getRestClient().getUser(new JsonHttpResponseHandler() {
				public void onSuccess(JSONObject object) {
					User user = User.fromJson(object);
					loadHeader(user);
				}
			});
		} else {
			loadHeader(u);
		}
	}
	
	public void loadHeader(User u) {
		
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

		tvName.setText(u.getName());
		tvTagline.setText(Html.fromHtml(u.getTagline()));
		tvFollowers.setText(Html.fromHtml(u.getFollowersCount() + " Followers"));
		tvFollowing.setText(Html.fromHtml(u.getFriendsCount() + " Following"));
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == android.R.id.home) {
	    	finish();
	    	return true;
	    }
	    return false;
	}
}
