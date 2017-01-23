package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Auction;
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
	List<Auction> findByVerified(Character flag);
}
