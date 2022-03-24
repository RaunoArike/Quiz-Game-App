package server.service;

public class MockTimerService implements TimerService {
	@Override
	public long getTime() {
		return System.currentTimeMillis();
	}

	@Override
	public void scheduleTimer(int timerId, long delay, Runnable runnable) {
		runnable.run();
	}

	@Override
	public void rescheduleTimer(int timerId, long delay) {

	}

	@Override
	public long getRemainingTime(int timerId) {
		return 0;
	}
}
