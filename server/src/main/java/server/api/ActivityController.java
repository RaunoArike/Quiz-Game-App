package server.api;

import commons.model.Activity;
import org.springframework.web.bind.annotation.*;
import server.service.ActivityService;

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

	@PutMapping("/api/activities/{id}")
	public void updateActivity(@PathVariable long id, Activity activity) {
		activityService.updateActivity(id, activity);
	}
}
