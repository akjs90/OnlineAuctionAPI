package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@RequestMapping(value = "r")
	public String register() {
		System.out.println("Here i am ");
		return "register";
	}
	@RequestMapping(value = "l")
	public String login(){
		return "login";
	}
}
