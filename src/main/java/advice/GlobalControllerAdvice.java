package advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import entities.User;
import repositories.UserRepo;

@ControllerAdvice
public class GlobalControllerAdvice {

	   @Autowired
	    private UserRepo userRepo;

	    @ModelAttribute("loggedUser")
	    public User loggedUser(Authentication authentication) {

	        if(authentication == null) {
	            return null;
	        }

	        return userRepo.findByEmail(authentication.getName());
	    }
}
