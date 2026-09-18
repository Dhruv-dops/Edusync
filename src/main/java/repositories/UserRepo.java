package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import entities.User;
import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User, Long>{
	
//	@Query("SELECT u FROM User u WHERE u.email = :email")
	User findByEmail(String email);
	boolean existsByEmail(String email);
	
	List<User> findByRole(String role);
}
