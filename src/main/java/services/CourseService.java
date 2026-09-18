package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Courses;
import repositories.CourseRepo;

@Service
public class CourseService {
	
	@Autowired
	CourseRepo courseRepo;
	
	public List<Courses> getAllCourses(){
		return courseRepo.findAll();
	}

}
