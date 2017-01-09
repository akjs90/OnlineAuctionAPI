package repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	User getByUsernameAndPassword(String username, String password);
	User findByUsername(String username);
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.enabled=?2 where u.userId=?1")
	Integer toogleStatus(Integer userid,String status);
}
