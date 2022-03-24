package server.service;

import java.util.Comparator;
import java.util.List;
import commons.model.LeaderboardEntry;
import server.entity.LeaderboardEntity;
import server.repository.LeaderboardRepository;

public class LeaderboardServiceImpl implements LeaderboardService {

	private final LeaderboardRepository repository;

	private static final int NUMBER_OF_TOP_ENTRIES_TO_RETURN = 10;

	public LeaderboardServiceImpl(LeaderboardRepository repository) {
		this.repository = repository;
	}

	@Override
	public void addToLeaderboard(LeaderboardEntry entry) {
		repository.save(LeaderboardEntity.fromModel(entry));
	}

	@Override
	public void clearLeaderboard() {
		repository.deleteAll();
	}

	public List<LeaderboardEntry> getTopLeaderboardEntries() {
		//TO DO - query the repository for the top ten entries, sorted descending
		List<LeaderboardEntry> entryList = repository.findAll().stream()
			.sorted(Comparator.<LeaderboardEntity>comparingInt(entry -> entry.getScore()).reversed())
			.limit(NUMBER_OF_TOP_ENTRIES_TO_RETURN)
			.map(LeaderboardEntity::toModel)
			.toList();
		return entryList;
	}

}
