package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entities.Courses;

@Repository
public interface CourseRepo extends JpaRepository<Courses, Long>{
		
	Courses findByName(String name);
}
