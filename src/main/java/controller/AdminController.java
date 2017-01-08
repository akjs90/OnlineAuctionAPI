package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public String getPeopleList(@RequestParam(name="p",defaultValue="1")Integer page,@RequestParam(name="l",defaultValue="10")Integer size,ModelMap map) {
		map.addAttribute("user_list", userService.findAllUsers(new PageRequest(page-1, size)));
		return "admin/peoples";
	}
}
