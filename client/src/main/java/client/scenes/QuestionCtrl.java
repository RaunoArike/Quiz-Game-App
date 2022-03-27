package client.scenes;

import client.service.ServerService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public abstract class QuestionCtrl {

	private static final int TIMER_UPDATE_PERIOD = 1000;
	private static final double TIMER_PROGRESS_PERCENTAGE = 0.05;
	private static final double TIMER_SECONDS = 20;

	protected final ServerService server;
	protected final MainCtrl mainCtrl;

	@FXML
	private Label questionText;

	@FXML
	private Label score;

	@FXML
	private Button doublePoints;

	@FXML
	private Button eliminateOption;

	@FXML
	private ProgressBar timerProgress;

	@FXML
	private Text timerNumber;

	private final Timer timer = new Timer();
	private TimerTask timerTask;

	@Inject
	public QuestionCtrl(ServerService server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void setQuestion(String questionText) {
		this.questionText.setText(questionText);

	}

	public void setScore(int scoreNumber) {
		String scoreText = "Score: " + scoreNumber;
		this.score.setText(scoreText);
	}

	public void callTimeLimiter() {
		timerTask = new TimerTask() {
			double timeLeft = 1;

			@Override
			public void run() {
				if (timeLeft >= TIMER_PROGRESS_PERCENTAGE) {
					timeLeft -= TIMER_PROGRESS_PERCENTAGE;
					timerProgress.setProgress(timeLeft);
					timerNumber.setText(String.valueOf(Math.round(timerProgress.getProgress() * TIMER_SECONDS)));
				} else {
					timerNumber.setText("0");
					timerProgress.setProgress(0);
				}
			}
		};
		timer.schedule(timerTask, 0, TIMER_UPDATE_PERIOD);
	}

	public void timeStop() {
		timerTask.cancel();
	}
}
