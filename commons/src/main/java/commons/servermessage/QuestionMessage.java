package commons.servermessage;

import commons.model.Question;

public record QuestionMessage(Question question, int questionNumber) { }
