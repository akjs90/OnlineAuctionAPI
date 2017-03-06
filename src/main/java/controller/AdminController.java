package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import entity.OngoingAuction;
import entity.UserWrapper;
import service.AuctionService;
import service.UserService;

@Controller
@RequestMapping("admin")
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class AdminController {
	@Autowired
	private UserService userService;

	@Autowired
	private AuctionService auctionServ;
	
	private SseEmitter adminEmitter=new SseEmitter(-1L);
	
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

		@RequestMapping(value="verify",method=RequestMethod.PUT, params="id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> rejectAuction(@RequestParam(required=true,name="id" )int auction_id) {
		if (auctionServ.rejectAuction(auction_id))
			return new ResponseEntity<>(HttpStatus.OK);//send mail of rejection to user.
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value="verify",method=RequestMethod.POST, params={"start-time","end-time","verify_auc_id"},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> verifyAuctionRequest(@RequestParam("start-time") String start,@RequestParam("end-time") String end,int verify_auc_id) throws ParseException{
		if(auctionServ.verifyAuctionRequest(start, end, verify_auc_id))
			return new ResponseEntity<>(HttpStatus.OK);		//send mail of approval to user.
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@RequestMapping("/admin-events")
	public SseEmitter adminEventsBroadcast(){
		return adminEmitter;
	}
	
}
