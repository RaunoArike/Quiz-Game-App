package client.scenes;

import client.model.QuestionData;
import client.service.MessageLogicService;
import com.google.inject.Inject;
import commons.model.JokerType;
import commons.model.Question;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class QuestionCtrl<Q extends Question> extends AbstractCtrl {

	private static final long TIMER_DEFAULT_TIME = 20000;
	private static final long TIMER_UPDATE_PERIOD = 1000;
	private static final long TIMER_SECOND = 1000;
	private static final int DISLIKE_EMOJI_TYPE = 3;
	private static final int ANGRY_EMOJI_TYPE = 4;
	private static final int VOMIT_EMOJI_TYPE = 5;
	private static final int MOVE_EMOJI = -150;

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
	private ImageView lolEmoji;

	@FXML
	private ImageView sunglassesEmoji;

	@FXML
	private ImageView likeEmoji;

	@FXML
	private ImageView dislikeEmoji;

	@FXML
	private ImageView angryEmoji;

	@FXML
	private ImageView vomitEmoji;

	@FXML
	private ImageView lolEmojiStatic;

	@FXML
	private ImageView sunglassesEmojiStatic;

	@FXML
	private ImageView likeEmojiStatic;

	@FXML
	private ImageView dislikeEmojiStatic;

	@FXML
	private ImageView angryEmojiStatic;

	@FXML
	private ImageView vomitEmojiStatic;

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

	public void handleLolEmojiClicks() {
		useEmoji(0);
		notifyEmojiPlayed(0);
	}

	public void handleSunglassesEmojiClicks() {
		useEmoji(1);
		notifyEmojiPlayed(1);
	}

	public void handleLikeEmojiClicks() {
		useEmoji(2);
		notifyEmojiPlayed(2);
	}

	public void handleDislikeEmojiClicks() {
		useEmoji(DISLIKE_EMOJI_TYPE);
		notifyEmojiPlayed(DISLIKE_EMOJI_TYPE);
	}

	public void handleAngryEmojiClicks() {
		useEmoji(ANGRY_EMOJI_TYPE);
		notifyEmojiPlayed(ANGRY_EMOJI_TYPE);
	}

	public void handleVomitEmojiClicks() {
		useEmoji(VOMIT_EMOJI_TYPE);
		notifyEmojiPlayed(VOMIT_EMOJI_TYPE);
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

	private void useEmoji(int emojiType) {
		switch (emojiType) {
			case 0:
				messageService.sendEmoji(0);
				break;

			case 1:
				messageService.sendEmoji(1);
				break;

			case 2:
				messageService.sendEmoji(2);
				break;

			case DISLIKE_EMOJI_TYPE:
				messageService.sendEmoji(DISLIKE_EMOJI_TYPE);
				break;

			case ANGRY_EMOJI_TYPE:
				messageService.sendEmoji(ANGRY_EMOJI_TYPE);
				break;

			case VOMIT_EMOJI_TYPE:
				messageService.sendEmoji(VOMIT_EMOJI_TYPE);
		}
	}

	public void notifyEmojiPlayed(int emojiType) {
		switch (emojiType) {
			case 0:
				animateEmoji(lolEmoji, lolEmojiStatic);
				break;

			case 1:
				animateEmoji(sunglassesEmoji, sunglassesEmojiStatic);
				break;

			case 2:
				animateEmoji(likeEmoji, likeEmojiStatic);
				break;

			case DISLIKE_EMOJI_TYPE:
				animateEmoji(dislikeEmoji, dislikeEmojiStatic);
				break;

			case ANGRY_EMOJI_TYPE:
				animateEmoji(angryEmoji, angryEmojiStatic);
				break;

			case VOMIT_EMOJI_TYPE:
				animateEmoji(vomitEmoji, vomitEmojiStatic);
				break;
		}
	}

	private void animateEmoji(ImageView emojiType, ImageView toDisable) {
		toDisable.setDisable(true);

		TranslateTransition translate = new TranslateTransition();
		translate.setNode(emojiType);
		translate.setDuration(Duration.millis(TIMER_SECOND));
		translate.setByY(MOVE_EMOJI);
		translate.setCycleCount(2);
		translate.setAutoReverse(true);
		translate.play();

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.schedule(() -> toDisable.setDisable(false), 2, TimeUnit.SECONDS);
	}
}
