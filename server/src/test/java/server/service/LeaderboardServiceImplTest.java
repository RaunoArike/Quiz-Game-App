package server.service;


import commons.model.LeaderboardEntry;
import server.entity.LeaderboardEntity;
import server.repository.LeaderboardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceImplTest {

	private static final LeaderboardEntry FAKE_ENTRY = new LeaderboardEntry("Name", 100);

	@Mock
	private LeaderboardRepository leaderboardRepository;

	@Captor
	private ArgumentCaptor<LeaderboardEntity> leaderboardEntityCaptor;

	private LeaderboardServiceImpl createService() {
		return new LeaderboardServiceImpl(leaderboardRepository);
	}

	@Test
	public void addtoLeaderboard_should_save_entry() {
		var service = createService();
		service.addtoLeaderboard(FAKE_ENTRY);

		verify(leaderboardRepository).save(leaderboardEntityCaptor.capture());
	}

	@Test
	public void clearLeaderboard_should_remove_all_entries() {
		var service = createService();
		service.clearLeaderboard();

		verify(leaderboardRepository).deleteAll();
	}

}
