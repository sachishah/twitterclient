package com.codepath.apps.mytwitterapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class MyTwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "AN06HMd5M5lRNdO6dPWrg";
    public static final String REST_CONSUMER_SECRET = "CH1pEFHY9iN78B877NTJbxwcfhdjGFkHZ63lg7Jo";
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp";

    public MyTwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
 
    public void getHomeTimeline(AsyncHttpResponseHandler handler, long maxId) {
    	String url = "/statuses/home_timeline.json";
    	getRequest(handler, url, maxId);
    }
    
    public void getMentionsTimeline(AsyncHttpResponseHandler handler, long maxId) {
    	String url = "/statuses/mentions_timeline.json";
    	getRequest(handler, url, maxId);
    }
    
    public void getUser(AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("/account/verify_credentials.json");
    	client.get(apiUrl, null, handler);
    }
    
    public void getUserTimeline(AsyncHttpResponseHandler handler, long maxId) {
    	String url = "/statuses/user_timeline.json";
    	getRequest(handler, url, maxId);
    }

    public void postStatusesUpdate(AsyncHttpResponseHandler handler, String tweet) {
    	String apiUrl = getApiUrl("/statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", tweet);
    	client.post(apiUrl, params, handler);
    }
    
    private void getRequest(AsyncHttpResponseHandler handler, String url, long maxId) {
    	String apiUrl = getApiUrl(url);
    	RequestParams params = new RequestParams();
    	params.put("count", "25");
    	if (maxId > 0)
    		params.put("max_id", Long.toString(maxId));
    	client.get(apiUrl, params, handler);
    }
}