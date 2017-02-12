package repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.Auction;
import entity.Item;
import entity.User;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {

	List<Auction> findByVerifiedOrderByItem_RegistrationDateDesc(Character flag);

	@Modifying
	@Transactional
	@Query(value = "UPDATE Auction a SET a.verified='R' WHERE a.itemId=:auction")
	int rejectItem(@Param("auction") int auction_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE Auction a SET a.verified='A', a.endDate=:end, a.startDate=:start WHERE a.itemId=:auction")
	int verifyItem(@Param("start") Date start_date, @Param("end") Date end_date, @Param("auction") int auction_id);

	
	@Query(value="SELECT a.item_id,i.name,a.end_date,max(b.bid_price) as `bid_price`,count(b.bid_price) as `Total Bids` , count(distinct(b.bidder_id)) as `total bidders` FROM `auction` as a Left join `bids` as b ON item_id=auction_id Left join `item` as i ON i.item_id=a.item_id WHERE current_timestamp between a.start_date and a.end_date AND a.verified='A' group by i.item_id ", nativeQuery=true)
	List<Object[]> getOngoingAuctions();

	
	//List<Auction> findByUser(User user);

	@Query(value="Select a.item from Auction a where a.user =:user")
	List<Item> findItemByUser(@Param("user") User user);

}
