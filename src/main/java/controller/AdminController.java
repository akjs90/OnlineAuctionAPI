package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import entity.UserWrapper;
import service.UserService;

@Controller
@RequestMapping("admin")
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class AdminController {
	@Autowired
	UserService userService;

	@RequestMapping("people")
	public String getPeopleList(Pageable p,ModelMap map) {
		map.addAttribute("user_list", userService.findAllUsers(p));
		return "admin/peoples";
	}
}
