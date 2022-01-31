package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.Blog;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.Writer;
import com.example.demo.service.MemberService;
import com.example.demo.service.UserService;
import com.example.demo.service.WebService;
import com.example.demo.services.UserDetailsServiceImpl;
import com.example.demo.util.JwtUtil;

@Controller
public class WebController {
	@Autowired
	private WebService webService;
	
	private User user;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@GetMapping("/editblog/{id}")
	public String editblog(Model model, @PathVariable("id") Long blogId) {
		Blog blog = webService.getBlogById(blogId);
		model.addAttribute("blog", blog);
		return "editblog.html";
	}
	
	@GetMapping("/deleteblog/{id}")
	public String deleteblog(Model model, @PathVariable("id") Long blogId) {
		webService.deleteBlog(blogId);
		return "redirect:/";
	}
	
	@RequestMapping("/updateBlog/{id}")
	public String updateBlog(@ModelAttribute("blog") Blog blog, @PathVariable(value = "id") Long id, @RequestParam("Image") MultipartFile file) throws IOException {
		Blog oldBlog = webService.getBlogById(id);
		oldBlog.setTitle(blog.getTitle());
		oldBlog.setContent(blog.getContent());
		
		String ImageFile = Base64.encodeBase64String(file.getBytes());
		if(ImageFile.length() != 0) {
			oldBlog.setImageFile(ImageFile);
		}
		webService.saveBlog(oldBlog);
		return "redirect:/"; 
	}
	
	@GetMapping("/blog/{id}")
	public String blog(Model model, @PathVariable("id") Long blogId) {
		Blog blog = webService.getBlogById(blogId);
		model.addAttribute("blog", blog);
		return "blog.html";
	}
	
	@GetMapping("/author/{id}")
	public String author(Model model, @PathVariable("id") Long authorId) {
		Writer author = userService.getWriterById(authorId);
		model.addAttribute("author", author);
		return "writer.html";
	}
	
	@GetMapping(value = {"", "/home"})
	public String home(Model model) {
		
		String username = null;
		Set<Role> role = null;
		String authrole = null;
		if(user != null) {
			username = user.getUsername();
			 role = user.getRoles();
			 for(Role rol: role) {
				 authrole = rol.getName();
			 }
			System.out.println("Username: " + username);
		}
		
		Page<Blog> page = webService.getPageByNum(1);
		int totalPages = page.getTotalPages();
		Long totalItems = page.getTotalElements();
		List<Blog> blogs = page.getContent();
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("blogs", blogs);
		model.addAttribute("authName", username);
		//model.addAttribute("authName", authName);
		model.addAttribute("authRole", authrole);
		//model.addAttribute("blogs", webService.getAllBlogs());
		return "index.html";
	}
	
	@GetMapping(value = {"/myblogs"})
	public String myblogs(Model model, HttpServletRequest request) {
		System.out.println(request);
		Page<Blog> page = webService.getPageByNum(1);
		int totalPages = page.getTotalPages();
		Long totalItems = page.getTotalElements();
		List<Blog> blogs = page.getContent();
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("blogs", blogs);
		//model.addAttribute("blogs", webService.getAllBlogs());
		return "myblogs.html";
	}
	
	@GetMapping("/page/{id}")
	public String home(Model model, @PathVariable("id") int pageNumber) {
		
		String username = null;
		Set<Role> role = null;
		String authrole = null;
		if(user != null) {
			username = user.getUsername();
			 role = user.getRoles();
			 for(Role rol: role) {
				 authrole = rol.getName();
			 }
			System.out.println("Username: " + username);
		}
		
		
		//System.out.println(pageNumber);
		Page<Blog> page = webService.getPageByNum(pageNumber);
		int totalPages = page.getTotalPages();
		Long totalItems = page.getTotalElements();
		List<Blog> blogs = page.getContent();
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("blogs", blogs);
		model.addAttribute("authName", username);
		//model.addAttribute("authName", authName);
		model.addAttribute("authRole", authrole);
		return "index.html";
	}
	
	@GetMapping("/addBlog")
	public String addBlog(Model model) {
		Writer writer = new Writer();
		model.addAttribute("writer", writer);
		Blog blog = new Blog();
		model.addAttribute("blog", blog);
		return "addBlog.html";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "register.html";
	}
	
	@PostMapping("/saveBlog")
	public String saveBlog(@ModelAttribute("blog") Blog blog, @RequestParam("Image") MultipartFile file) throws IOException {
		
		String base64EncodedImage = Base64.encodeBase64String(file.getBytes());
		blog.setImageFile(base64EncodedImage);
		
		String authorEmail = user.getUsername();
		String authorName = authorEmail; 
		
		Writer writer = userService.getWriterByEmail(authorEmail);
		if(writer == null) {
			writer = new Writer();
			writer.setEmail(authorEmail);
			writer.setName(authorName);
			userService.saveWriter(writer);
			blog.setWriter(writer);
		}
		else {
			blog.setWriter(writer);
		}
			
		writer.getBlogs().add(blog);
		blog.setWriter(writer); 
		webService.saveBlog(blog);
		return "redirect:/"; 
	}
	
	@PostMapping("/registeruser")
	public String registeruser(@ModelAttribute("user") User user) throws IOException {
		Role role = new Role();
		role.setName("USER");
		user.setEnabled(true);
		user.getRoles().add(role);
		memberService.saveMember(user);
		return "redirect:/login"; 
	}
	
	@RequestMapping("contact")
	public String contact() {
		System.out.println("contact");
		return "contact.html";
	}
	
	@RequestMapping("about")
	public String about() {
		System.out.println("about");
		return "about.html";
	}
	
	@RequestMapping("/token")
	public String generateTokenForUser(HttpServletResponse resp, HttpServletRequest req)
			throws Exception {
		System.out.println("helooo  ");
		user = memberService.findByName(req.getUserPrincipal().getName());
		
		System.out.println("UserName: "+ user.getUsername());
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
		
		System.out.println("USerdetails: "+ userDetails);
		String token = this.jwtTokenUtil.generateToken(userDetails);
		System.out.println("Token: " + token);
		
		Cookie cookie = new Cookie("token", token);
		cookie.setMaxAge(-1);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setPath("/user/");
		resp.addCookie(cookie);
		return "redirect:/home";
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.GET)
	public String StartAuthenticationProcess() {
		return "redirect:/login";
	}
	
	@RequestMapping("/403")
	public String accessDenied(Model model) {
		return "403.html";
	}
	
}