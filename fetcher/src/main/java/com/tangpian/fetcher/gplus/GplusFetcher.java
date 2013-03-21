package com.tangpian.fetcher.gplus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.tangpian.fetcher.Fetcher;

@Component
public class GplusFetcher implements Fetcher {

	private static Logger logger = LoggerFactory.getLogger(GplusFetcher.class);

	public static final String TYPE_CONTENT = "CONTENTS";
	public static final String TYPE_RELATION = "RELATIONS";

	public static final int MAX_QUOTAS = 10000;

	private static int quotas = 0;

	private Plus plus;

	public GplusFetcher(Plus plus) {
		this.plus = plus;
	}

	public List<String> fetchContent(String account) {

		List<String> contents = new ArrayList<String>();
		try {
			Plus.Activities.List listActivities = plus.activities().list(
					account, "public");
			listActivities.setMaxResults(100L);

			// Execute the request for the first page
			checkQuotas();
			ActivityFeed activityFeed = listActivities.execute();
			quotas++;
			logger.debug("Google Api Call Number:" + quotas);

			// Unwrap the request and extract the pieces we want
			List<Activity> activities = activityFeed.getItems();

			// Loop through until we arrive at an empty page
			while (activities != null) {
				for (Activity activity : activities) {
//					StringBuffer buffer = new StringBuffer();
//					buffer.append(activity.getId() + "	");
//					buffer.append(activity.getTitle() + "	");
//					buffer.append(activity.getUrl() + "	");
//					buffer.append(activity.getProvider() + "	");
//					buffer.append(activity.getGeocode() + "	");
//					buffer.append(activity.getObject().getPlusoners() + "	");
//					buffer.append(activity.getObject().getReplies() + "	");
//					buffer.append(activity.getObject().getResharers() + "	");
//					// buffer.append(new SimpleDateFormat("yyyy-MM-dd")
//					// .format(activity.getPublished()) + "	");
//					buffer.append(activity.getPublished() + "	");
//					buffer.append(activity.getObject().getContent() + "	");
//					buffer.append(activity.getProvider());
//					contents.add(buffer.toString());
					String content = activity.getObject().getContent().trim();
					if (!content.isEmpty()) {
						contents.add(content);
					}				
				}

				// We will know we are on the last page when the next page token
				// is
				// null.
				// If this is the case, break.
				if (activityFeed.getNextPageToken() == null) {
					break;
				}

				// Prepare to request the next page of activities
				listActivities.setPageToken(activityFeed.getNextPageToken());

				// Execute and process the next page request
				checkQuotas();
				activityFeed = listActivities.execute();
				quotas++;
				logger.debug("Google Api Call Number:" + quotas);
				activities = activityFeed.getItems();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contents;
	}

	private void checkQuotas() {
		if (quotas >= MAX_QUOTAS) {
			throw new RuntimeException("Google Api Quotas exceed the limit");
		}
	}

	// private List<Relation> fetchRelation(List<Content> contents) {
	// List<Relation> relations = new ArrayList<Relation>();
	//
	// // for (Content content : contents) {
	// // try {
	// // List<Person> people = gplusBuilder.getServicePlus().people()
	// // .listByActivity(content.getContentNo(), "plusoners")
	// // .execute().getItems();
	// // for (Person person : people) {
	// // relations.add(new Relation(content.getProfile()
	// // .getAccount(), person.getId()));
	// // }
	// // } catch (IOException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // } catch (GeneralSecurityException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// // }
	// // TODO Auto-generated method stub
	// return relations;
	// }

}
