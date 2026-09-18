package controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import entities.Attendance;
import entities.Certificate;
import entities.Courses;
import entities.Orders;
import entities.Questions;
import entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import repositories.ActivityRepo;
import repositories.AttendanceRepository;
import repositories.CertificateRepo;
import repositories.CourseRepo;
import repositories.OrdersRepo;
import repositories.QuestionRepo;
import repositories.UserRepo;
import services.ActivityService;
import services.CourseService;
import services.OrdersService;
import services.UserService;

@Controller
//@SessionAttributes("SessionUser")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuestionRepo questionRepo;
	
	@Autowired
	AttendanceRepository attendanceRepo;
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	ActivityRepo activityRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	CourseRepo courseRepo;
	
	@Autowired
	OrdersRepo ordersRepo;
	
	@Autowired
	OrdersService ordersService;
	
	@Autowired
	CertificateRepo certificateRepo;
	
	@GetMapping({"/","/index"})
	public String openIndexPage(Model model) {
	 List<Courses> courses = courseService.getAllCourses();
	 model.addAttribute("courses",courses);
	 
	 if(!model.containsAttribute("SessionUser")) {
		 model.addAttribute("SessionUser",null);
	 }
		
		return "index";
	}
	
	@GetMapping("/login")
	public String openLoginPage(){
		return "login";
	}
	
	@GetMapping("/register")
	public String openRegistrationPage(Model model){
		model.addAttribute("user",new User());
		return "registration";
	}
	
	@GetMapping("/admin/dashboard")
	public String openAdminPanel(Model model) {
		
		model.addAttribute(
		        "recentActivities",
		        activityRepo.findTop10ByOrderByTimestampDesc());
		
		return "admin";
	}
	
	@GetMapping("/admin/courses")
	public String manageCourses(Model model) {
		model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("newCourse", new Courses());
		return "courses";
	}
	
	@GetMapping("/admin/questions")
	public String questionsPage(Model model){

	    model.addAttribute("question",
	            new Questions());

	    model.addAttribute("courses",
	            courseRepo.findAll());

	    model.addAttribute("questions",
	            questionRepo.findAll());

	    return "questions";
	}
	
	@GetMapping("/profile")
	public String openProfile() {
		return "userProfille";
	}
	
	@GetMapping("/mycourses")
	public String openMyCourse() {
		return "login";
	}
	
	@GetMapping("/enrollment")
	public String enrollment(
	        Authentication authentication,
	        Model model){

	    String email =
	            authentication.getName();

	    List<Orders> myCourses =
	            ordersService.getUserCourses(email);

	    model.addAttribute("myCourses", myCourses);

	    return "enrollment";
	}
	
	@GetMapping("/certificate-test/{courseName}")
	public String showCertificateTest(
	        @PathVariable String courseName,
	        Model model) {

	    List<Questions> questions =
	            questionRepo.findByCourseName(courseName);

	    model.addAttribute("questions", questions);
	    model.addAttribute("courseName", courseName);

	    return "certificateTest";
	}
	
	@GetMapping("/certificate/{id}")
	public String certificate(
	        @PathVariable Long id,
	        Model model){

	    Certificate cert =
	            certificateRepo.findById(id)
	            .orElseThrow();

	    model.addAttribute("cert", cert);

	    return "certificate";
	}
	
	@GetMapping("/admin/students")
	public String manageStudents(Model model) {

	    List<User> students =
	            userRepo.findByRole("ROLE_STUDENT");

	    model.addAttribute("students", students);

	    return "manageStudents";
	}
	
	@GetMapping("/admin/student/{id}")
	public String studentDetails(
	        @PathVariable Long id,
	        Model model) {

	    User user =
	            userRepo.findById(id)
	                    .orElseThrow();

	    List<Orders> courses =
	            ordersRepo.findByUserEmail(
	                    user.getEmail());

	    model.addAttribute("student", user);
	    model.addAttribute("courses", courses);

	    return "studentDetails";
	}
	
	@GetMapping("/admin/deleteStudent/{id}")
	public String deleteStudent(
	        @PathVariable Long id) {

	    User user =
	            userRepo.findById(id)
	                    .orElseThrow();

	    ordersRepo.deleteByUserEmail(
	            user.getEmail());

	    userRepo.delete(user);

	    return "redirect:/admin/students";
	}
	
	@GetMapping("/admin/attendance")
	public String attendancePage(Model model){	

		    model.addAttribute("courses",
		            courseRepo.findAll());

	    return "attendence";
	}
	
	@GetMapping("/admin/loadAttendance")
	public String loadAttendance(
	        @RequestParam String courseName,
	        Model model){

	    List<Orders> enrolledStudents =
	            ordersRepo.findByCourseName(courseName);

	    model.addAttribute("courses",
	            courseRepo.findAll());

	    model.addAttribute("enrolledStudents",
	            enrolledStudents);

	    model.addAttribute("selectedCourse",
	            courseName);

	    return "attendence";
	}
	
	@GetMapping("/attendance")
	public String studentAttendance(
	        Authentication authentication,
	        Model model){

	    User user =
	            userRepo.findByEmail(
	                    authentication.getName());

	    List<Attendance> attendanceList =
	            attendanceRepo.findByUser(user);

	    long presentCount =
	            attendanceList.stream()
	                    .filter(a ->
	                            a.getStatus()
	                             .equals("PRESENT"))
	                    .count();

	    long totalCount =
	            attendanceList.size();

	    double percentage = 0;

	    if(totalCount > 0){

	        percentage =
	                (presentCount * 100.0)
	                / totalCount;
	    }

	    model.addAttribute(
	            "attendanceList",
	            attendanceList);

	    model.addAttribute(
	            "presentCount",
	            presentCount);

	    model.addAttribute(
	            "totalCount",
	            totalCount);

	    model.addAttribute(
	            "percentage",
	            percentage);

	    return "studentAttendence";
	}
	
	@PostMapping("/admin/saveAttendance")
	public String saveAttendance(

	        @RequestParam String courseName,

	        @RequestParam String attendanceDate,

	        @RequestParam List<String> emails,

	        @RequestParam List<String> statuses){

	    Courses course =
	            courseRepo.findByName(courseName);

	    LocalDate date =
	            LocalDate.parse(attendanceDate);

	    for(int i=0;i<emails.size();i++){

	        User student =
	                userRepo.findByEmail(emails.get(i));

	        Attendance attendance =
	                new Attendance();

	        attendance.setUser(student);
	        attendance.setCourse(course);
	        attendance.setDate(date);
	        attendance.setStatus(statuses.get(i));

	        attendanceRepo.save(attendance);
	    }

	    return "redirect:/admin/attendance";
	}
	
	@PostMapping("/submit-test")
	public String submitTest(
	        @RequestParam String courseName,
	        HttpServletRequest request,
	        Authentication authentication,
	        Model model) {

	    User user =
	            userRepo.findByEmail(
	                    authentication.getName());

	    List<Questions> questions =
	            questionRepo.findByCourseName(courseName);

	    int score = 0;

	    for(Questions q : questions){

	        String userAnswer =
	                request.getParameter(
	                        "q_" + q.getId());

	        if(userAnswer != null &&
	           userAnswer.equalsIgnoreCase(
	                   q.getCorrectAnswer())) {

	            score++;
	        }
	    }

	    Certificate cert = new Certificate();

	    cert.setCertificateNo(
	            "EDU-" + System.currentTimeMillis());

	    cert.setUserEmail(user.getEmail());

	    cert.setUserName(user.getName());

	    cert.setCourseName(courseName);

	    cert.setScore(score);

	    cert.setIssueDate(
	            LocalDate.now().toString());

	    certificateRepo.save(cert);

	    return "redirect:/certificate/" + cert.getId();
	}
	
	@PostMapping("/admin/courses/add")
	public String addCourse(@ModelAttribute Courses course,
            @ModelAttribute("SessionUser") User admin) {
		courseRepo.save(course);
		
		activityService.saveActivity(
	            admin.getName(),
	            "Added Course : " + course.getName());
		
		return "redirect:/admin/courses";
		
	}
	
	@PostMapping("/admin/courses/delete/{id}")
	public String deleteCourse(@PathVariable Long id ,  @ModelAttribute("SessionUser") User admin) {
		
		Courses course = courseRepo.findById(id).orElse(null);
		
		    activityService.saveActivity(
		            admin.getName(),
		            "Deleted Course : " + course.getName());

		    courseRepo.deleteById(id);
		return "redirect:/admin/courses";
	}
	
	@PostMapping("/regForm")
	public String handleRegistration(
	        @Valid @ModelAttribute("user") User user,
	        BindingResult result,
	        Model model) {

	    if (result.hasErrors()) {
	        return "registration";
	    }

	    try {

	        userService.registerUser(user);

	        model.addAttribute(
	                "successMessage",
	                "Successfully Registered"
	        );

	        model.addAttribute("user", new User());

	        return "registration";

	    } catch (RuntimeException e) {

	        model.addAttribute(
	                "failedMessage",
	                "This email is already registered."
	        );

	        model.addAttribute("user", new User());

	        return "registration";
	    }
	}
//	@PostMapping("/loginForm")
//	public String handleLogin(@ModelAttribute("user") User user , Model model) {
//		Boolean isAuth = userService.findUser(user.getEmail(), user.getPassword());
//		if(isAuth) {
//			User authenticatedUser = userRepo.findByEmail(user.getEmail());
//			model.addAttribute("SessionUser",authenticatedUser);
//			return "redirect:/index";
//		}else {
//			model.addAttribute("Failed","Invalid Credentials");
//			return "login";
//		}
//	}
	
	@PostMapping("/admin/saveQuestion")
	public String saveQuestion(@ModelAttribute Questions question) {

	    questionRepo.save(question);

	    return "redirect:/admin/questions";
	}
	
	@PostMapping("/logout")
	public String handleLogout(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "login";
	}
}
