package server.service;

/**
 * Service for the timer
 */
public interface TimerService {
	/**
	 * Provides the time at the current moment
	 * @return returns the current time in milliseconds
	 */
	long getTime();
}
