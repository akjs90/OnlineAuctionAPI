package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import entity.User;
import entity.UserWrapper;
import service.UserService;

@Controller
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class HomeController {
	@Autowired
	UserService service;

	private final Path rootLoc = Paths.get("uploadedImages");

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
	private UserWrapper getAuth(SecurityContextHolder sec) {
		Object obj=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserWrapper uw=new UserWrapper();
		if(obj instanceof org.springframework.security.core.userdetails.User){
			org.springframework.security.core.userdetails.User u=(org.springframework.security.core.userdetails.User)(obj);
			
			Object[] authorities=  u.getAuthorities().toArray();
			uw.setRole(authorities[0].toString());
			uw.setUsername(u.getUsername());
			System.out.println("home controller "+uw.getUsername()+" --- "+uw.getRole());
		}
		
		return uw;
	}

	@RequestMapping(value = "login")
	public String login(@ModelAttribute("user_info") UserWrapper user) {
		System.out.println("here i am in login");
		if (user.getUsername() != null)
			return "redirect:/welcome";
		return "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String authentication(@ModelAttribute("user_info") UserWrapper user, RedirectAttributes attr,
			SessionStatus session) {
		User u = service.authorize(user);

		if (u == null) {
			attr.addFlashAttribute("login_status", "Invalid username/password.");
			session.setComplete();
			return "redirect:/login";
		}
		System.out.println("User " + u.getEmail());
		user.setRole(u.getRole().getRole());
		System.out.println("Hello");
		return "redirect:/welcome";
	}

	@RequestMapping("welcome")
	public String welcomePage() {
		System.out.println("Welcome fro");
		return "user/userhome";
	}

	@RequestMapping("logout")
	public String logout(SessionStatus session) {
		session.setComplete();
		System.out.println("Logout sessin");
		return "redirect:/login";
	}

	@RequestMapping("403")
	public String acessDeny(SessionStatus session) {
		return "error/403";
	}

	@RequestMapping("image/{path}/{imagename}")
	public @ResponseBody byte[] imageReader(@PathVariable("path") String path, @PathVariable("imagename") String name) throws IOException {
		File serveFile = new File(rootLoc.toString()+File.separator+"item"+path);
		Path p=Paths.get("uploadedImages", "item"+path,"item"+path+"_"+name);
		if (!serveFile.exists())
			return null;
		else {
			File[] listFiles = serveFile.listFiles();
			for (File file : listFiles) {
				System.out.println(file.getName());
			}
			
		}
		return Files.readAllBytes(p);
	}
}
