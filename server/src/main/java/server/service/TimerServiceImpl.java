package server.service;

import org.springframework.stereotype.Service;

@Service
public class TimerServiceImpl implements TimerService {

	public TimerServiceImpl() {
	}

	public long getTime() {
		return System.currentTimeMillis();
	}
}
