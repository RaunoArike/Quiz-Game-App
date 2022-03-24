package server.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class TimerServiceImpl implements TimerService {

	Map<Integer, Runnable> taskMap = new HashMap<>();
	Map<Integer, Timer> timerMap = new HashMap<>();

	public TimerServiceImpl() {
	}

	public long getTime() {
		return System.currentTimeMillis();
	}

	public void scheduleTimer(int timerId, long delay, Runnable runnable) {
		Timer timer = new Timer();
		taskMap.put(timerId, runnable);
		timerMap.put(timerId, timer);

		timer.schedule(
				new TimerTask() {
					@Override
					public void run() {
						runnable.run();
					}
				}, delay
		);
	}

	public void rescheduleTimer(int timerId, long delay) {
		Timer timer = new Timer();
		Runnable runnable = taskMap.get(timerId);
		timerMap.get(timerId).cancel();
		timerMap.put(timerId, timer);

		timer.schedule(
				new TimerTask() {
					@Override
					public void run() {
						runnable.run();
					}
				}, delay
		);
	}

	public long getRemainingTime(int timerId) {
		// TODO
		return 0;
	}
}
