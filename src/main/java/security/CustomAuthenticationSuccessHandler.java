package security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import entity.UserWrapper;
@Component
public class CustomAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {
	RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth) throws IOException,
			ServletException {
		HttpSession session=request.getSession();
		UserWrapper userWrapper=new UserWrapper();
		User user=(User) auth.getPrincipal();
		Object[] authorities=  user.getAuthorities().toArray();
		System.out.println(Arrays.toString(authorities));
		userWrapper.setUsername(user.getUsername());
		userWrapper.setRole(authorities[0].toString());
		System.out.println("Custm redirect: "+userWrapper.getUsername());
		session.setAttribute("user_info", userWrapper);
		redirectStrategy.sendRedirect(request, response, "/");
		
	}
}
