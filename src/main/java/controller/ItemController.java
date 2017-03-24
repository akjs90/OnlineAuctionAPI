package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@RequestMapping("/")
	public String itemHome(HttpSession session) {

		if (session.getAttribute("items") != null)
			session.removeAttribute("items");

		return "item/itemHome";
	}

	@RequestMapping("/{verify}")
	public String getItemsByVerification(@PathVariable("verify") char verify, HttpSession session) {
		UserWrapper userWrapper = (UserWrapper) session.getAttribute("user_info");
		User user = usrService.findUserByUsername(userWrapper.getUsername());
		ArrayList<Item> itemsList = service.getItemByUserAndVerified(user, verify);
		session.setAttribute("items", itemsList);
		if(verify =='X')
			session.setAttribute("X", "X");
		else
			session.setAttribute("X", "notX");
		return "item/itemHome";
	}
	
	@RequestMapping("/bid/{id}")
	@ResponseBody
	public String getItemBidders(@PathVariable("id") int id){
		Item item = service.getItem(id);
		
		return service.getOngoingBids(item.getAuction());
	}

	@RequestMapping("/add")
	public String showForm(@ModelAttribute("item") Item item) {
		return "item/addItem";
	}
	

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String createItem(Item item, HttpSession session) {
		System.out.println("Item received" + item.getName());
		if (item.getItemId() != 0) {
			System.out.println("Item received id " + item.getItemId());
			Item rcvdItem = service.getItem(item.getItemId());
			item.setItemPictures(rcvdItem.getItemPictures());

		}

		session.setAttribute("item", item);
		return "item/itemPicture";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> saveItemAndPictures(
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam(value = "removedImages", required = false) int[] removedImages, HttpSession session) {

		Item item = (Item) session.getAttribute("item");
		if (item.getItemId() == 0) {// for new Item
			item.setRegistrationDate(new Date());

			UserWrapper uw = (UserWrapper) session.getAttribute("user_info");
			User user = usrService.findUserByUsername(uw.getUsername());

			Item savedItem = service.createItem(item, user);

			if (savedItem == null)
				return (new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

			for (int i = 0; i < files.length; i++) {
				try {
					Path itemLocal = Files.createDirectories(rootLoc.resolve("item" + savedItem.getItemId()));
					Files.copy(files[i].getInputStream(),
							itemLocal.resolve("item" + savedItem.getItemId() + "_" + (i + 1)));
					ItemPicture itmPic = new ItemPicture();
					itmPic.setItem(savedItem);
					String picUrl = savedItem.getItemId() + File.separator + (i + 1);
					itmPic.setPictureUrl(picUrl);
					service.addPicture(itmPic);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
					return (new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
				}
			}
		} else {// modify item

			if (removedImages != null) {
				try {
					for (int i = 0; i < removedImages.length; i++) {
						String fileName = "item" + item.getItemId() + "_" + removedImages[i];
						File serveFile = new File(rootLoc.toString() + File.separator + "item" + item.getItemId()
								+ File.separator + fileName);
						System.out.println(serveFile.getAbsolutePath());
						if (serveFile.exists() && serveFile.isFile()) {
							if (serveFile.delete())
								service.deleteImage(removedImages[i]);
						} else {
							System.out.println("from else of removed images..............");
							return (new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
						}

					}

				} catch (Exception e) {
					System.out.println("from catch of removed images..............");
					e.printStackTrace();
					return (new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
				}
			}
			if (files != null) {
				try {
					File serveFile = new File(rootLoc.toString() + File.separator + "item" + item.getItemId());
					if (serveFile.exists()) {
						File[] filesInIt = serveFile.listFiles();
						String fileName = filesInIt[0].getName();
						System.out.println(fileName);
						int len = fileName.length();
						int counter = Integer.parseInt(String.valueOf(fileName.charAt(len - 1))) + 1;
						for (int i = 0; i < files.length; i++) {
							Files.copy(files[i].getInputStream(),
									serveFile.toPath().resolve("item" + item.getItemId() + "_" + (counter)));
							ItemPicture itmPic = new ItemPicture();
							itmPic.setItem(item);
							String picUrl = item.getItemId() + File.separator + (counter++);
							itmPic.setPictureUrl(picUrl);
							service.addPicture(itmPic);
						}
					}
				} catch (Exception e) {
					System.out.println("from catch of new images..............");
					e.printStackTrace();
					return (new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
				}
			}
			Item modifiedItem = service.modifyItem(item);
		}

		return (new ResponseEntity<>(HttpStatus.OK));
	}

	@RequestMapping(value = "/modify/{id}")
	public String modifyItem(@PathVariable("id") int id, @ModelAttribute("item") Item item, Model model,
			HttpSession session) {
		item = service.getItem(id);
		System.out.println("Item is " + item.getName() + " with item id" + item.getItemId());
		model.addAttribute("item", item);
		return "item/addItem";
	}

}
