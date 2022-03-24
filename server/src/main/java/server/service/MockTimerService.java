package server.service;


/**
 * A mock TimerService class that enables testing without timer delay for methods that use the timer.
 */
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
		// TODO
	}

	@Override
	public long getRemainingTime(int timerId) {
		// TODO
		return 0;
	}
}
