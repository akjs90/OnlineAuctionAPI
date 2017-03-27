package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Bid;
import repository.BidRepository;

@Service
public class BidService {

	@Autowired
	BidRepository repo;
	
	public List<Bid> findTopTenBids(int id){
		return repo.findTopTenBids(id);
	}
}
