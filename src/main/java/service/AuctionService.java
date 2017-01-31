package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.AuctionRepository;
import entity.Auction;
@Service
public class AuctionService {
	@Autowired
	AuctionRepository auctionRepo;
	
	public List<Auction> getRecentAuctionRequest() {
		return auctionRepo.findByVerifiedOrderByItem_RegistrationDateDesc('N');
	}
	
	public boolean rejectAuction(int auction_id){
		int row_count=auctionRepo.rejectItem(auction_id);
		if(row_count==1)
			return true;
		return false;
	}
}
