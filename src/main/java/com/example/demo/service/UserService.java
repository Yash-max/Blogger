package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.model.Writer;

@Service
public interface UserService {
	List<Writer> getAllWriters();
	void saveWriter(Writer blog);
	Writer getWriterById(Long id);
	Writer getWriterByEmail(String email);
}