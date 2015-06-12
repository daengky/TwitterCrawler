
import java.util.ArrayList;

import twitter4j.User;
import Common.EgoNetwork;
import TwitterCrawler.Crawler;
import TwitterCrawler.Engine;

public class TwitterMiner {
	public static void main(String[] arg) {
		Engine engine = Engine.getSingleton();
		
		ArrayList<User> seedUsers = new ArrayList<User>();
//		seedUsers.add(engine.getUser(78199077L));		// Jiwon
		seedUsers.add(engine.getUser(1188870223L));		// ChangUk
//		seedUsers.add(engine.getUser(3182892457L));		// JeeIn
		
		for (User seedUser : seedUsers) {
			EgoNetwork egoNetwork = new EgoNetwork(seedUser, 1);
			egoNetwork.init();
			Crawler crawler = new Crawler(egoNetwork);
			crawler.run();
		}
		
		System.out.println("Finished");
	}
}
