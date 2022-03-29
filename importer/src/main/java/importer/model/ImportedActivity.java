package importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import commons.model.Activity;

import java.nio.file.Path;

/**
 * Model representing an activity from the ActivityBank
 */
public record ImportedActivity(
		String id,
		@JsonProperty("image_path") String imagePath,
		String title,
		@JsonProperty("consumption_in_wh") long energyInWh,
		String source
) {
	public Activity toModel(String filePath) {
		String path = Path.of(filePath, imagePath).toString();
		return new Activity(title, path, energyInWh);
	}
}
