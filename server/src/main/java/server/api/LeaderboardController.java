
package server.api;

import java.util.List;
import commons.model.LeaderboardEntry;
import server.repository.LeaderboardRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class LeaderboardController {

	private final LeaderboardRepository repo;

	public LeaderboardController(LeaderboardRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/api/leaderboard")
	public List<LeaderboardEntry> getLeaderboard() {
		//TO DO
		return null;
	}
}