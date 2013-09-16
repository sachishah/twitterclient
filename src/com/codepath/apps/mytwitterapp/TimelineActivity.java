package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mytwitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.mytwitterapp.fragments.MentionsTimelineFragment;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupNavigationTabs();
		setActionBar();
	}
	
	public void setupNavigationTabs() {
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(true);

		Tab home = bar.newTab().setText("Home")
				.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home)
				.setTabListener(this);
		bar.addTab(home);

		Tab mentions = bar.newTab().setText("Mentions")
				.setTag("MentionsTimelineFragment").setIcon(R.drawable.ic_at)
				.setTabListener(this);
		bar.addTab(mentions);
		bar.selectTab(home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
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
				startActivity(new Intent(this, ComposeActivity.class));
				return true;
			case R.id.action_profile:
				startActivity(new Intent(this, ProfileActivity.class));
				return true;
		}
	    return false;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction(); 
		if (tab.getTag() == "HomeTimelineFragment") {
			ActionBar bar = getActionBar();
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		} else {
			fts.replace(R.id.frame_container, new MentionsTimelineFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
}
