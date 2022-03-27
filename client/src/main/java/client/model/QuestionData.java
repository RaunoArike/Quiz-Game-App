package client.model;

import commons.model.Question;

public record QuestionData<Q extends Question>(
		Q question,
		int questionNumber,
		int currentScore,
		GameType gameType
) { }
