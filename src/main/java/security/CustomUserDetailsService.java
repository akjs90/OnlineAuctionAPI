package security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import entity.User;
import service.UserService;
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userService.findUserByUsername(username);
		// user not exists
		System.out.println("In security");
		if (user == null) {
			System.out.println("In security---->null");
			throw new UsernameNotFoundException("Username " + username + " does not exists.");
		}
		if (user.getRole() == null)// no role assigned
		{	System.out.println("In security--->no role");
			throw new UsernameNotFoundException("User not authorised.");
		
		}
		
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		
		grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getRole()));
		
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true,
				grantedAuthorities);
		System.out.println(userDetails.isEnabled());
		return userDetails;
	}

}
