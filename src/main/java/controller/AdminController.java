package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import entity.User;
import entity.UserWrapper;
import service.UserService;

@Controller
@RequestMapping("admin")
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class AdminController {
	@Autowired
	UserService userService;

	@RequestMapping("people")
	public String getPeopleList(
			@RequestParam(name = "p", defaultValue = "1") Integer page,
			@RequestParam(name = "l", defaultValue = "10") Integer size,
			ModelMap map) {
		map.addAttribute("user_list",
				userService.findAllUsers(new PageRequest(page - 1, size)));
		return "admin/peoples";
	}

	@RequestMapping(value = "changestatus/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> toggleStatus(
			@PathVariable(required = true, value = "id") int id) {
		if (userService.toggleStatus(id))
			return new ResponseEntity<String>(HttpStatus.OK);
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@RequestMapping(value = "search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<String> getData(
			@RequestParam(value = "c", required = false, defaultValue = "username") String criteria,
			@RequestParam("t") String term) {
		List<String> data = null;
		if (criteria.equalsIgnoreCase("name")) {
			data = userService.getByNameData(term);

		} else if (criteria.equalsIgnoreCase("email")) {
			data = userService.getByEmailData(term);
		} else {
			data = userService.getByUserNameData(term);
		}
		return data;
	}

	@RequestMapping(value = "people", params = { "q", "search_param" })
	public String getPeopleByFilter(ModelMap map,
			@RequestParam("q") String query,
			@RequestParam("search_param") String criteria,
			@RequestParam(name = "p", defaultValue = "1") Integer page,
			@RequestParam(name = "l", defaultValue = "10") Integer size) {

		map.addAttribute("user_list", userService.findPeopleUsingFilter(
				criteria, query, new PageRequest(page - 1, size)));
		return "admin/peoples";
	}
	
	@RequestMapping(value="info",params={"t"},produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object[]> getUserInfoByUsername(@RequestParam("t")String username){
		Object[] data=userService.getUserInfoByUsername(username);
		if(null==data)
			return new ResponseEntity<Object[]>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		return new ResponseEntity<Object[]>(data, HttpStatus.OK);
	}
	@RequestMapping("requests")
	public String getItemRequestForAuction(){
		return "admin/request";
	}
}
