package importer.api;

import commons.model.Activity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

public class ActivityApiImpl implements ActivityApi {
	private final RestTemplate restTemplate = new RestTemplate();

	@Override
	public void addActivities(String serverUrl, List<Activity> activities) {
		restTemplate.postForEntity(serverUrl + "/api/activities", activities, Object.class);
	}

	@Override
	public void uploadImage(String serverUrl, String imagePath, File image) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", image);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		restTemplate.postForEntity(serverUrl + "/images", requestEntity, Object.class);
	}

	@Override
	public void deleteAllActivities(String serverUrl) {
		restTemplate.delete(serverUrl + "/api/activities");
	}
}
