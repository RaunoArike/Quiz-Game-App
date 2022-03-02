package server.api;

import commons.Question;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class QuestionsController {

    @GetMapping("api/questions")
    public List<Question> getQuestions() {
        return List.of(new Question("Estimation", "How much electricity does a computer consume in an hour?", "200 W"),
                (new Question("Estimation", "How much electricity does a wind turbine generate in an hour?", "1 MW")));
    }
}
