package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import entity.Item;
import entity.ItemPicture;
import entity.User;
import entity.UserWrapper;
import service.ItemService;
import service.UserService;

@Controller
@RequestMapping("/item")
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class ItemController {
	
	@Autowired
	ItemService service;
	@Autowired
	UserService usrService;
	
	private final Path rootLoc = Paths.get("uploadedImages");
	
	@RequestMapping("/")
	public String itemHome(HttpSession session){
		
		if(session.getAttribute("items")!= null)
			session.removeAttribute("items");
			
		return "item/itemHome";
	}
	
	@RequestMapping("/{verify}")
	public String getItemsByVerification(@PathVariable("verify") char verify, HttpSession session){
		UserWrapper userWrapper = (UserWrapper) session.getAttribute("user_info");
		User user = usrService.findUserByUsername(userWrapper.getUsername());
		ArrayList<Item> itemsList = service.getItemByUserAndVerified(user, verify);
		for (Item item : itemsList) {
			System.out.println(item.getName());
		}
		session.setAttribute("items", itemsList);
		return "item/itemHome";
	}
	
	@RequestMapping("/add")
	public String showForm(@ModelAttribute("item") Item item){
		return "item/addItem";
	}

	@RequestMapping(value="/add",method =RequestMethod.POST)
	public String createItem(Item item,HttpSession session){
		System.out.println("Item received"+item.getName());
		session.setAttribute("item", item);
		return "item/itemPicture";
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> saveItemAndPictures(@RequestParam("files")MultipartFile[] files,HttpSession session){
			
		Item item = (Item) session.getAttribute("item");
		item.setRegistrationDate(new Date());
		
		UserWrapper uw = (UserWrapper) session.getAttribute("user_info");
		User user = usrService.findUserByUsername(uw.getUsername());
		
		Item savedItem = service.createItem(item, user);

		
		if(savedItem == null )
			return(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
		
		for(int i= 0;i<files.length;i++){
			try{
				Path itemLocal = Files.createDirectories(rootLoc.resolve("item"+savedItem.getItemId()));
				Files.copy(files[i].getInputStream(),itemLocal.resolve("item"+savedItem.getItemId()+"_"+(i+1)));
				ItemPicture itmPic = new ItemPicture();
				itmPic.setItem(savedItem);
				String picUrl=savedItem.getItemId()+File.separator+(i+1);
				itmPic.setPictureUrl(picUrl);
				service.addPicture(itmPic);
			}
			catch(IllegalStateException | IOException e){
				e.printStackTrace();
				return(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
			}
		}	
		
	return(new ResponseEntity<>(HttpStatus.OK));
	} 
	
}
