package controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
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

import entity.UserWrapper;
import service.AuctionService;
import service.UserService;

@Controller
@RequestMapping("admin")
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class AdminController {
	@Autowired
	UserService userService;

	@Autowired
	AuctionService auctionServ;

	@RequestMapping("people")
	public String getPeopleList(@RequestParam(name = "p", defaultValue = "1") Integer page,
			@RequestParam(name = "l", defaultValue = "10") Integer size, ModelMap map) {
		map.addAttribute("user_list", userService.findAllUsers(new PageRequest(page - 1, size)));
		return "admin/peoples";
	}

	@RequestMapping(value = "changestatus/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> toggleStatus(@PathVariable(required = true, value = "id") int id) {
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
	public String getPeopleByFilter(ModelMap map, @RequestParam("q") String query,
			@RequestParam("search_param") String criteria, @RequestParam(name = "p", defaultValue = "1") Integer page,
			@RequestParam(name = "l", defaultValue = "10") Integer size) {

		map.addAttribute("user_list",
				userService.findPeopleUsingFilter(criteria, query, new PageRequest(page - 1, size)));
		return "admin/peoples";
	}

	@RequestMapping(value = "info", params = { "t" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object[]> getUserInfoByUsername(@RequestParam("t") String username) {
		Object[] data = userService.getUserInfoByUsername(username);
		if (null == data)
			return new ResponseEntity<Object[]>(HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity<Object[]>(data, HttpStatus.OK);
	}

	@RequestMapping("requests")
	public String getItemRequestForAuction(ModelMap map) {
		map.addAttribute("auction_request", auctionServ.getRecentAuctionRequest());
		return "admin/request";
	}

	@RequestMapping("completed")
	public String getCompletedAuction(ModelMap map) {
		return "admin/completed";
	}

	@RequestMapping("ongoing")
	@ResponseBody
	public String getOngoingAuction(ModelMap map) {
		return "admin/ongoing";
	}

	@RequestMapping(value="verify",method=RequestMethod.PUT, params="id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> rejectAuction(@RequestParam(required=true,name="id" )int auction_id) {
		if (auctionServ.rejectAuction(auction_id))
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value="verify",method=RequestMethod.POST, params={"start-time","end-time","verify_auc_id"},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> verifyAuctionRequest(@RequestParam("start-time") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Calendar start,@RequestParam("end-time") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)LocalDateTime end,int verify_auc_id){
		System.out.println(start);
		/*Date t;
		System.out.println("Date "+t);*/
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
