package commons.model;

/**
 * Model of an activity
 */
public record Activity(String name, String imageUrl, float energyInWh) {
	public Activity copyWithImageUrl(String imageUrl) {
		return new Activity(name, imageUrl, energyInWh);
	}
}
