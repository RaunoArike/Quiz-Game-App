package server.service;

import org.springframework.stereotype.Service;

@Service
public class TimerService {

	private final int gameId;

	public TimerService(int gameId) {
		this.gameId = gameId;
	}

	public void startTimer() {

	}

	public int stopTimer() {

	}
}
