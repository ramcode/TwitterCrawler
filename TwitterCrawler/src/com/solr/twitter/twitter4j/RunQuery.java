package com.solr.twitter.twitter4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import twitter4j.Status;

public class RunQuery {

	public static void main(String[] args) {
		try {
			TwitterCrawler crawler = new TwitterCrawlerImpl();
			DateFormat df = new SimpleDateFormat("MM_dd_yyyy");
			DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("aliens_en", "aliens");
			queryMap.put("aliens_de", "Außerirdische");
			queryMap.put("aliens_ru", "иностранцы");
			queryMap.put("cancer_en", "cancer");
			queryMap.put("cancer_de", "Krebs");
			queryMap.put("cancer_ru", "рак");
			queryMap.put("discovery_en", "discovery");
			queryMap.put("discovery_de", "Entdeckung");
			queryMap.put("discovery_ru", "discovery");
			queryMap.put("electriccars_en", "electric cars");
			queryMap.put("electriccars_de", "Elektroautos");
			queryMap.put("electriccars_ru", "electric car");
			queryMap.put("internet_en", "internet");
			queryMap.put("internet_de", "Internet-");
			queryMap.put("internet_ru", "интернет");
			queryMap.put("iphone6_en", "iphone6");
			queryMap.put("iphone6_de", "iphone");
			queryMap.put("iphone6_ru", "iphone");
			queryMap.put("mars_en", "mars");
			queryMap.put("mars_de", "Mars");
			queryMap.put("mars_ru", "Марс");
			queryMap.put("patent_en", "patent");
			queryMap.put("patent_de", "Patent");
			queryMap.put("patent_ru", "патент");
			queryMap.put("productreview_en", "product review");
			queryMap.put("productreview_de", "product review");
			queryMap.put("productreview_ru", "обзор");
			queryMap.put("robot_en", "robot");
			queryMap.put("robot_de", "Roboter");
			queryMap.put("robot_ru", "робот");
			Iterator<String> it = queryMap.keySet().iterator();
			/*
			 * while (it.hasNext()) { String key = it.next();
			 * System.out.println("Executing for keyword: "+key);
			 * Thread.sleep(2000); String[] splitString = key.split("_"); String
			 * lang = splitString[1]; String queryStringForFile =
			 * splitString[0]; String queryForSearch = queryMap.get(key); String
			 * fromDate = "2015-09-15"; String toDate = "2015-09-16"; long
			 * initTime = System.currentTimeMillis();
			 * crawler.initConfiguration(); List<Status> tweets =
			 * crawler.searchTweets(queryForSearch, lang, fromDate, toDate); for
			 * (Status tweet : tweets) { System.out.println(tweet.getText()); }
			 * System.out.println("Writing tweets to file....."); int tweetCount
			 * = crawler.writeTweetsToFile(tweets, queryStringForFile + "_" +
			 * lang + "_" + df.format(sf.parse(fromDate)) + ".json", lang);
			 * System.out.println("Total tweets crawled: " + tweets.size());
			 * System.out.println("Total Tweets written to file: " +
			 * tweetCount); long finishTime = System.currentTimeMillis();
			 * System.out.println("Total time taken: " + (finishTime - initTime)
			 * + " milli seconds"); Thread.sleep(2000); }
			 */
			String lang = "ru";
			String queryStringForFile = "aliens";
			String queryForSearch = "discovery";
			String fromDate = "2015-09-16";
			String toDate = "2015-09-17";
			long initTime = System.currentTimeMillis();
			crawler.initConfiguration();
			List<Status> tweets = crawler.searchTweets(queryForSearch, lang, fromDate, toDate);
			for (Status tweet : tweets) {
				System.out.println(tweet.getText());
			}
			System.out.println("Writing tweets to file.....");
			// int tweetCount = crawler.writeTweetsToFile(tweets,
			// queryStringForFile + "_" + lang + "_" +
			// df.format(sf.parse(fromDate)) + ".json", lang);
			System.out.println("Total tweets crawled: " + tweets.size());
			// System.out.println("Total Tweets written to file: " +
			// tweetCount);
			long finishTime = System.currentTimeMillis();
			System.out.println("Total time taken: " + (finishTime - initTime) + " milli seconds");

			/*
			 * catch (ParseException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */ /*
				 * catch (InterruptedException e) { // TODO Auto-generated catch
				 * block e.printStackTrace(); }
				 */
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
