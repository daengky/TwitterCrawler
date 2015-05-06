package TwitterCrawler;

import java.util.Map;
import java.util.logging.Logger;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApp {
	// The name of this TwitterApp
	public final String name;
	
	// Application authentication information
	public final String API_KEY;			// Consumers key
	public final String API_SECRET;			// Consumers secret
	public final String ACCESS_KEY;
	public final String ACCESS_SECRET;
	
	// twitter4j instance
	public Twitter twitter = null;
	
	public TwitterApp(String name, String API_KEY, String API_SECRET, String ACCESS_KEY, String ACCESS_SECRET) {
		this.name = name;
		this.API_KEY = API_KEY;
		this.API_SECRET = API_SECRET;
		this.ACCESS_KEY = ACCESS_KEY;
		this.ACCESS_SECRET = ACCESS_SECRET;
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(API_KEY);
		builder.setOAuthConsumerSecret(API_SECRET);
		builder.setOAuthAccessToken(ACCESS_KEY);
		builder.setOAuthAccessTokenSecret(ACCESS_SECRET);
		this.twitter = new TwitterFactory(builder.build()).getInstance();
		
		/**
		 * Do not use the following RateLimitStatusListener
		 * due to its possible unstable cases when the crawler is starting.
		 * I replace this listener into TwitterException handler for each API call.
		 * @author ChangUk
		 */
//			this.twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
//				@Override
//				public void onRateLimitStatus(RateLimitStatusEvent event) {
//					RateLimitStatus status = event.getRateLimitStatus();
//					AppManager appManager = AppManager.getSingleton();
//					if (status.getRemaining() == 0) {
//						appManager.registerLimitedApp(TwitterApp.this, status.getSecondsUntilReset());
//						printRateLimitStatus();
//					}
//				}
//				
//				@Override
//				public void onRateLimitReached(RateLimitStatusEvent event) {
//					RateLimitStatus status = event.getRateLimitStatus();
//					AppManager appManager = AppManager.getSingleton();
//					appManager.registerLimitedApp(TwitterApp.this, status.getSecondsUntilReset());
//					printRateLimitStatus();
//				}
//			});
	}
	
	/**
	 * Print current limited endpoints list
	 * @param curEndpoint
	 */
	public void printRateLimitStatus(String curEndpoint) {
		Logger logger = Logger.getGlobal();
		AppManager appManager = AppManager.getSingleton();
		logger.info("----------------------- Limited Endpoint Status -----------------------\n"
				+ "- App(" + name + " - '" + curEndpoint + "') reaches rate limited.");
		for (String endpoint : appManager.limitedEndpoints.keySet()) {
			logger.info("- Endpoint('" + endpoint + "'): " + appManager.limitedEndpoints.get(endpoint).size() + " apps.");
		}
		logger.info("-----------------------------------------------------------------------");
	}
	
	/**
	 * Warning: This function prints out weird result of rate limit status.
	 */
	@Deprecated
	public void printRateLimitStatusFromTwitter() {
		Logger logger = Logger.getGlobal();
		try {
			Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
			for (String endpoint : rateLimitStatus.keySet()) {
				RateLimitStatus status = rateLimitStatus.get(endpoint);
				logger.info("\tApp" + name + " - '" + endpoint + "' -\t\t" + status.getRemaining() + "/" + status.getLimit()
						+ ": " + status.getSecondsUntilReset());
			}
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}
}