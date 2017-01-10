package repository;

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
}
