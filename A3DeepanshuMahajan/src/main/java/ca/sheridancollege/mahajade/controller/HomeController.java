/*  
 * HTTPS runs on port 8443
 * HTTP runs on port 8080
 * 
 * Usernames and Passwords are as below
 * 
 * username: "employee1" and password = "pass" and role = "EMPLOYEE"
 * username: "employee2" and password = "pass" and role = "EMPLOYEE"
 * username: "employee3" and password = "pass" and role = "EMPLOYEE"
 * username: "user1" and password = "pass" and role = "USER"
 */


package ca.sheridancollege.mahajade.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.mahajade.beans.Employee;
import ca.sheridancollege.mahajade.beans.Post;
import ca.sheridancollege.mahajade.beans.Reply;
import ca.sheridancollege.mahajade.database.DatabaseAccess;

@Controller
public class HomeController {

	@Autowired
	private JdbcUserDetailsManager jdbc;
	
	@Autowired
	private DatabaseAccess da;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@GetMapping("/")
	public String getIndex() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String getHome(Model model) {
		ArrayList<Post> list = new ArrayList<Post>();
		list = da.getPosts();
		model.addAttribute("list",list);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("username", auth.getName());
		return "home";
	}
	
	@GetMapping("/addPost")
	public String getAddPost(Post post, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null)
			post.setUsername(auth.getName());
		post.setDate(LocalDate.now());
		post.setTime(LocalTime.now());
		model.addAttribute("post",post);
		return "addPost";
	}
	
	@PostMapping("/addPost")
	public String addPost(@ModelAttribute Post post) {
		da.addPost(post);
		return "redirect:/home";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model, Post post) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null)
			post.setUsername(auth.getName());
		post = da.getPostById(id);
		post.setId(Long.valueOf(id));
		post.setDate(LocalDate.now());
		post.setTime(LocalTime.now());
		model.addAttribute("post",post);
		return "edit";
	}
	
	@PostMapping("/edit")
	public String postEdit(@ModelAttribute Post post) {
		da.edit(post.getId().intValue(), post);
		return "redirect:/home";
	}
	
	@GetMapping("/getReplies/{id}")
	public String getReplies(@PathVariable int id, Model model) {
		ArrayList<Reply> replies = da.getReplies(id);
		Post list = da.getPostById(id);
		if(replies.size()==0)
			model.addAttribute("msg","No Replies Yet!");
		model.addAttribute("list",list);
		model.addAttribute("replies",replies);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("username", auth.getName());
		return "replies";
	}
	
	@GetMapping("/addReply/{id}")
	public String getReply(Model model, Reply reply, @PathVariable int id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null)
			reply.setUsername(auth.getName());
		reply.setId(Long.valueOf(id));
		reply.setDate(LocalDate.now());
		reply.setTime(LocalTime.now());
		model.addAttribute("reply",reply);
		return "addReply";
	}
	
	@PostMapping("/addReply")
	public String postReply(@ModelAttribute Reply reply) {
		da.addReply(reply);
		return "redirect:/home";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@GetMapping("/addEmployee")
	public String getAddEmployee(Employee employee, Model model) {
		model.addAttribute("employee",employee);
		return "addEmployee";
	}
	
	@PostMapping("/addEmployee")
	public String postAddEmployee(@ModelAttribute Employee employee, HttpSession s) {
		ArrayList<GrantedAuthority> emp = new ArrayList<GrantedAuthority>();
		emp.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
		User user = new User(employee.getUsername(),encoder.encode(employee.getPassword()),emp);
		jdbc.createUser(user);
		s.setAttribute("msg", "Employee Account Created");
		return "redirect:/home";
	}
	
	@GetMapping("/accessDenied")
	public String accessDenied() {
		return "accessDenied";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		da.deletePost(id);
		return "redirect:/home";
	}
}
