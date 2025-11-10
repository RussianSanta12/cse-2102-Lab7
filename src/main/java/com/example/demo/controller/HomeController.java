package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import com.example.demo.model.UserQuiz;
import com.example.demo.model.MyString;
import com.example.demo.model.questions.ArrayQuestionsTrueFalse;

@Controller
public class HomeController {

	private final ArrayQuestionsTrueFalse questionBank = new ArrayQuestionsTrueFalse();

	// @GetMapping("/")
	// public String home() {
	// 	// Redirect root to the quiz start page so the default page shows the first question
	// 	return "redirect:/get_question";
	// }

	@GetMapping("/")
	public String questionForm(Model model, HttpSession session) {
		// Retrieve or create per-user quiz state stored in the HTTP session
		UserQuiz quiz = (UserQuiz) session.getAttribute("quiz");
		if (quiz == null) {
			quiz = new UserQuiz();
			session.setAttribute("quiz", quiz);
		}

		int idx = quiz.getCurrentIndex();
		String question = questionBank.nextQuestion(idx).getQuestion();

		model.addAttribute("questionText", question);
		model.addAttribute("currentIndex", idx);
		model.addAttribute("score", quiz.getScore());
		model.addAttribute("total", questionBank.getTotalQuestions());

		return "question";
	}

	@PostMapping("/answer")
	public String submitAnswer(@RequestParam("answer") boolean answer, Model model, HttpSession session, jakarta.servlet.http.HttpServletResponse response) {
		UserQuiz quiz = (UserQuiz) session.getAttribute("quiz");
		if (quiz == null) {
			// No session - start a new quiz
			quiz = new UserQuiz();
			session.setAttribute("quiz", quiz);
		}

		int idx = quiz.getCurrentIndex();
		boolean correct = questionBank.nextQuestion(idx).getAnswer();
		if (correct == answer) {
			quiz.incrementScore();
		}

		quiz.incrementIndex();

		// If finished, show result page
		if (quiz.getCurrentIndex() >= questionBank.getTotalQuestions()) {
			model.addAttribute("score", quiz.getScore());
			model.addAttribute("total", questionBank.getTotalQuestions());
			// remove session quiz to allow restart
			session.removeAttribute("quiz");
			return "result";
		}

		// otherwise redirect to next question using 303 See Other to force the client to follow with GET
	response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_SEE_OTHER);
		response.setHeader("Location", "/");
		return null;
	}

}
