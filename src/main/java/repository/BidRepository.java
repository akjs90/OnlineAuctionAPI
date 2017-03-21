package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import entity.Auction;
import entity.Bid;

public interface BidRepository extends CrudRepository<Bid, Integer> {
	
	@Query("Select max(b.bidPrice),count(b),count(distinct b.user) from Bid b where b.auction =:auction")
	Object[] getOngoingBids(@Param("auction") Auction auction);
}
