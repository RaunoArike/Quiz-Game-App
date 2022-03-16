package importer.api;

import commons.model.Activity;

import java.util.List;

public interface ActivityApi {
	void addActivities(String serverUrl, List<Activity> activities);
}
