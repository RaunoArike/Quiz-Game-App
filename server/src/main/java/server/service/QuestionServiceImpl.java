package server.service;

import commons.model.Activity;
import commons.model.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import server.entity.ActivityEntity;
import server.repository.ActivityRepository;

public class QuestionServiceImpl implements QuestionService {
	private static final List<ActivityEntity> visited = new ArrayList<>();
	private final ActivityRepository activityRepository;

	public QuestionServiceImpl(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	public ActivityRepository getActivityRepository() {
		return activityRepository;
	}

	public Question generateQuestion(int gameId) {

		int no = Math.abs(new Random().nextInt());

		switch (no % 3){
			case 0: {
				return generateMC();
			}
			case 1: {
				return generateEst();
			}
			case 2: {
				return generateCal();
			}
		}
		return null;
	}
	public List<ActivityEntity> GenerateActivities(){
		Collections.shuffle(activityRepository.findAll());
		List<ActivityEntity> ListActivities = activityRepository.findAll();
		List<ActivityEntity> selectedEntities = new ArrayList<>();

		int index = 0;

		while(index < 3){
			int no = Math.abs(new Random().nextInt());
			ActivityEntity act = ListActivities.get(no % ListActivities.size());
			Activity activityFromEnt = new Activity(act.getName(),act.getImageUrl());
			if(!visited.contains(act)){
				ListActivities.add(act);
				visited.add(act);
				selectedEntities.add(act);
				index++;
			}
		}
		return selectedEntities;
	}

	public Question.MultiChoice generateMC(){
		List<ActivityEntity> selectedEntities = GenerateActivities();
		return new Question.MultiChoice(selectedEntities.stream().
				map(x -> new Activity(x.getName(),x.getImageUrl())).
				collect(Collectors.toList()), GenerateAnswer(selectedEntities));
	}

	public Question.EstimationQuestion generateEst(){
		List<ActivityEntity> selectedEntities = GenerateActivities();
		return new Question.EstimationQuestion(new Activity(selectedEntities.get(1).getName(),
				selectedEntities.get(1).getImageUrl()),
				selectedEntities.get(1).getEnergyInWh());
	}

	public Question.CalculationQuestion generateCal(){
		///TODO
		return null;
	}
	public int GenerateAnswer(List<ActivityEntity> ListActivities){

		int answer = 0;

		if(ListActivities.get(0).getEnergyInWh() < ListActivities.get(1).getEnergyInWh()){
			if(ListActivities.get(1).getEnergyInWh() < ListActivities.get(2).getEnergyInWh()){
				answer = 2;
			}
			else{
				answer = 1;
			}
		}
		if(ListActivities.get(0).getEnergyInWh() <= ListActivities.get(2).getEnergyInWh()){
			answer = 2;
		}
		return answer;
	}



}
