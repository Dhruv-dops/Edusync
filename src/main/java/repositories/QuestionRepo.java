package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entities.Questions;

public interface QuestionRepo extends JpaRepository<Questions,Long>{
	
	List<Questions> findByCourseName(String courseName);

}
