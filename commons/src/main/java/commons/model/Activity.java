package commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Model of an activity
 */
@AllArgsConstructor
@Getter
public class Activity {
	private final String name;
	private final String imageUrl;
}
