package client.scenes;

import client.model.QuestionData;
import client.service.MessageLogicService;
import com.google.inject.Inject;
import commons.model.JokerType;
import commons.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public abstract class QuestionCtrl<Q extends Question> extends AbstractCtrl {

	private static final long TIMER_DEFAULT_TIME = 20000;
	private static final long TIMER_UPDATE_PERIOD = 1000;
	private static final long TIMER_SECOND = 1000;

	protected final MessageLogicService messageService;
	protected final MainCtrl mainCtrl;

	@FXML
	private Label questionText;

	@FXML
	private Label score;

	@FXML
	private Button reduceTimeJoker;

	@FXML
	private Button doublePointsJoker;

	@FXML
	private Button eliminateOptionJoker;

	@FXML
	private ProgressBar timerProgress;

	@FXML
	private Text timerNumber;

	private final Timer timer = new Timer();
	private TimerTask timerTask;

	@Inject
	public QuestionCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		this.messageService = messageService;
		this.mainCtrl = mainCtrl;
	}

	@Override
	public void init() {
		super.init();
		callTimeLimiter(TIMER_DEFAULT_TIME);
	}

	public void setQuestion(QuestionData<Q> questionData) {
		setScore(questionData.currentScore());
		setJokerAvailability(questionData.availableJokers());
	}

	protected void setQuestionText(String questionText) {
		this.questionText.setText(questionText);
	}

	private void setScore(int scoreNumber) {
		String scoreText = "Score: " + scoreNumber;
		this.score.setText(scoreText);
	}

	private void setJokerAvailability(Set<JokerType> availableJokers) {
		reduceTimeJoker.setDisable(!availableJokers.contains(JokerType.REDUCE_TIME));
		doublePointsJoker.setDisable(!availableJokers.contains(JokerType.DOUBLE_POINTS));
		eliminateOptionJoker.setDisable(!availableJokers.contains(JokerType.ELIMINATE_MC_OPTION));
	}

	private void callTimeLimiter(long initialTimeLeftMs) {
		timeStop();
		timerTask = new TimerTask() {
			long timeLeft = initialTimeLeftMs;

			@Override
			public void run() {
				if (timeLeft >= TIMER_UPDATE_PERIOD) {
					timeLeft -= TIMER_UPDATE_PERIOD;
					timerProgress.setProgress(timeLeft / (float) TIMER_DEFAULT_TIME);
					timerNumber.setText(String.valueOf(Math.round(timeLeft / (float) TIMER_SECOND)));
				} else {
					timerNumber.setText("0");
					timerProgress.setProgress(0);
				}
			}
		};
		timer.schedule(timerTask, 0, TIMER_UPDATE_PERIOD);
	}

	protected void timeStop() {
		if (timerTask != null) {
			timerTask.cancel();
		}
	}

	public void notifyReduceTimePlayed(long timeLeftMs) {
		callTimeLimiter(timeLeftMs);
	}

	public void useReduceTimeJoker() {
		messageService.sendJoker(JokerType.REDUCE_TIME);
		reduceTimeJoker.setDisable(true);
	}

	public void useDoublePointsJoker() {
		messageService.sendJoker(JokerType.DOUBLE_POINTS);
		doublePointsJoker.setDisable(true);
	}

	public void useEliminateOptionJoker() {
		messageService.sendJoker(JokerType.ELIMINATE_MC_OPTION);
		eliminateOptionJoker.setDisable(true);
	}
}
