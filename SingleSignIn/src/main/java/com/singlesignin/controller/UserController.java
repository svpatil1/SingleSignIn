package com.singlesignin.controller;


import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.dashboard.domain.Peanut_account;
import com.dashboard.service.Peanut_accountService;
import com.singlesignin.command.LoginCommand;
import com.singlesignin.command.UserCommand;
import com.singlesignin.domain.User;
import com.singlesignin.exception.UserBlockedException;
import com.singlesignin.service.UserService;
import com.singlesignin.db1.*;

/**
 * This class provides mapping to HTTP requests.
 * @author sanika
 */

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;  // userService is injected in UserController
	@Autowired 
	private Peanut_accountService peanut_accountService;
	
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public ModelAndView dashboard(ModelMap m) {
		return new ModelAndView("redirect:dashboard",m);
	}
	
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.POST)
	public ModelAndView postDashboard(ModelMap m) {
		return new ModelAndView("redirect:dashboard",m);
	}
	
	@RequestMapping(value = {"/dashboard"}, method = RequestMethod.GET)
	public ModelAndView getDashboard(Model m) {
		ModelAndView mav = new ModelAndView("/index");
		return mav;
	}
	
	@RequestMapping(value = {"/get_login"}, method = RequestMethod.GET)
	public ModelAndView index(ModelMap m) {
		m.addAttribute("command", new LoginCommand());
		return new ModelAndView("redirect:index_login",m);
	}
	
	@RequestMapping(value = {"/index_login"}, method = RequestMethod.GET)
	public ModelAndView index(Model m) {
		m.addAttribute("command", new LoginCommand());
		ModelAndView mav = new ModelAndView("/index_login");
		return mav;
	}
	
	/*
	 * handleLogin() method is called by dispatcher servlet(front controller). This controller will receive the request from user,
	 * read the data from the request parameter(Login details). And then will bind loginName and password to LoginCommand object.
	 * Then LoginCommand object will be given to controller method.
	 */
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView handleLogin(@ModelAttribute("command") LoginCommand cmd, Model m, HttpSession session) {
		
		try {
			User loggedInUser = userService.login(cmd.getLoginName(),cmd.getPassword());
			/*
			 * add error message and go back to login form
			 */
			if(loggedInUser == null) {
				
				m.addAttribute("err", "Login Failed! Enter valid credentials");
				ModelAndView mav = new ModelAndView("/index_login");
				return mav ; 	
			}
			/*
			 * On Successful login, check the role (authorization), add the user in session and redirect to appropriate dashboard.
			 * The redirecting to the correct dashboard is handled in index.jsp
			 */
			else {			
				addUserInSession(loggedInUser, session);
				
				Integer userID = (Integer) session.getAttribute("userId");
				String loginName = (String) session.getAttribute("loginName");
				String role = (String) session.getAttribute("role");
				db1 db=new db1();
				db.logged(userID, loginName, role);
			    int sessionID=db.getSessionID(userID);
			    session.setAttribute("sessionID",sessionID);
			    session.setAttribute("userId",userID);
			    session.setAttribute("loginName",loginName);
				
				
				ModelAndView mav = new ModelAndView("redirect:index");
				return mav ; 
				}
		} catch (UserBlockedException e) {
			m.addAttribute("err", e.getMessage()); // add error message and go back to login form
			ModelAndView mav = new ModelAndView("/index");
			return mav ; 
		}
		
	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		Integer userId = (Integer) session.getAttribute("userId");
		session.setAttribute("role", null);
		db1 db = new db1();
		db.deleteLogged(userId);
		session.invalidate();
		ModelAndView mav = new ModelAndView("redirect:index?act=lo");
		return mav;
	}
	
	/*
	 * Add the logged in user in session
	 */
	private void addUserInSession(User u, HttpSession session) {
		
		session.setAttribute("user", u);
		session.setAttribute("userId", u.getUserId());
		session.setAttribute("role", u.getRole());
		session.setAttribute("loginName", u.getLoginName());
	}
	
	@RequestMapping(value = "/reg_form", method = RequestMethod.GET)
	public ModelAndView registrationForm(Model m) {
		UserCommand cmd = new UserCommand();
		m.addAttribute("command", cmd);
		ModelAndView mav = new ModelAndView("/reg_form");
		return mav;
	}
	
	/*
	 * registerUser() method is called by dispatcher servlet(front controller). This controller will receive the request from user,
	 * read the data from the request parameter(Registration details). And then will bind user details to User object.
	 */
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerUser(@ModelAttribute("command") User user, Model m) {
		try {	
			
			/*
			 * Check if the user has selected the role before registering. If not then throw error
			 */
			
			if(user.getRole() == null) {
				m.addAttribute("err", "Select correct role.");
				ModelAndView mav = new ModelAndView("/reg_form");
				return mav;
			}
			else {
				
				/*
				 * Check the length of password used for registration, to have proper encryption.
				 * After successful registration, the user is redirected to login.
				 */
				
				if(user.getPassword().length()<8) {
					m.addAttribute("err", "Registration Failed! Enter password with 8 characters");
					ModelAndView mav = new ModelAndView("/reg_form");
					return mav;
				}
				else if(user.getPassword().length()>8) {
					m.addAttribute("err", "Registration Failed! Enter password with 8 characters");
					ModelAndView mav = new ModelAndView("/reg_form");
					return mav;
				}
				else {	
					user.setLoginStatus(userService.LOGIN_STATUS_ACTIVE);
					//System.out.println(user.getLoginName());
					userService.register(user);
					Peanut_account p = null;
					try {
						peanut_accountService.createAccount(p, user.getUserId());
					} catch (Exception e) {
						m.addAttribute("err", "Peanut account not created!.");
					}
				}
			}
				ModelAndView mav = new ModelAndView("redirect:index_login?act=reg");
				return mav;
		}catch(DuplicateKeyException e) {
			e.printStackTrace();
			m.addAttribute("err", "Username already exist. Please select different username."); // it takes care that the username is unique.
			ModelAndView mav = new ModelAndView("/reg_form");
			return mav;
		}
			
	}
	
}