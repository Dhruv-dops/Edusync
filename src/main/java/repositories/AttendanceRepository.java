package repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import entities.Attendance;
import entities.Courses;
import entities.User;

@Repository
public interface AttendanceRepository
        extends JpaRepository<Attendance, Long>{

    List<Attendance> findByUser(User user);

    List<Attendance> findByCourse(Courses course);

    List<Attendance> findByUserAndCourse(User user,
                                         Courses course);
    
    
}