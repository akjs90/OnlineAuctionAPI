package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Item;
import repository.ItemRepository;

@Service
public class ItemService {
	
	@Autowired
	ItemRepository repo;
	
	public Item createItem(Item item){
		if(item.getItemId()==0)
			return repo.save(item);
		return null;
	}

}
