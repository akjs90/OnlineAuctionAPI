package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import entity.User;
import entity.UserRole;
import repository.UserRoleRepository;
import service.UserService;

@Controller
public class HomeController {
	@Autowired
	UserService service;
	@RequestMapping(value = {"r","register"},method=RequestMethod.GET)
	public String register(@ModelAttribute("reg_user") User u) {
		return "register";
	}
	
	@RequestMapping(value = {"r","register"},method=RequestMethod.POST)
	public String registerUser(@ModelAttribute("reg_user") User u) {
		service.registerUser(u);
		return "redirect:/r";
	}
	
	
	@RequestMapping(value = "l")
	public String login(){
		UserRole r=service.getUserRoleByName("ROLE_ADMIN");
		System.out.println(r.getUser_role_id());
		return "login";
	}
}
