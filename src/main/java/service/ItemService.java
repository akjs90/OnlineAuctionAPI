package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Auction;
import entity.Item;
import entity.ItemPicture;
import entity.User;
import repository.AuctionRepository;
import repository.ItemPictureRepository;
import repository.ItemRepository;

@Service
public class ItemService {
	
	@Autowired
	ItemRepository repo;
	
	@Autowired
	ItemPictureRepository picRepo;
	
	@Autowired
	AuctionRepository auctionRepo;
	
	public Item createItem(Item item, User user){
		if(item.getItemId()==0 && user.getUserId()!=0){
			Item savedItem = repo.save(item);
			entryToAuction(user, savedItem);
			return savedItem ;}
		return null;
	}
	
	public ItemPicture addPicture(ItemPicture ip){
		if(ip.getId()==0)
			return picRepo.save(ip);
		return null;
	}
	
	public Auction entryToAuction(User user, Item savedItem){
		Auction auction = new Auction();
		auction.setItemId(savedItem.getItemId());
		auction.setItem(savedItem);
		auction.setUser(user);
		auction.setVerified('N');
			return auctionRepo.save(auction);
	}

}
