package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entity.UserRole;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	UserRole findByRole(String roleName);
}
