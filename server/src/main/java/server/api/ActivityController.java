package server.api;

import commons.model.Activity;
import org.springframework.web.bind.annotation.*;
import server.exception.IdNotFoundException;
import server.service.ActivityService;
import server.service.FileStorageService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Controller for admin panel interactions
 */
@RestController
@RequestMapping("api/activities")
public class ActivityController {
	private final ActivityService activityService;
	private final FileStorageService storageService;

	public ActivityController(ActivityService activityService, FileStorageService storageService) {
		this.activityService = activityService;
		this.storageService = storageService;
	}

	/**
	 * Provides a list of all activities on the server.
	 *
	 * @return returns a list of activities
	 */
	@GetMapping
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
	 * Removes one activity from the DB.
	 *
	 * @param id the id of the activity to remove
	 */
	@DeleteMapping("/{id}")
	public void removeOneActivity(@PathVariable long id) {
		try {
			activityService.removeActivity(id);
		} catch (EntityNotFoundException e) {
			throw new IdNotFoundException();
		}
	}

	/**
	 * Updates the contents of a single activity.
	 * Throws an IdNotFoundException if there isn't an activity with the id specified by the user.
	 *
	 * @param id the id of the activity to update
	 * @param activity the activity to replace the old activity with
	 */
	@PutMapping("/{id}")
	public void updateActivity(@PathVariable long id, Activity activity) {
		try {
			activityService.updateActivity(id, activity);
		} catch (EntityNotFoundException e) {
			throw new IdNotFoundException();
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("files") MultipartFile file) {
		String message = "";
		try {
			storageService.save(file);
			message = "Uploaded successfully : " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			message = "Could not upload : " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}
}
