package server.service;

import commons.model.Activity;
import commons.model.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import server.entity.ActivityEntity;
import server.repository.ActivityRepository;

public class QuestionServiceImpl implements QuestionService {
	private final List<ActivityEntity> visited = new ArrayList<>();
	private final ActivityRepository activityRepository;
	private static final int NUM = 3;

	public QuestionServiceImpl(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	public ActivityRepository getActivityRepository() {
		return activityRepository;
	}

	public Question generateQuestion(int gameId) {

		int no = Math.abs(new Random().nextInt());

		switch (no % NUM) {
			case 0:	return generateMC();
			case 1: return generateEst();
			case 2: return generateCal();
		}
		return null;
	}

	public List<ActivityEntity> generateActivities() {
		Collections.shuffle(activityRepository.findAll());
		List<ActivityEntity> listActivities = activityRepository.findAll();
		List<ActivityEntity> selectedEntities = new ArrayList<>();

		int index = 0;
		///MAGIC NUMBERS HAVE TO BE REMOVED

		while (index < NUM) {
			int no = Math.abs(new Random().nextInt());
			ActivityEntity act = listActivities.get(no % listActivities.size());
			Activity activityFromEnt = new Activity(act.getName(), act.getImageUrl());
			if (!visited.contains(act)) {
				listActivities.add(act);
				visited.add(act);
				selectedEntities.add(act);
				index++;
			}
		}
		return selectedEntities;
	}

	public Question.MultiChoice generateMC() {
		List<ActivityEntity> selectedEntities = generateActivities();
		return new Question.MultiChoice(selectedEntities.stream().map(x -> new Activity(x.getName(),
				x.getImageUrl())).collect(Collectors.toList()), generateAnswer(selectedEntities));
	}

	public Question.EstimationQuestion generateEst() {
		List<ActivityEntity> selectedEntities = generateActivities();
		return new Question.EstimationQuestion(new Activity(selectedEntities.get(1).getName(),
				selectedEntities.get(1).getImageUrl()), selectedEntities.get(1).getEnergyInWh());
	}

	public Question.CalculationQuestion generateCal() {
		///TODO
		return null;
	}

	public int generateAnswer(List<ActivityEntity> listActivities) {
		int answer = 0;

		if (listActivities.get(0).getEnergyInWh() < listActivities.get(1).getEnergyInWh()) {
			if (listActivities.get(1).getEnergyInWh() < listActivities.get(2).getEnergyInWh()) {
				answer = 2;
			} else {
				answer = 1;
			}
		}
		if (listActivities.get(0).getEnergyInWh() <= listActivities.get(2).getEnergyInWh()) {
			answer = 2;
		}
		return answer;
	}


}
