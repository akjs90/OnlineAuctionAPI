package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.Auction;
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
	
	List<Auction> findByVerifiedOrderByItem_RegistrationDateDesc(Character flag);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE Auction a SET a.verified='R' WHERE a.itemId=:auction")
	int rejectItem(@Param("auction")int auction_id);
}
