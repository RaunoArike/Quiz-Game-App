package importer.api;

import commons.model.Activity;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ActivityApiImpl implements ActivityApi {
	private final Client client = ClientBuilder.newClient(new ClientConfig());

	@Override
	public void addActivities(String serverUrl, List<Activity> activities) {
		client.target(serverUrl)
				.path("api/activities")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(activities, APPLICATION_JSON));
	}

	@Override
	public void deleteAllActivities(String serverUrl) {
		client.target(serverUrl)
				.path("api/activities")
				.request()
				.delete();
	}
}
