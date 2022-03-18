package server.service;

import commons.model.Question;

/**
 * Question generation service
 */
public interface QuestionService {
	/**
	 * Generates a new question
	 * @param gameId the game the question is generated for
	 * @return generated question
	 */
	Question generateQuestion(int gameId);

	/**
	 * Calculates the score a player gets for an answer to a question
	 * @param question the question that the player answered
	 * @param answer the answer that the player gave
	 * @return the score of the player
	 */
	int calculateScore(Question question, Number answer);
}
