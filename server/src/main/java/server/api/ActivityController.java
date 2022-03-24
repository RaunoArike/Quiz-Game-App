package server.api;

import commons.model.Activity;
import org.springframework.web.bind.annotation.*;
import server.exception.IdNotFoundException;
import server.service.ActivityService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Controller for admin panel interactions
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {
	private final ActivityService activityService;

	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
	}

	/**
	 * Provides a list of all activities on the server.
	 *
	 * @return returns a list of activities
	 */
	@GetMapping("/api/admin")
	public List<Activity> getActivities() {
		return activityService.provideActivities();
	}

	/**
	 * Adds new activities.
	 *
	 * @param activities list of activities to add
	 */
	@PostMapping
	public void addActivities(@RequestBody List<Activity> activities) {
		activityService.addActivities(activities);
	}

	/**
	 * Removes all activities.
	 */
	@DeleteMapping
	public void removeAllActivities() {
		activityService.removeAllActivities();
	}

	/**
	 * Updates the contents of a single activity.
	 * Throws an IdNotFoundException if there isn't an activity with the id specified by the user.
	 *
	 * @param id the id of the activity to update
	 * @param activity the activity to replace the old activity with
	 */
	@PutMapping("/api/activities/{id}")
	public void updateActivity(@PathVariable long id, Activity activity) {
		try {
			activityService.updateActivity(id, activity);
		} catch (EntityNotFoundException e) {
			throw new IdNotFoundException();
		}
	}
}
