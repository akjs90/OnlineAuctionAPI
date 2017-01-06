package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("admin")
public class AdminController {
@RequestMapping("people")
	public String getPeopleList(){
	return "admin/peoples";
}
}
