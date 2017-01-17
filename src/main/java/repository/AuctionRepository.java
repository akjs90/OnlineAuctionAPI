package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {

}
