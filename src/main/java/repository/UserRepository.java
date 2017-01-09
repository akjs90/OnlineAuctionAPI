package repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	User getByUsernameAndPassword(String username, String password);

	User findByUsername(String username);
}
