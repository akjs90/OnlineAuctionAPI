package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import entity.Bid;
import entity.Item;
import entity.UserWrapper;
import service.BidService;
import service.ItemService;

@Controller
@RequestMapping("/bid")
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class BidController {
	
	@Autowired
	BidService service;
	
	@Autowired
	ItemService itmService;
	
	@ModelAttribute("user_info")
	private UserWrapper getAuth(SecurityContextHolder sec) {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserWrapper uw = new UserWrapper();
		if (obj instanceof org.springframework.security.core.userdetails.User) {
			org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) (obj);

			Object[] authorities = u.getAuthorities().toArray();
			uw.setRole(authorities[0].toString());
			uw.setUsername(u.getUsername());
			System.out.println("home controller " + uw.getUsername() + " --- " + uw.getRole());
		}

		return uw;
	}
	
	@RequestMapping(value="/{id}")
	public String bidItem(@PathVariable("id") int id,ModelMap map){
		Item itm = itmService.getItem(id);
		List<Bid> bids = service.findTopTenBids(itm.getItemId());
		map.addAttribute("item", itm);
		map.addAttribute("bids", bids);
		return "bid/home";
	}
	
}
