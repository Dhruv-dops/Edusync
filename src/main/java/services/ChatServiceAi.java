package services;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import org.springframework.stereotype.Service;

@Service
public class ChatServiceAi {
	
	private final Client client;

	public ChatServiceAi(Client client) {
		this.client = client;
	}
	
	
	public String askQuestion(String question) {

	    String prompt = """
	            %s

	            Student Question:
	            %s

	            Answer:
	            """.formatted(EDUSYNC_KNOWLEDGE, question);

	    int maxAttempts = 4;

	    for (int attempt = 1; attempt <= maxAttempts; attempt++) {

	        try {

	            GenerateContentResponse response =
	                    client.models.generateContent(
	                            "gemini-3.8-flash",
	                            prompt,
	                            null
	                    );

	            return response.text();

	        } catch (Exception e) {

	            System.out.println("Gemini attempt " + attempt + " failed:");
	            System.out.println(e.getMessage());

	            if (attempt == maxAttempts) {
	                return "Sorry, EduSync AI is temporarily unavailable. Please try again.";
	            }

	            try {
	                // 2 sec, 4 sec, 8 sec
	                long waitTime = (long) Math.pow(2, attempt) * 1000;

	                System.out.println("Retrying in " + waitTime + " ms...");

	                Thread.sleep(waitTime);

	            } catch (InterruptedException ex) {
	                Thread.currentThread().interrupt();
	                return "Sorry, please try again.";
	            }
	        }
	    }

	    return "Sorry, please try again.";
	}
	
	
	private static final String EDUSYNC_KNOWLEDGE = """
			You are EduSync AI Assistant.

			About EduSync:
			EduSync is an online learning platform offering industry-oriented courses
			with certificates, affordable pricing, and placement support.

			Available Courses:

			1. Java Full Stack
			   Fee: ₹4999
			   Duration: 6 Months
			   Certificate: Yes

			2. Python Full Stack
			   Fee: ₹3999
			   Duration: 5 Months
			   Certificate: Yes

			3. MERN Stack
			   Fee: ₹5999
			   Duration: 6 Months
			   Certificate: Yes

			Course Features:
			- Lifetime access after purchase.
			- HD video lectures.
			- Downloadable notes.
			- Hands-on projects.
			- Regular quizzes.
			- Mentor support.
			- Course progress tracking.

			Certificates:
			- Certificate issued after successful course completion.
			- Student must complete all modules.
			- Student must pass the final assessment.
			- Certificate can be downloaded as PDF.

			Enrollment:
			- Students can enroll after successful payment.
			- Purchased courses appear on the Enrollment page.
			- Students can access courses immediately after enrollment.

			Placement Support:
			- Resume Building
			- Mock Interviews
			- Job Assistance
			- Career Guidance
			- LinkedIn Profile Review

			Refund Policy:
			- Refund available within 7 days.
			- Refund not available after 7 days.
			- Refund request must be submitted through support.

			Support:
			Email: support@edusync.com
			Working Hours:
			Monday to Saturday
			9:00 AM - 6:00 PM

			Rules:
			- Answer ONLY using the above information.
			- If the answer is not present, politely reply:
			"Sorry, I don't have information about that."
			""";
}
