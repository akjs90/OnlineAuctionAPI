package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import entity.Auction;
import entity.Bid;

public interface BidRepository extends CrudRepository<Bid, Integer> {
	
	@Query("Select max(b.bidPrice),count(b),count(distinct b.user) from Bid b where b.auction =:auction")
	//@Query(value="Select max(`bid_price`),count(*),count(distinct `bidder_id`) from bids where `auction_id` =:id",nativeQuery= true)
	List<Object[]>  getOngoingBids(@Param("auction") Auction auction);
	
	@Query(value="SELECT * FROM `bids` WHERE `auction_id` =:id order by `bid_price` desc limit 10",nativeQuery= true)
	List<Bid> findTopTenBids(@Param("id") int id);
}
