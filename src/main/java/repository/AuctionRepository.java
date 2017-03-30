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
	int verifyItem(@Param("start") Date start_date,
			@Param("end") Date end_date, @Param("auction") int auction_id);

	@Query(value = "SELECT a.item_id,i.name,a.end_date,max(b.bid_price) as `bid_price`,count(b.bid_price) as `Total Bids` , count(distinct(b.bidder_id)) as `total bidders` FROM `auction` as a Left join `bids` as b ON item_id=auction_id Left join `item` as i ON i.item_id=a.item_id WHERE current_timestamp between a.start_date and a.end_date AND a.verified='A' group by i.item_id ", nativeQuery = true)
	List<Object[]> getOngoingAuctions();

	List<Auction> findByUserAndVerified(User user, char verified);

	// not using right now... but can be used if want to know how many items pr
	// put on site by the user.
	// List<Auction> findByUser(User user);

	@Query(value = "Select a.item from Auction a where a.user =:user")
	List<Item> findItemByUser(@Param("user") User user);

	@Query(value = "Select a.item from Auction a where a.user =:user and a.verified =:verified")
	List<Item> findItemByUserAndVerified(@Param("user") User user,
			@Param("verified") char verified);

	@Query(value = "SELECT a.item from Auction a where a.itemId=:id")
	Item findItemByItemId(@Param("id") int auction_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE Auction a SET a.verified='C' WHERE current_timestamp>a.endDate AND a.verified!='N' AND a.verified!='C'")
	int markCompleted();

	@Query(value = "SELECT a.item_id,i.name,a.start_date,a.end_date,max(b.bid_price) as `bid_price`,count(b.bid_price) as `Total Bids` , count(distinct(b.bidder_id)) as `total bidders` FROM `auction` as a Left join `bids` as b ON item_id=auction_id Left join `item` as i ON i.item_id=a.item_id WHERE a.verified='C' OR a.verified='A' AND current_timestamp>a.end_date  group by i.item_id order by a.end_date desc ", nativeQuery = true)
	List<Object[]> getCompletedAuctions();

	@Query(value = "SELECT `item`.`item_id`,`item`.`name`, COUNT(DISTINCT `bids`.`bidder_id`), `auction`.`end_date` FROM `auction` LEFT JOIN `bids` ON `bids`.`auction_id` = `auction`.`item_id`LEFT JOIN `item`ON `item`.`item_id`=`auction`.`item_id` WHERE CURRENT_TIMESTAMP BETWEEN `auction`.`start_date` AND `auction`.`end_date` AND`auction`.`verified`='A' GROUP BY `item`.`item_id` LIMIT 3", nativeQuery = true)
	List<Object[]> getPopularOngoingAuction();

	@Query(value="SELECT i.item_id,i.name, a.start_date,a.end_date FROM auction a JOIN item i ON i.item_id=a.item_id WHERE a.verified='A' and date(a.start_date) Between current_date and DAte_add(Current_date,interval 50 day) limit 3 ", nativeQuery=true)
	List<Object[]> getUpcomingActionIn50Days();
	/*
	 * SELECT * FROM `auction` Where `auction`.verified='A' and date(`start_date` ) Between current_date and DAte_add(Current_date,interval 50 day) limit 3
	 * SELECT * FROM `auction` Where
	 * `auction`.verified='C' and date(`end_date` ) Between current_date and
	 * DAte_sub(Current_date, interval 50 day) limit 3;
	 */
}
