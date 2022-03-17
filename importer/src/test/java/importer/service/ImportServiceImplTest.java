package importer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.model.Activity;
import importer.api.ActivityApi;
import importer.model.ImportedActivity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImportServiceImplTest {
	private static final ImportedActivity[] FAKE_IMPORTED_ACTIVITIES = new ImportedActivity[] {
			new ImportedActivity("id1", null, "A1", 1L, null),
			new ImportedActivity("id2", null, "A2", 2L, null),
			new ImportedActivity("id3", null, "A3", 3L, null)
	};
	private static final List<Activity> FAKE_ACTIVITIES = List.of(
			new Activity("A1", null, 1f),
			new Activity("A2", null, 2f),
			new Activity("A3", null, 3f)
	);

	@Mock
	private ActivityApi activityApi;
	@Mock
	private ObjectMapper objectMapper;

	private ImportServiceImpl createService() {
		return new ImportServiceImpl(activityApi, objectMapper);
	}

	@Test
	public void addActivities_should_save_activities_to_repo() throws IOException {
		when(objectMapper.readValue(ArgumentMatchers.<File>any(), eq(ImportedActivity[].class)))
				.thenReturn(FAKE_IMPORTED_ACTIVITIES);

		var service = createService();
		service.importServicesFromFile("url", "filePath");

		verify(activityApi).addActivities("url", FAKE_ACTIVITIES);
	}
}
