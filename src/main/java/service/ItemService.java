package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Item;
import entity.ItemPicture;
import repository.ItemPictureRepository;
import repository.ItemRepository;

@Service
public class ItemService {
	
	@Autowired
	ItemRepository repo;
	
	@Autowired
	ItemPictureRepository picRepo;
	
	public Item createItem(Item item){
		if(item.getItemId()==0)
			return repo.save(item);
		return null;
	}
	
	public ItemPicture addPicture(ItemPicture ip){
		if(ip.getId()==0)
			return picRepo.save(ip);
		return null;
	}

}
