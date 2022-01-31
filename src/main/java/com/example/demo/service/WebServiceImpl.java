package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BlogDao;
import com.example.demo.model.Blog;

@Service
public class WebServiceImpl implements WebService {
	@Autowired
	private BlogDao blogDao;
	
	@Override
	public List<Blog> getAllBlogs() {
		return blogDao.findAll();
	}

	@Override
	public void saveBlog(Blog blog) {
		blogDao.save(blog);
	}

	@Override
	public Blog getBlogById(Long id) {
		return blogDao.getById(id);
	}

	@Override
	public Page<Blog> getPageByNum(int pageNumber) {
		PageRequest pageable = PageRequest.of(pageNumber-1, 5);
		return blogDao.findAll(pageable);
	}

	@Override
	public void deleteBlog(Long id) {
		blogDao.deleteById(id);
	}
	
}
