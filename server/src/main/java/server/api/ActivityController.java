package server.api;

import commons.model.Activity;
import org.springframework.web.bind.annotation.*;
import server.service.ActivityService;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
	private final ActivityService activityService;

	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
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

	@DeleteMapping
	public void removeAllActivities() {
		activityService.removeAllActivities();
	}
}
