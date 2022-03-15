package commons.model;

import lombok.Data;

/**
 * Model of an activity
 */
@Data
public class Activity {
	private final String name;
	private final String imageUrl;
	private final float energyInWh;
}
