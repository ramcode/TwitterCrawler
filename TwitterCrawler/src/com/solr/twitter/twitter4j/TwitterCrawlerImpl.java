/*
 * Implementation of Crawler for retrieving and storing tweets
 * @author Ramesh
 * @date 09/11/15
 * @version 1.0
 */
package com.solr.twitter.twitter4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import twitter4j.HashtagEntity;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterCrawlerImpl implements TwitterCrawler {

	public ConfigurationBuilder config = null;
	static TwitterFactory twitterFactory = null;
	static Twitter twitter = null;
	final int MAX_RESULTS_PER_QUERY = 50;
	final int MAX_TWEETS = 50;
	long maxTweetId = Long.MAX_VALUE;
	String dateFormat = "YYYY-MM-DD";

	public static void main(String[] args) {
		ConfigurationBuilder config = new ConfigurationBuilder();
		config.setOAuthAccessToken(ACCESS_TOKEN);
		config.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
		config.setOAuthConsumerKey(CONSUMER_KEY);
		config.setOAuthConsumerSecret(CONSUMER_SECRET_KEY);

		config.setDebugEnabled(true);
		config.setGZIPEnabled(true);
		config.setJSONStoreEnabled(true);
		config.setDebugEnabled(true);
		// config.setApplicationOnlyAuthEnabled(true);
		twitterFactory = new TwitterFactory(config.build());
		twitter = twitterFactory.getInstance();
		// twitter = getTwitter();
		Query twitterQuery = new Query("ramesh");
		twitterQuery.setCount(2000);
		// twitterQuery.setResultType(Query.RECENT);
		twitterQuery.setSince("2015-09-13");
		twitterQuery.setUntil("2015-09-14");
		twitterQuery.setLang("en");
		// twitterQuery.setQuery();
		try {
			QueryResult tweets = twitter.search(twitterQuery);
			System.out.println("Total Tweets crawled: " + tweets.getCount());
			for (Status tweet : tweets.getTweets()) {
				System.out.println("Tweet :" + tweet.getText());
				System.out.println("Created at : " + tweet.getCreatedAt());
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Twitter getTwitter() {
		return initConfiguration();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solr.twitter.twitter4j.TwitterCrawler#authenciateUser()
	 * 
	 * @author Ramesh
	 */

	private boolean isValidDateForTwitter(String date) {
		try {
			DateFormat format = new SimpleDateFormat(dateFormat);
			format.setLenient(false);
			format.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}

	}

	public void authenticateUser() {
		AccessToken accessToken = new AccessToken(CONSUMER_KEY, CONSUMER_SECRET_KEY);
		twitter.setOAuthAccessToken(accessToken);
		twitter.setOAuthConsumer(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
	}

	@Override
	public List<Status> searchTweets(String query, String language, String fromDate, String toDate) {
		int remainingTweets = MAX_TWEETS;
		List<Status> tweets = new ArrayList<Status>();
		try {
			Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
			RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
			System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds\n",
					searchTweetsRateLimit.getRemaining(), searchTweetsRateLimit.getLimit(),
					searchTweetsRateLimit.getSecondsUntilReset());

			while (remainingTweets - tweets.size() > 0) {
				Query twitterQuery = new Query(query);
				// twitterQuery.setResultType(Query.MIXED);
				twitterQuery.setLang(language);
				twitterQuery.setMaxId(maxTweetId);
				/*
				 * if (fromDate != null && isValidDateForTwitter(fromDate)) {
				 * //twitterQuery.setSince(fromDate); twitterQuery.setSince("");
				 * }
				 */
				/*
				 * if (toDate != null && isValidDateForTwitter(toDate)) {
				 * twitterQuery.setUntil(toDate); }
				 */
				twitterQuery.setSince(fromDate);
				twitterQuery.setUntil(toDate);
				if (remainingTweets > 100) {
					twitterQuery.setCount(MAX_RESULTS_PER_QUERY);
				} else {
					twitterQuery.setCount(remainingTweets);
				}
				if (searchTweetsRateLimit.getRemaining() == 0) {
					System.out.printf("!!! Sleeping for %d seconds due to rate limits\n",
							searchTweetsRateLimit.getSecondsUntilReset());
					try {
						Thread.sleep((searchTweetsRateLimit.getSecondsUntilReset() + 2) * 1000l);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Tweets Remaining: " + (remainingTweets - tweets.size()));
				System.out.println("Executing Query........");
				QueryResult results = twitter.search(twitterQuery);
				int resultCount = results.getTweets().size();
				System.out.println("Tweets retrieved in this query :" + resultCount);
				if (resultCount > 0) {
					tweets.addAll(results.getTweets());
					for (Status tweet : results.getTweets()) {
						if (tweet.getId() < maxTweetId) {
							maxTweetId = tweet.getId();
						}
					}
				} else {
					break;
				}
				maxTweetId = maxTweetId - 1;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (TwitterException ex) {
			ex.printStackTrace();
		}
		return tweets;
	}

	@Override
	public Twitter initConfiguration() {
		config = new ConfigurationBuilder();

		config.setOAuthAccessToken(ACCESS_TOKEN);
		config.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
		config.setOAuthConsumerKey(CONSUMER_KEY);
		config.setOAuthConsumerSecret(CONSUMER_SECRET_KEY);

		config.setDebugEnabled(true);
		config.setGZIPEnabled(true);
		config.setJSONStoreEnabled(true);
		config.setDebugEnabled(true);
		// config.setApplicationOnlyAuthEnabled(true);
		twitterFactory = new TwitterFactory(config.build());
		twitter = twitterFactory.getInstance();
		return twitter;
		/*
		 * try { OAuth2Token token =
		 * twitterFactory.getInstance().getOAuth2Token();
		 * config.setOAuth2TokenType(token.getTokenType());
		 * config.setOAuth2AccessToken(token.getAccessToken()); twitter = new
		 * TwitterFactory(config.build()).getInstance(); //accessToken =
		 * twitter.getOAuthAccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET); }
		 * catch (TwitterException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		// twitter.setOAuthAccessToken(accessToken);
		// twitter.setOAuthConsumer(OAUTH_KEY, OAUTH_SECRET_KEY);
	}

	@Override
	public int writeTweetsToFile(List<Status> tweets, String fileName, String lang, String dateCreatedOn) {
		BufferedWriter bos = null;
		int j = 0;
		int k = 0;
		try {
			JSONArray array = new JSONArray();
			for (Status tweet : tweets) {
				String tweetText = tweet.getText();
				System.out.println("Tweet Created At: " + tweet.getCreatedAt());
				JSONObject newJsonTweet = new JSONObject();
				newJsonTweet.put("id", tweet.getId());
				newJsonTweet.put("lang", tweet.getLang());
				newJsonTweet.put("text", tweetText);
				if (lang.equalsIgnoreCase("en")) {
					newJsonTweet.put("text_en", tweetText);
					newJsonTweet.put("text_de", "");
					newJsonTweet.put("text_ru", "");
				}
				if (lang.equalsIgnoreCase("de")) {
					newJsonTweet.put("text_en", "");
					newJsonTweet.put("text_de", tweetText);
					newJsonTweet.put("text_ru", "");
				}
				if (lang.equalsIgnoreCase("ru")) {
					newJsonTweet.put("text_en", "");
					newJsonTweet.put("text_de", "");
					newJsonTweet.put("text_ru", tweetText);
				}
				Date createdDate = tweet.getCreatedAt();
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
				newJsonTweet.put("created_at", sd.format(createdDate));
				List<URLEntity> urlEntities = Arrays.asList(tweet.getURLEntities());
				JSONArray urlArray = new JSONArray();
				for (int i = 0; i < urlEntities.size(); i++) {
					String expandedUrl = urlEntities.get(i).getExpandedURL();
					urlArray.put(i, expandedUrl);
				}
				newJsonTweet.put("twitter_urls", urlArray);
				List<HashtagEntity> hashTagEntities = Arrays.asList(tweet.getHashtagEntities());
				JSONArray hashTagArray = new JSONArray();
				for (int i = 0; i < hashTagEntities.size(); i++) {
					String hashTagText = hashTagEntities.get(i).getText();
					hashTagArray.put(i, hashTagText);
				}
				newJsonTweet.put("twitter_hashtags", hashTagArray);

				array.put(newJsonTweet);
				j++;
			}
			if (array.length() > 0) {
				bos = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(new File("working_data/"+dateCreatedOn+"/"+fileName), false), "UTF-8"));
				bos.write(array.toString());
				bos.flush();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Actual Tweets Crawled: " + j);
		System.out.println("Number of Error Tweets : " + k);
		return j;

	}

}
