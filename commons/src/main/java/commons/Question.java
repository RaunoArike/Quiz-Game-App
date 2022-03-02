package commons;

public class Question {

	private String type;
	private String content;
	private String correctAnswer;

	public Question(String type, String content, String correctAnswer) {
		this.type = type;
		this.content = content;
		this.correctAnswer = correctAnswer;
	}

	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}
}
