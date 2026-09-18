package services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.ActivityLog;
import repositories.ActivityRepo;

@Service
public class ActivityService {

	@Autowired
	ActivityRepo activityRepo;
	
	public void saveActivity(String username , String activity) {
		ActivityLog log = new ActivityLog();
		log.setUsername(username);
		log.setActivity(activity);
		log.setTimestamp(LocalDateTime.now());
		
		activityRepo.save(log);
	}
}
