package commons.servermessage;
import commons.model.Activity;

import java.util.List;

public record AdminMessage(List<Activity> activityList) {
}
