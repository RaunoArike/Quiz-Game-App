package server.service;

import java.util.List;
import commons.model.LeaderboardEntry;

public interface LeaderboardService {

	/**
	 * Adds a player to the leaderboard
	 * @param entry the name of the entry
	 */
	void addToLeaderboard(LeaderboardEntry entry);

	/**
	 * Removes all existing leaderboard entry records from the repository
	 */
	void clearLeaderboard();

	/**
	 * Returns the ten highest leaderboard entries from the repository
	 * @return list of leaderboard entries
	 */
	List<LeaderboardEntry> getTopLeaderboardEntries();
}
