package controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import services.ChatServiceAi;

@Controller
public class AiController {


	    private final ChatServiceAi chatServiceAi;

	    public AiController(ChatServiceAi chatServiceAi) {
	        this.chatServiceAi = chatServiceAi;
	    }

	    @GetMapping("/eduAi")
	    public String aiPage() {
	        return "aiChat";
	    }

	    @PostMapping("/ask-ai")
	    @ResponseBody
	    public String askQuestion(@RequestParam("question") String question) {

	    	 return chatServiceAi.askQuestion(question);
	    }

}
