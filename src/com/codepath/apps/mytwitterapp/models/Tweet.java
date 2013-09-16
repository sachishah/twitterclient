package com.codepath.apps.mytwitterapp.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Tweet extends BaseModel implements Serializable{
    private User user;

    public User getUser() {
        return user;
    }

    public String getBody() {
        return getString("text");
    }

    public long getId() {
        return getLong("id");
    }

    public boolean isFavorited() {
        return getBoolean("favorited");
    }

    public boolean isRetweeted() {
        return getBoolean("retweeted");
    }
    
    public String getTimestamp() {
    	SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d h:m:s ZZZ yyyy", Locale.getDefault());
    	
    	try {
	    	Date date = (Date) formatter.parse(getString("created_at"));        
	    	Calendar cal = Calendar.getInstance();
	    	cal.setTime(date);
	    	Calendar now = Calendar.getInstance();

	    	int hourOfTweet = cal.get(Calendar.HOUR_OF_DAY);
	    	int currentHour = now.get(Calendar.HOUR_OF_DAY); 
	    	int hour = currentHour - hourOfTweet;
	    	
	    	int minOfTweet = cal.get(Calendar.MINUTE);
    		int currentMin = now.get(Calendar.MINUTE);
    		int min = Math.abs(currentMin - minOfTweet);
    		
    		if (hour != 0) {
    			if (hour == 1)
	    			return "1 hour ago";
	    		else {
	    			if (hour < 0)
	    				hour = currentHour + 24 - hourOfTweet;
	    			return hour + " hours ago";
	    		}
    		} else {
    			switch (min) {
	    			case 0: return "few seconds ago";
	    			case 1: return "1 minute ago";
	    			default: return min + " minutes ago";
				}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.jsonObject = jsonObject;
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}