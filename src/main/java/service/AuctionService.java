package service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.AuctionRepository;
import entity.Auction;
import entity.OngoingAuction;

@Service
public class AuctionService {
	@Autowired
	AuctionRepository auctionRepo;

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
			
			Date end=(Date)obj[2];
			Date curr=new Date();
			long remainingTime=end.getTime()-curr.getTime();
			OngoingAuction auction=new OngoingAuction((int)obj[0], obj[1].toString(), BigDecimal.valueOf(12), remainingTime);
			ongoingAuctions.add(auction);
		}
		System.out.println(li.size());
		return ongoingAuctions;
	}
}
