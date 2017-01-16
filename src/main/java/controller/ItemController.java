package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import entity.Item;
import entity.ItemPicture;
import entity.UserWrapper;
import service.ItemService;

@Controller
@RequestMapping("/item")
@SessionAttributes(value = "user_info", types = { UserWrapper.class })
public class ItemController {
	
	@Autowired
	ItemService service;
	
	private final Path rootLoc = Paths.get("uploadedImages");
	
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
		Item savedItem = service.createItem(item);
		if(savedItem == null)
			return(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
		
		for(int i= 0;i<files.length;i++){
			try{
				Path itemLocal = Files.createDirectories(rootLoc.resolve("item"+savedItem.getItemId()));
				Files.copy(files[i].getInputStream(),itemLocal.resolve("item"+savedItem.getItemId()+"_"+(i+1)));
				ItemPicture itmPic = new ItemPicture();
				itmPic.setItem(savedItem);
				String picUrl=itemLocal.toString()+File.separator+"item"+savedItem.getItemId()+"_"+(i+1);
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
	
	@RequestMapping(value="/home")
	public String showHome(){
		return "user/home";
	}
}
