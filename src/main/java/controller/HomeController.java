package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import entity.User;
import entity.UserRole;
import entity.UserWrapper;
import repository.UserRoleRepository;
import service.UserService;

@Controller
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class HomeController {
	@Autowired
	UserService service;

	@RequestMapping(value = { "r", "register" }, method = RequestMethod.GET)
	public String register(@ModelAttribute("reg_user") User u) {
		return "register";
	}

	@RequestMapping(value = { "r", "register" }, method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("reg_user") User u) {
		service.registerUser(u);
		return "redirect:/r";
	}

	@ModelAttribute("user_info")
	private UserWrapper createSession() {
		return new UserWrapper();
	}

	@RequestMapping(value = "login")
	public String login(@ModelAttribute("user_info") UserWrapper user) {
		return "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String authentication(@ModelAttribute("user_info") UserWrapper user,
			RedirectAttributes attr, SessionStatus session) {
		User u = service.authorize(user);
		
		if (u == null) {
			attr.addFlashAttribute("login_status", "Invalid username/password.");
			session.setComplete();
			return "redirect:/login";
		}
		System.out.println("User " + u.getEmail());
		user.setRole(u.getRole().getRole());
		return "redirect:/welcome";
	}

	@RequestMapping("welcome")
	public String welcomePage() {
		return "user/userhome";
	}
	@RequestMapping("logout")
	public String logout(SessionStatus session) {
		session.setComplete();
		return "redirect:/login";
	}
}
