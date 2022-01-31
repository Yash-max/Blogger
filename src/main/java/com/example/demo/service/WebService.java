package com.example.demo.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.model.Blog;

@Service
public interface WebService {
	List<Blog> getAllBlogs();
	void saveBlog(Blog blog);
	void deleteBlog(Long id);
	Blog getBlogById(Long id);
	Page<Blog> getPageByNum(int pageNumber);
}
