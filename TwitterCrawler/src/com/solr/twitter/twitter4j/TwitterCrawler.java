package com.solr.twitter.twitter4j;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;

public interface TwitterCrawler {

	String CONSUMER_KEY = "pRKBHczSh5Hb8MfPruXoJEcy3";
	String CONSUMER_SECRET_KEY = "RwdbCGS5hu11kIv9S4nlSCo4po99Jm9kZnwHTnNN0NGhX8sxh4";
	String ACCESS_TOKEN = "56035111-y5uthe6vzLHlYuis2MXy79iDdKj0jZ0T1B2mY3MdL";
	String ACCESS_TOKEN_SECRET = "xvqC3S16Oj660nuBbLqT6xoFzetHZQtAcyw4Hl358kOck";

	public Twitter initConfiguration();

	// void searchTweets(String query, String language, Date from, Date to);

	List<Status> searchTweets(String query, String language, String fromDate, String toDate);

	int writeTweetsToFile(List<Status> tweets, String fileName, String lang,String dateCreatedOn);

}
