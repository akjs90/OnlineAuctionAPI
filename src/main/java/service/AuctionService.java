package service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import repository.AuctionRepository;
import repository.ItemPictureRepository;
import entity.Auction;
import entity.Item;
import entity.ItemPicture;
import entity.OngoingAuction;

@Service
public class AuctionService {
	@Autowired
	AuctionRepository auctionRepo;
	@Autowired
	ItemPictureRepository picRepo;

	public List<Auction> getRecentAuctionRequest() {
		return auctionRepo.findByVerifiedOrderByItem_RegistrationDateDesc('N');
	}

	public boolean rejectAuction(int auction_id) {
		int row_count = auctionRepo.rejectItem(auction_id);
		if (row_count == 1)
			return true;
		return false;
	}

	public boolean verifyAuctionRequest(String start, String end, int auc_id) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		int rows = auctionRepo.verifyItem(sdf.parse(start), sdf.parse(end), auc_id);
		if (rows == 1)
			return true;
		return false;
	}
	
	public List<OngoingAuction> getOngoingAuctionList(){
		List<Object[]> li=auctionRepo.getOngoingAuctions();
		if(li.size()==0)
			return null;
		List<OngoingAuction> ongoingAuctions=new ArrayList<OngoingAuction>();
		for(Object[] obj : li){
			System.out.println(obj.length);
			Date end=(Date)obj[2];
			Date curr=new Date();
			long remainingTime=end.getTime()-curr.getTime();
			BigInteger d=(BigInteger)obj[4];
			int a=d.intValueExact();
			BigInteger e=(BigInteger)obj[5];
			System.out.println(e.toString());
			int g=e.intValueExact();
			OngoingAuction auction=new OngoingAuction((int)obj[0], obj[1].toString(), (BigDecimal)obj[3], remainingTime,a, g);
			ongoingAuctions.add(auction);
		}
		System.out.println(li.size());
		return ongoingAuctions;
	}
	public String[] getItemImages(int auction_id){
		Item i=auctionRepo.findItemByItemId(auction_id);
		if(null==i)
			return null;
		int k=0;
		String[] urls=new String[i.getItemPictures().size()];
		for(ItemPicture pic:i.getItemPictures()){
			urls[k++]=pic.getPictureUrl();
		}
		return urls;
	}
	@Scheduled(cron="0 0/30 * * * *")
	public void checkForAuctionCompletion(){
		int completed = auctionRepo.markCompleted();
		System.out.println("Completed "+ completed+" Auctions "+new Date().getTime());
	}
	
	public JSONArray getCompletedAuctions(){
	  List<Object[]> list=auctionRepo.getCompletedAuctions();
	  JSONArray arr=new JSONArray();
	  for(Object[] auc:list){
		  Date d1=(Date) auc[2];
		  Date d2=(Date) auc[3];
		  JSONObject obj=new JSONObject();
		  obj.put("auction_id", auc[0]);
		  obj.put("item_name", auc[1]);
		  obj.put("start_date", d1.getTime());
		  obj.put("end_date", d2.getTime());
		  obj.put("wining_bid", auc[4]);
		  obj.put("total_bids", auc[5]);
		  obj.put("total_bidders", auc[6]);
		  arr.put(obj);
	  }
	  return arr;
	}
	

	public List<Auction> getActiveAuction(){
		
		return auctionRepo.getActiveAuctions();
	} 

	public String getAuctionForHomepage(){
		JSONArray array=new JSONArray();
		//get Popular ongoing auction top 3
		List<Object[]> popular=auctionRepo.getPopularOngoingAuction();
		for (Object[] obj:popular) {
			JSONObject json=new JSONObject();
			json.put("type", "ongoing");
			json.put("auction_id", obj[0]);
			json.put("name", obj[1]);
			Date d=(Date) obj[3];
			json.put("end", d.getTime());
			//get image here
			ItemPicture picture=picRepo.findTop1ByItem_ItemId(Integer.parseInt(obj[0].toString()));
			json.put("image",picture.getPictureUrl());
			array.put(json);
		}
		//get  completed top 3 in last 50 days
		
		//get 3 upcoming in next 50 days
		popular=auctionRepo.getUpcomingActionIn50Days();
		for(Object[] obj:popular){
			JSONObject json=new JSONObject();
			json.put("type", "upcoming");
			json.put("item_id", obj[0]);
			json.put("name", obj[1]);
			Date d=(Date) obj[2];
			json.put("start", d.getTime());
			d=(Date) obj[3];
			json.put("end", d.getTime());
			ItemPicture picture=picRepo.findTop1ByItem_ItemId(Integer.parseInt(obj[0].toString()));
			json.put("image",picture.getPictureUrl());
			array.put(json);
		}
		//System.out.println("assas0"+array.toString());
		
		return array.toString();
	}
	public List<Object[]> getCompletedObjectList(){
		
		return auctionRepo.getCompletedAuctions();
	}
	
	public List<Object[]> getUpcomingAuctions(){
		return auctionRepo.getUpcomingActionIn50Days();
	}
}
