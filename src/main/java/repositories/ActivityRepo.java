package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entities.ActivityLog;

@Repository
public interface ActivityRepo extends JpaRepository<ActivityLog, Long>{

	List<ActivityLog> findTop10ByOrderByTimestampDesc();
	
}
