package server.api;

import commons.model.Question;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class GameController {

    @MessageMapping("/question")
    @SendToUser("")
    public List getQuestion() {
        Question question = QuestionServiceImpl.generateQuestion();
        return List.of(question, 1);
    }


    @MessageMapping("/answer")
    public List<Integer> getAnswer() {
        int answer = QuestionServiceImpl.generateAnswer(question.getActivities());
        int questionScore = 0;
        if (answer.equals(userAnswer)) {
            questionScore = 1000 - timePassed * 0.01;
            totalScore = totalScore + questionScore;
        }
        return 	List.of(questionScore, totalScore);
    }
}