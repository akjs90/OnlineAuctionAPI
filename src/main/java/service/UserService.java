package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import repository.UserRepository;
import repository.UserRoleRepository;
import entity.User;
import entity.UserRole;
import entity.UserWrapper;

@Service
public class UserService {

	@Autowired
	UserRoleRepository userRoleRepo;

	@Autowired
	UserRepository userRepo;

	public UserRole getUserRoleByName(String name) {
		return userRoleRepo.findByRole(name);
	}

	public boolean registerUser(User user) {
		user.setRole(getUserRoleByName("ROLE_USER"));
		User u = userRepo.save(user);
		if (u != null)
			return true;
		return false;
	}

	public User authorize(UserWrapper user) {
		User u = userRepo.getByUsernameAndPassword(user.getUsername(),
				user.getPassword());
		if (u != null)
			return u;
		return null;
	}
	
	public User findUserByUsername(String username){
		return userRepo.findByUsername(username);
	}
	
	public Page<User> findAllUsers(Pageable p){
		return userRepo.findAll(p);
	}
	
	public boolean toggleStatus(int id){
		if(userRepo.toogleStatus(id)==1)
			return true;
		return false;
	}
	
	public List<String> getByNameData(String name){
		return userRepo.getByName(name);
	}
	
	public List<String> getByUserNameData(String username){
		return userRepo.getByUserName(username);
	}
	public List<String> getByEmailData(String email){
		return userRepo.getByEmail(email);
	}
	
}
