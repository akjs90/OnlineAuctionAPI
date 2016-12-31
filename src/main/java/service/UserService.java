package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.UserRepository;
import repository.UserRoleRepository;
import entity.User;
import entity.UserRole;
@Service
public class UserService {

	@Autowired
	UserRoleRepository userRoleRepo;
	
	@Autowired
	UserRepository userRepo;
	
	public UserRole getUserRoleByName(String name){
		return userRoleRepo.findByRole(name);
	}
	
	public boolean registerUser(User user){
		user.setEnabled("inactive");
		user.setRole(getUserRoleByName("ROLE_USER"));
		User u=userRepo.save(user);
		if(u!=null)
			return true;
		return false;
	}
}
