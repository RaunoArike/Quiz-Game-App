package client.scenes;

import client.service.ServerService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;


public abstract class QuestionCtrl {

	protected final ServerService server;
	protected final MainCtrl mainCtrl;
	private static final int PERIOD = 1000;

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
	private Text number;

	private Timer timer = new Timer();

	@Inject
	public QuestionCtrl(ServerService server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void setQuestion(String questionText) {
		this.questionText.setText(questionText);
	}

	public void setScore(int score) {
		String scoreText = "Score: " + score;
		this.score.setText(scoreText);
	}

	public void callTimeLimiter() {
		TimerTask timerTask = new TimerTask() {
			double timeLeft = 1;

			@Override
			public void run() {
				final double progressPercentage = 0.05;
				final double seconds = 20;
				if (timeLeft >= progressPercentage) {
					timeLeft -= progressPercentage;
					timerProgress.setProgress(timeLeft);
					number.setText(String.valueOf(Math.round(timerProgress.getProgress() * seconds)));
				} else {
					number.setText("0");
					timerProgress.setProgress(0);
				}
			}
		};
		timer.schedule(timerTask, 0, PERIOD);
	}

	public void timeStop() {
		timer.cancel();
	}
}
