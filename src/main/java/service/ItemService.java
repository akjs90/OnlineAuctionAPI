package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Auction;
import entity.Item;
import entity.ItemPicture;
import entity.User;
import repository.AuctionRepository;
import repository.BidRepository;
import repository.ItemPictureRepository;
import repository.ItemRepository;

@Service
public class ItemService {
	
	@Autowired
	ItemRepository repo;
	
	@Autowired
	ItemPictureRepository picRepo;
	
	@Autowired
	BidRepository bidRepo;
	
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

	public ArrayList<Item> getItemByUser(User user){
		/*ArrayList<Item> items = new ArrayList<Item>();
		ArrayList<Auction> returnedObjects =(ArrayList<Auction>) auctionRepo.findByUser(user);
		for (Auction obj : returnedObjects) {
			Item i = (Item) obj.getItem();
			items.add(i);
		}
		return items;*/
		return (ArrayList<Item>) auctionRepo.findItemByUser(user);
	}
	
	public ArrayList<Item> getItemByUserAndVerified(User user, char verified){
		ArrayList<Item> returnedItems = new ArrayList<Item>();
		ArrayList<Auction> returnedAuction = new ArrayList<Auction>();
		if(verified =='X'){
			returnedAuction = (ArrayList<Auction>) auctionRepo.findByUserAndVerified(user, 'A');
			for (Auction auction : returnedAuction) {
				if(auction.getStartDate().before(new Date()) && auction.getEndDate().after(new Date())){
					returnedItems.add(auction.getItem());
				}
			}
		}
		else{
			returnedItems = (ArrayList<Item>) auctionRepo.findItemByUserAndVerified(user, verified);
		}
			
		return returnedItems;
	}
	
	public Item getItem(int id){
		return repo.getOne(id);
	}
	public Item modifyItem(Item item){
		Item dbItem = repo.findOne(item.getItemId());
		dbItem.setBasePrice(item.getBasePrice());
		dbItem.setDescription(item.getDescription());
		dbItem.setName(item.getName());
		return repo.save(dbItem);
		
	}
	public void deleteImage(int id){
		picRepo.delete(id);
	}
	
	public String getOngoingBids(Auction auction){
		Object[] obj = bidRepo.getOngoingBids(auction);
		System.out.println(obj.length);
		if(obj == null)
			return null;
		JSONObject jObj = new JSONObject();
		jObj.put("maxBid", obj[0]);
		jObj.put("totalBids", obj[1]);
		jObj.put("totalBidders", obj[2]);
		return obj.toString();
	}
}
