package client.scenes;

import client.model.GameType;
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

public abstract class QuestionCtrl<Q extends Question> extends AbstractCtrl {

	private static final long TIMER_DEFAULT_TIME = 20000;
	private static final long TIMER_UPDATE_PERIOD = 1000;
	private static final long TIMER_SECOND = 1000;
	private static final int EMOJI_Y_OFFSET = 50;

	private static final int LOL_EMOJI_TYPE = 0;
	private static final int SUNGLASSES_EMOJI_TYPE = 1;
	private static final int LIKE_EMOJI_TYPE = 2;
	private static final int DISLIKE_EMOJI_TYPE = 3;
	private static final int ANGRY_EMOJI_TYPE = 4;
	private static final int VOMIT_EMOJI_TYPE = 5;
	private static final int EMOJI_MOVEMENT_RANGE = -170;

	protected final MessageLogicService messageService;
	protected final MainCtrl mainCtrl;

	private final Timer timer = new Timer();

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
	public ProgressBar timerProgress;

	@FXML
	private Text timerNumber;

	private QuestionData<Q> questionData;
	private TimerTask timerTask;


	@Inject
	public QuestionCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		this.messageService = messageService;
		this.mainCtrl = mainCtrl;
	}

	@Override
	public void init() {
		super.init();
		// lolEmoji.setLayoutY(lolEmoji.getLayoutY() + 50);
		// sunglassesEmoji.setLayoutY(sunglassesEmoji.getLayoutY() + 50);
		// likeEmoji.setLayoutY(likeEmoji.getLayoutY() + 50);
		// dislikeEmoji.setLayoutY(dislikeEmoji.getLayoutY() + 50);
		// angryEmoji.setLayoutY(angryEmoji.getLayoutY() + 50);
		// vomitEmoji.setLayoutY(vomitEmoji.getLayoutY() + 50);

		callTimeLimiter(TIMER_DEFAULT_TIME);
	}


	public void setQuestion(QuestionData<Q> questionData) {
		timerProgress.setStyle("-fx-accent: blue;");
		this.questionData = questionData;
		setScore(questionData.currentScore());
		setJokerAvailability(questionData.availableJokers());
		setEmojis(questionData.gameType());
	}

	private void setEmojis(GameType gameType) {
		lolEmoji.setVisible(false);
		sunglassesEmoji.setVisible(false);
		likeEmoji.setVisible(false);
		dislikeEmoji.setVisible(false);
		angryEmoji.setVisible(false);
		vomitEmoji.setVisible(false);
		boolean isMultiPlayer = !(gameType == GameType.SINGLE);
		lolEmojiStatic.setVisible(isMultiPlayer);
		sunglassesEmojiStatic.setVisible(isMultiPlayer);
		likeEmojiStatic.setVisible(isMultiPlayer);
		dislikeEmojiStatic.setVisible(isMultiPlayer);
		angryEmojiStatic.setVisible(isMultiPlayer);
		vomitEmojiStatic.setVisible(isMultiPlayer);
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

	protected void disableJokers() {
		setJokerAvailability(Set.of());
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
		useEmoji(LOL_EMOJI_TYPE);
		notifyEmojiPlayed(LOL_EMOJI_TYPE);
	}

	public void handleSunglassesEmojiClicks() {
		useEmoji(SUNGLASSES_EMOJI_TYPE);
		notifyEmojiPlayed(SUNGLASSES_EMOJI_TYPE);
	}

	public void handleLikeEmojiClicks() {
		useEmoji(LIKE_EMOJI_TYPE);
		notifyEmojiPlayed(LIKE_EMOJI_TYPE);
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
		timerProgress.setStyle("-fx-accent: red;");
	}

	public void useDoublePointsJoker() {
		messageService.sendJoker(JokerType.DOUBLE_POINTS);
		doublePointsJoker.setDisable(true);
	}

	public void useEliminateOptionJoker() {
		messageService.sendJoker(JokerType.ELIMINATE_MC_OPTION);
		eliminateOptionJoker.setDisable(true);
	}

	protected QuestionData<Q> getQuestionData() {
		return questionData;
	}

	private void useEmoji(int emojiType) {
		switch (emojiType) {
			case LOL_EMOJI_TYPE:
				messageService.sendEmoji(LOL_EMOJI_TYPE);
				break;

			case SUNGLASSES_EMOJI_TYPE:
				messageService.sendEmoji(SUNGLASSES_EMOJI_TYPE);
				break;

			case LIKE_EMOJI_TYPE:
				messageService.sendEmoji(LIKE_EMOJI_TYPE);
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
			case LOL_EMOJI_TYPE:
				animateEmoji(lolEmoji, lolEmojiStatic);
				break;

			case SUNGLASSES_EMOJI_TYPE:
				animateEmoji(sunglassesEmoji, sunglassesEmojiStatic);
				break;

			case LIKE_EMOJI_TYPE:
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

	private void animateEmoji(ImageView emoji, ImageView staticEmoji) {
		staticEmoji.setDisable(true);
		emoji.setLayoutY(emoji.getLayoutY() + EMOJI_Y_OFFSET);
		emoji.setVisible(true);
		TimerTask emojiTimer = new TimerTask() {

			@Override
			public void run() {
				emoji.setVisible(false);
				staticEmoji.setDisable(false);
				emoji.setLayoutY(emoji.getLayoutY() - EMOJI_Y_OFFSET);
			}

		};
		timer.schedule(emojiTimer, (TIMER_SECOND) * 2);

		TranslateTransition translate = new TranslateTransition();
		translate.setNode(emoji);
		translate.setDuration(Duration.millis(TIMER_SECOND));
		translate.setByY(EMOJI_MOVEMENT_RANGE);
		translate.setCycleCount(2);
		translate.setAutoReverse(true);
		translate.play();
	}
}