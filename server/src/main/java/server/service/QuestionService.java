package server.service;

import commons.model.Question;

public interface QuestionService {
    Question generateQuestion(int gameId);
}
