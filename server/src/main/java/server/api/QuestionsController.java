package server.api;

import commons.model.Activity;
import commons.model.Question;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class QuestionsController {

	@GetMapping("api/questions")
	public List<Question> getQuestions() {
		return List.of(
				new Question.MultiChoice(List.of(
						new Activity("Activity A", null),
						new Activity("Activity B", null)
				), 0),
				new Question.MultiChoice(List.of(
						new Activity("Activity C", null),
						new Activity("Activity D", null)
				), 1)
		);
	}
}
