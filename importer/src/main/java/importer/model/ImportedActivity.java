package importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import commons.model.Activity;

public record ImportedActivity(
		String id,
		@JsonProperty("image_path") String imagePath,
		String title,
		@JsonProperty("consumption_in_wh") long consumptionInWh,
		String source
) {
	public Activity toModel() {
		return new Activity(title, imagePath, consumptionInWh);
	}
}
