package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import entity.Item;
import service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	ItemService service;
	
	@RequestMapping("/addItem")
	public String showForm(@ModelAttribute("item") Item item){
		return "item/addItem";
	}

	@RequestMapping(value="/addItem",method =RequestMethod.POST)
	public String createItem(Item item,HttpSession session){
		System.out.println("Item received"+item.getName());
		session.setAttribute("item", item);
		return "item/itemPicture";
	}
}
