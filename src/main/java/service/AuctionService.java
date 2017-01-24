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
}
