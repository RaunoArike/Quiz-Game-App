package server.service.question;

import commons.model.Question;

public interface QuestionService {
	Question generateQuestion(int gameId);

	int calculateScore(Question question, Number answer);
}
