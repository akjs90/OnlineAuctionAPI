package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import entity.Item;
import service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	ItemService service;
	
	private final Path rootLoc = Paths.get("uploadedImages");
	
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
	
	@RequestMapping(value="/saveItem",method=RequestMethod.POST)
	public @ResponseBody String saveItemAndPictures(@RequestParam("files")MultipartFile[] files,HttpSession session){
		System.out.println("Entering--------------- ");
		System.out.println(files.length);
		for(MultipartFile file:files){
			System.out.println(file.getOriginalFilename());	
		}
		
		
		/*Iterator<String> itr =  request.getFileNames();
		if(itr.hasNext()){
		 MultipartFile mpf = request.getFile(itr.next());
	     System.out.println(mpf.getOriginalFilename() +" uploaded!");}
		else
			System.out.println("itr null");
		*/
		/*Item item = (Item) session.getAttribute("item");
		item.setRegistrationDate(new Date());
		Item savedItem = service.createItem(item);
		for(int i= 0;i<files.length;i++){
			try{
				Files.copy(files[i].getInputStream(),this.rootLoc.resolve(item.getItemId()+"/"+files[i].getOriginalFilename()));
			}
			catch(IllegalStateException | IOException e){
				e.printStackTrace();
			}
		}*/
		
		return "success";
	} 
}
