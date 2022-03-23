package server.service;

import java.util.List;
import commons.model.LeaderboardEntry;
import server.entity.LeaderboardEntity;
import server.repository.LeaderboardRepository;

public class LeaderboardServiceImpl implements LeaderboardService {

	private final LeaderboardRepository repository;

	public LeaderboardServiceImpl(LeaderboardRepository repository) {
		this.repository = repository;
	}

	@Override
	public void addtoLeaderboard(LeaderboardEntry entry) {
		repository.save(LeaderboardEntity.fromModel(entry));
	}

	@Override
	public void clearLeaderboard() {
		repository.deleteAll();
	}

	public List<LeaderboardEntry> getTopLeaderboardEntries() {
		//TO DO - query the repository for the top ten entries, sorted descending
		return null;
	}

}
