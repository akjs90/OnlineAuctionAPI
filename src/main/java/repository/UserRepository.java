package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	User getByUsernameAndPassword(String username, String password);
	User findByUsername(String username);
	@Modifying
	@Transactional
	@Query(value="UPDATE `users` u SET u.`enabled`=NOT(u.`enabled`) WHERE u.`user_id`=:userid",nativeQuery=true)
	int toogleStatus( @Param("userid")int userid);
	@Query(value="SELECT u.name FROM User u WHERE u.name LIKE %?1%")
	List<String> getByName(String name);
	@Query(value="SELECT u.username FROM User u WHERE u.username LIKE %?1%")
	List<String> getByUserName(String username);
	@Query(value="SELECT u.email FROM User u WHERE u.email LIKE %?1%")
	List<String> getByEmail(String email);
}
